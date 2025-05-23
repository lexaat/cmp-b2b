package features.auth.presentation

sealed class AuthSideEffect {
    data class ShowError(val message: String) : AuthSideEffect()
    object NavigateToMain : AuthSideEffect()
}