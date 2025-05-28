package features.auth.presentation.login

sealed interface AuthIntent {
    data class SubmitCredentials(val username: String, val password: String) : AuthIntent
}