package features.auth.presentation.password.change

import core.presentation.BaseSideEffect

sealed interface ChangePasswordSideEffect : BaseSideEffect {
    object NavigateToOtp : ChangePasswordSideEffect

    data class ShowError(val message: String) : ChangePasswordSideEffect, BaseSideEffect
    object SessionExpired : ChangePasswordSideEffect, BaseSideEffect
    object NavigateBack   : ChangePasswordSideEffect, BaseSideEffect
}