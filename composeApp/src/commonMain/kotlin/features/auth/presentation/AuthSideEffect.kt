package features.auth.presentation

sealed class AuthSideEffect {
    data class ShowSnackbar(val message: String) : AuthSideEffect()
    object NavigateToHome : AuthSideEffect()
}