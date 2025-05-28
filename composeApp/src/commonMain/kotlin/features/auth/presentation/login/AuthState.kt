package features.auth.presentation.login

sealed interface AuthState {
    object EnterCredentials : AuthState
    object Loading : AuthState
}