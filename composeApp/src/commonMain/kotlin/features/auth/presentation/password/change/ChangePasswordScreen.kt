package features.auth.presentation.password.change

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import core.error.GlobalErrorHandler
import dev.icerock.moko.resources.compose.stringResource
import features.auth.presentation.password.otp.PasswordOtpScreen
import features.common.ui.collectInLaunchedEffect
import org.koin.compose.koinInject
import ui.components.ScreenWrapper
import uz.hb.b2b.SharedRes

data class ChangePasswordScreen(val login: String, val password: String) : Screen {
    @Composable
    override fun Content() {
        val viewModel = koinInject<ChangePasswordViewModel>()
        val navigator = LocalNavigator.currentOrThrow
        var newPass by remember { mutableStateOf("") }

        val snackbarHostState = remember { SnackbarHostState() }
        val state by viewModel.state.collectAsState()

        // Инъекция с параметрами!
        val globalErrorHandler = koinInject<GlobalErrorHandler>(
            parameters = { org.koin.core.parameter.parametersOf(navigator) }
        )

        viewModel.sideEffect.collectInLaunchedEffect { effect ->
            // 1. Делегируем глобальные сайд-эффекты
            globalErrorHandler.handle(effect)

            // 2. Фичевые эффекты
            when (effect) {
                is ChangePasswordSideEffect.NavigateToOtp -> navigator.push(PasswordOtpScreen(login, password))
                ChangePasswordSideEffect.NavigateBack -> { /* обработано глобально */ }
                ChangePasswordSideEffect.SessionExpired -> { /* обработано глобально */ }
                is ChangePasswordSideEffect.ShowError -> {
                    snackbarHostState.showSnackbar(effect.message)
                }
            }
        }

        Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { paddingValues ->
            ScreenWrapper {
                Column(Modifier.padding(16.dp)) {
                    OutlinedTextField(
                        value = newPass,
                        onValueChange = { newPass = it },
                        label = {
                            Text(
                                stringResource(
                                    SharedRes.strings.new_password
                                )
                            )
                        }
                    )
                    Button(onClick = {
                        viewModel.reduce(
                            ChangePasswordIntent.SubmitNewPassword(
                                username = login,
                                password = password,
                                newPassword = newPass
                            )
                        )
                    }, modifier = Modifier.padding(top = 8.dp)) {
                        Text(
                            stringResource(
                                SharedRes.strings.to_continue
                            )
                        )
                    }
                }
            }
        }
    }
}
