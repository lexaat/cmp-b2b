package features.auth.presentation.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import features.auth.presentation.LoginFormContent

@Preview(showBackground = true)
@Composable
fun LoginFormContentPreview() {
    var login by remember { mutableStateOf("demo") }
    var password by remember { mutableStateOf("123456") }

    LoginFormContent(
        login = login,
        password = password,
        onLoginChange = { login = it },
        onPasswordChange = { password = it },
        isLoading = true,
        canUseBiometrics = true,
        onSubmit = {},
        onBiometricLogin = {},
        loginLabel = "Логин",
        passwordLabel = "Пароль",
        loginButtonText = "Войти"
    )
}

@Preview(showBackground = true)
@Composable
fun LoginFormContentPreview2() {
    var login by remember { mutableStateOf("demo") }
    var password by remember { mutableStateOf("123456") }

    LoginFormContent(
        login = login,
        password = password,
        onLoginChange = { login = it },
        onPasswordChange = { password = it },
        isLoading = false,
        canUseBiometrics = true,
        onSubmit = {},
        onBiometricLogin = {},
        loginLabel = "Логин",
        passwordLabel = "Пароль",
        loginButtonText = "Войти"
    )
}