package features.auth.presentation.password.otp

import core.presentation.BaseSideEffect

sealed interface PasswordOtpSideEffect : BaseSideEffect {
    object NavigateToLogin : PasswordOtpSideEffect

    data class ShowError(val message: String) : PasswordOtpSideEffect, BaseSideEffect
    object SessionExpired : PasswordOtpSideEffect, BaseSideEffect
    object NavigateBack   : PasswordOtpSideEffect, BaseSideEffect
}