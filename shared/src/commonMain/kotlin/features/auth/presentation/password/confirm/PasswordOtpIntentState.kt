package features.auth.presentation.password.confirm

sealed class PasswordOtpState {
    object WaitingForPasswordOtp : PasswordOtpState()
    object EnterCredentials : PasswordOtpState()
}

sealed class ChangePasswordOtpIntent {
    data class SubmitNewPassword(
        val username: String,
        val password: String,
        val newPassword: String,
        val otp: String) : ChangePasswordOtpIntent()
    object ClearState : ChangePasswordOtpIntent()
}