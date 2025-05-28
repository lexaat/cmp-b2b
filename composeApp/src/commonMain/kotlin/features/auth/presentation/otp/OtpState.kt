package features.auth.presentation.otp

sealed interface OtpState {
    object EnterOtp : OtpState
    object Loading : OtpState
}