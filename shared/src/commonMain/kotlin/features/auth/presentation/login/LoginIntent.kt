package features.auth.presentation.login

sealed interface LoginIntent {
    object ScreenEntered : LoginIntent
    data class LoginInputChanged(val value: String) : LoginIntent
    data class PasswordInputChanged(val value: String) : LoginIntent
    object LoginButtonClicked : LoginIntent
    object TogglePasswordVisibility : LoginIntent
}