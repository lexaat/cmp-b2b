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
import dev.icerock.moko.resources.compose.stringResource
import uz.hb.b2b.SharedRes

data class PasswordOtpScreen(val login: String, val oldPassword: String, val newPassword: String) : Screen {
    @Composable
    override fun Content() {
        val viewModel = koinInject<LoginViewModel>()
        var otp by remember { mutableStateOf("") }

        ScreenWrapper {
            Column(Modifier.padding(16.dp)) {
                OutlinedTextField(value = otp, onValueChange = { otp = it }, label = { Text(
                    stringResource(
                    SharedRes.strings.sms_code)
                ) })
                Button(onClick = {
                    viewModel.dispatch(LoginIntent.SubmitPasswordOtp(login, oldPassword, newPassword, otp))
                }, modifier = Modifier.padding(top = 8.dp)) {
                    Text(stringResource(
                        SharedRes.strings.change_password))
                }
            }
        }
    }
}
