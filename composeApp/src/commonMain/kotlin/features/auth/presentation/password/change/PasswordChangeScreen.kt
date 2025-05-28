package features.auth.presentation.password.change

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import dev.icerock.moko.resources.compose.stringResource
import features.auth.presentation.password.otp.PasswordOtpScreen
import org.koin.compose.koinInject
import ui.components.ScreenWrapper
import uz.hb.b2b.SharedRes

data class PasswordChangeScreen(val login: String, val oldPassword: String) : Screen {
    @Composable
    override fun Content() {
        val viewModel = koinInject<PasswordChangeViewModel>()
        val navigator = LocalNavigator.currentOrThrow
        var newPass by remember { mutableStateOf("") }

        val state by viewModel.state.collectAsState()

        LaunchedEffect(state) {
            if (state is PasswordChangeState.WaitingForPasswordOtp) {
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
                    viewModel.dispatch(PasswordChangeIntent.SubmitNewPassword(username = "", password = ""))
                }, modifier = Modifier.padding(top = 8.dp)) {
                    Text(stringResource(
                        SharedRes.strings.to_continue))
                }
            }
        }
    }
}
