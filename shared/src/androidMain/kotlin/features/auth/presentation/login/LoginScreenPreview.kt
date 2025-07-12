package features.auth.presentation.login

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Preview(
    showBackground = true,
    device = "spec:width=411dp,height=891dp"
)
@Composable
fun LoginScreenContentPreview() {
    val state = LoginState(
        loginInput = "demo",
        passwordInput = "123456",
        isLoading = false,
        isPasswordVisible = false
    )
    LoginScreenContent(
        state = state,
        canUseBiometrics = false,
        onLoginChanged = {},
        onPasswordChanged = {},
        onLoginClicked = {},
        onBiometricLogin = {},
        onTogglePasswordVisibility = {}
    )
}