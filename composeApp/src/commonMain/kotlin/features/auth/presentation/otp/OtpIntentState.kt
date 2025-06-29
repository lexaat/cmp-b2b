package features.auth.presentation.otp

sealed interface OtpIntent {
    data class SubmitOtp(val username: String, val password: String, val otp: String) : OtpIntent
}

sealed interface OtpState {
    object EnterOtp : OtpState
    object Loading : OtpState
}