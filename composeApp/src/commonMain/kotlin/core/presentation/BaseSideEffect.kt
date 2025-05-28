package core.presentation

import features.auth.presentation.login.AuthSideEffect

interface BaseSideEffect {
    object NavigateToLogin : BaseSideEffect
    data class ShowError(val message: String) : BaseSideEffect
    object NavigateToMain : BaseSideEffect
    object NavigateToOtp : BaseSideEffect
    object NavigateToPasswordChange : BaseSideEffect
}
