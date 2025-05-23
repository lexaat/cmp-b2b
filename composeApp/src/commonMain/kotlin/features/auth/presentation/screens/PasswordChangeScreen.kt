package features.auth.presentation.screens

import androidx.compose.runtime.*
import cafe.adriel.voyager.core.screen.Screen
import features.auth.presentation.login.LoginIntent
import features.auth.presentation.login.LoginViewModel
import org.koin.compose.koinInject
import ui.components.ScreenWrapper
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import dev.icerock.moko.resources.compose.stringResource
import features.auth.presentation.login.LoginState
import uz.hb.b2b.SharedRes

data class PasswordChangeScreen(val login: String, val oldPassword: String) : Screen {
    @Composable
    override fun Content() {
        val viewModel = koinInject<LoginViewModel>()
        val navigator = LocalNavigator.currentOrThrow
        var newPass by remember { mutableStateOf("") }

        val state by viewModel.state.collectAsState()

        LaunchedEffect(state) {
            if (state is LoginState.WaitingForPasswordOtp) {
                navigator.push(PasswordOtpScreen(login, oldPassword, newPass))
            }
        }

        ScreenWrapper {
            Column(Modifier.padding(16.dp)) {
                OutlinedTextField(
                    value = newPass,
                    onValueChange = { newPass = it },
                    label = { Text(
                        stringResource(
                        SharedRes.strings.new_password)
                    ) }
                )
                Button(onClick = {
                    viewModel.dispatch(LoginIntent.SubmitNewPassword(login, oldPassword, newPass))
                }, modifier = Modifier.padding(top = 8.dp)) {
                    Text(stringResource(
                        SharedRes.strings.to_continue))
                }
            }
        }
    }
}
