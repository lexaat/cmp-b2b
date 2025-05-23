package features.auth.presentation.login

import core.presentation.BaseSideEffect

sealed class AuthSideEffect : BaseSideEffect {
    data class ShowError(val message: String) : AuthSideEffect()
    object NavigateToMain : AuthSideEffect()
    object NavigateToOtp : AuthSideEffect()
    object NavigateToPasswordChange : AuthSideEffect()
}