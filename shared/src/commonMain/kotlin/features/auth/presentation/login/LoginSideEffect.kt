package features.auth.presentation.login

import core.presentation.BaseSideEffect

sealed interface LoginSideEffect : BaseSideEffect {
    object NavigateToMain : LoginSideEffect
    data class NavigateToOtp(val phone: String) : LoginSideEffect

    object SessionExpired : LoginSideEffect, BaseSideEffect
    data class ShowError(val message: String) : LoginSideEffect, BaseSideEffect
    object NavigateBack   : LoginSideEffect, BaseSideEffect
}