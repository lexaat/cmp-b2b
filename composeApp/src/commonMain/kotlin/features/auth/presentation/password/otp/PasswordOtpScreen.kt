package features.auth.presentation.password.otp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import dev.icerock.moko.resources.compose.stringResource
import org.koin.compose.koinInject
import ui.components.ScreenWrapper
import uz.hb.b2b.SharedRes

data class PasswordOtpScreen(val login: String, val oldPassword: String, val newPassword: String) : Screen {
    @Composable
    override fun Content() {
        val viewModel = koinInject<PasswordOtpViewModel>()
        var otp by remember { mutableStateOf("") }

        ScreenWrapper {
            Column(Modifier.padding(16.dp)) {
                OutlinedTextField(value = otp, onValueChange = { otp = it }, label = { Text(
                    stringResource(
                    SharedRes.strings.sms_code)
                ) })
                Button(onClick = {
                    viewModel.dispatch(PasswordOtpIntent.SubmitNewPassword(username = "", password = ""))
                }, modifier = Modifier.padding(top = 8.dp)) {
                    Text(stringResource(
                        SharedRes.strings.change_password))
                }
            }
        }
    }
}
