package features.auth.presentation.login

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Preview(showBackground = false,
    device = "id:Nexus 5",
    uiMode = Configuration.UI_MODE_NIGHT_NO, showSystemUi = true
)
@Composable
fun LoginFormContentPreview() {
    var login by remember { mutableStateOf("demo") }
    var password by remember { mutableStateOf("123456") }

    LoginFormContent(
        login = login,
        password = password,
        isLoading = true,
        canUseBiometrics = true,
        onBiometricLogin = {},
        loginLabel = "Логин",
        passwordLabel = "Пароль",
        loginButtonText = "Войти",
        onLoginChanged = {},
        onPasswordChanged = {},
        onLoginClicked = {},
        modifier = Modifier,
        loginError = null,
        passwordError = null,
        generalError = null,
        isPasswordVisible = false,
        onTogglePasswordVisibility = {}
    )
}