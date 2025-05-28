package features.auth.presentation.login

import core.presentation.BaseSideEffect

sealed interface AuthSideEffect : BaseSideEffect {
    object NavigateToMain : AuthSideEffect
    object NavigateToOtp : AuthSideEffect
    object NavigateToPasswordChange : AuthSideEffect

    object SessionExpired : AuthSideEffect, BaseSideEffect
    data class ShowError(val message: String) : AuthSideEffect, BaseSideEffect
}