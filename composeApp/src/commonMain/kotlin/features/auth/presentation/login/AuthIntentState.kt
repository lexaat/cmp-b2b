package features.auth.presentation.login

sealed interface AuthIntent {
    data class SubmitCredentials(val username: String, val password: String) : AuthIntent
}

sealed interface AuthState {
    object EnterCredentials : AuthState
    object Loading : AuthState
}