package features.auth.presentation

sealed class AuthState {
    object EnterCredentials : AuthState()
    object WaitingForOtp : AuthState()
    object Authorized : AuthState()
    object RequirePasswordChange : AuthState()
    object WaitingForPasswordOtp : AuthState()
    object PasswordChanged : AuthState()
}

sealed class AuthIntent {
    data class SubmitCredentials(val username: String, val password: String) : AuthIntent()
    data class SubmitOtp(val username: String, val password: String, val otp: String) : AuthIntent()
    data class SubmitNewPassword(val newPassword: String) : AuthIntent()
    data class SubmitPasswordOtp(val newPassword: String, val otp: String) : AuthIntent()
}