package features.auth.presentation.password.change

import core.presentation.BaseSideEffect

sealed class PasswordChangeSideEffect : BaseSideEffect {
    data class ShowError(val message: String) : PasswordChangeSideEffect()
    object NavigateToOtp : PasswordChangeSideEffect()
}