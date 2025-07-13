package features.auth.presentation.password.confirm

import core.presentation.BaseSideEffect

sealed interface PasswordChangeOtpSideEffect : BaseSideEffect {
    object NavigateBack : PasswordChangeOtpSideEffect
    object SessionExpired : PasswordChangeOtpSideEffect
    object PasswordChanged : PasswordChangeOtpSideEffect
    data class ShowError(val message: String) : PasswordChangeOtpSideEffect
}