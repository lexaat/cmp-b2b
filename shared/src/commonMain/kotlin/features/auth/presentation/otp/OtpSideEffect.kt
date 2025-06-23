package features.auth.presentation.otp

import core.presentation.BaseSideEffect

sealed interface OtpSideEffect : BaseSideEffect {
    object NavigateToMain : OtpSideEffect
    object NavigateToPasswordChange : OtpSideEffect

    object SessionExpired : OtpSideEffect, BaseSideEffect
    object NavigateBack   : OtpSideEffect, BaseSideEffect
    data class ShowError(val message: String) : OtpSideEffect, BaseSideEffect
}