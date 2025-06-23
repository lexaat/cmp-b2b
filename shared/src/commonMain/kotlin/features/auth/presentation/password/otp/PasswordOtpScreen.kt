package features.auth.presentation.password.otp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import features.auth.presentation.AuthScreen
import features.common.ui.collectInLaunchedEffect
import org.koin.compose.koinInject
import ui.components.ScreenWrapper
import uz.hb.shared.SharedRes

data class PasswordOtpScreen(val login: String, val oldPassword: String, val newPassword: String) : Screen {
    @Composable
    override fun Content() {
        val viewModel = koinInject<PasswordOtpViewModel>()
        val navigator = LocalNavigator.currentOrThrow
        var otp by remember { mutableStateOf("") }

        val snackbarHostState = remember { SnackbarHostState() }

        // Инъекция с параметрами!
        val globalErrorHandler = koinInject<GlobalErrorHandler>(
            parameters = { org.koin.core.parameter.parametersOf(navigator) }
        )

        viewModel.sideEffect.collectInLaunchedEffect { effect ->
            // 1. Делегируем глобальные сайд-эффекты
            globalErrorHandler.handle(effect)

            // 2. Фичевые эффекты
            when (effect) {
                is PasswordOtpSideEffect.NavigateToLogin -> {
                    navigator.popUntilRoot()
                    navigator.push(AuthScreen)
                }
                PasswordOtpSideEffect.NavigateBack -> { /* обработано глобально */ }
                PasswordOtpSideEffect.SessionExpired -> { /* обработано глобально */ }
                is PasswordOtpSideEffect.ShowError -> {
                    snackbarHostState.showSnackbar(effect.message)
                }
            }
        }

        Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { paddingValues ->
            ScreenWrapper {
                Column(Modifier.padding(16.dp)) {
                    OutlinedTextField(value = otp, onValueChange = { otp = it }, label = {
                        Text(
                            stringResource(
                                SharedRes.strings.sms_code
                            )
                        )
                    })
                    Button(onClick = {
                        viewModel.reduce(
                            ChangePasswordOtpIntent.SubmitNewPassword(
                                username = login,
                                password = oldPassword,
                                newPassword = newPassword,
                                otp = otp
                            )
                        )
                    }, modifier = Modifier.padding(top = 8.dp)) {
                        Text(
                            stringResource(
                                SharedRes.strings.change_password
                            )
                        )
                    }
                }
            }
        }
    }
}
