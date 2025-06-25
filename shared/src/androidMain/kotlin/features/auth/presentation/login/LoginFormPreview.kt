package features.auth.presentation.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import features.auth.presentation.LoginFormContent

@Preview(showBackground = true, device = "spec:width=1280dp,height=800dp,dpi=240")
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
        onLoginChanged = TODO(),
        onPasswordChanged = TODO(),
        onLoginClicked = TODO(),
        modifier = TODO(),
        loginError = TODO(),
        passwordError = TODO(),
        generalError = TODO()
    )
}

@Preview(showBackground = true)
@Composable
fun LoginFormContentPreview2() {
    LoginFormContent(

        loginLabel = "Логин",
        passwordLabel = "Пароль",
        loginButtonText = "Войти",
        onLoginChanged = TODO(),
        onPasswordChanged = TODO(),
        onLoginClicked = TODO(),
        modifier = TODO(),
        loginError = TODO(),
        passwordError = TODO(),
        generalError = TODO(),
        login = TODO(),
        password = TODO(),
        isLoading = TODO(),
        canUseBiometrics = TODO(),
        onBiometricLogin = TODO()
    )
}