package features.auth.presentation.password.otp

import core.presentation.BaseSideEffect

sealed class PasswordOtpSideEffect : BaseSideEffect {
    data class ShowError(val message: String) : PasswordOtpSideEffect()
    object NavigateToOtp : PasswordOtpSideEffect()
}