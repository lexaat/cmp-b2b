package features.auth.presentation.login_otp

sealed class OtpIntent {
    data class SubmitOtp(val username: String, val password: String, val otp: String) : OtpIntent()
    object ClearState : OtpIntent()
}