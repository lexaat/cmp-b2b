package features.auth.presentation.login_otp

import core.presentation.BaseSideEffect

sealed class OtpSideEffect : BaseSideEffect {
    data class ShowError(val message: String) : OtpSideEffect()
    object NavigateToMain : OtpSideEffect()
}