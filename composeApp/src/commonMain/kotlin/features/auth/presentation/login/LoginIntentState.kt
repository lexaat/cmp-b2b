package features.auth.presentation.login

sealed class LoginState {
    object EnterCredentials : LoginState()
    object WaitingForOtp : LoginState()
    object RequirePasswordChange : LoginState()
    object WaitingForPasswordOtp : LoginState()
    object PasswordChanged : LoginState()
}

sealed class LoginIntent {
    data class SubmitCredentials(val username: String, val password: String) : LoginIntent()
    data class SubmitOtp(val username: String, val password: String, val otp: String) : LoginIntent()
    data class SubmitNewPassword(val username: String, val password: String, val newPassword: String) : LoginIntent()
    data class SubmitPasswordOtp(val username: String, val password: String, val newPassword: String, val otp: String) : LoginIntent()
    object ClearState : LoginIntent()
}