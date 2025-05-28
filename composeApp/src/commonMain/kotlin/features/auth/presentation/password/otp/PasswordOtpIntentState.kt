package features.auth.presentation.password.otp

sealed class PasswordOtpState {
    object WaitingForPasswordOtp : PasswordOtpState()
    object EnterCredentials : PasswordOtpState()
}

sealed class PasswordOtpIntent {
    data class SubmitNewPassword(val username: String, val password: String) : PasswordOtpIntent()
    object ClearState : PasswordOtpIntent()
}