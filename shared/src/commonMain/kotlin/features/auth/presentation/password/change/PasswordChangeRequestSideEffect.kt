package features.auth.presentation.password.change

import core.presentation.BaseSideEffect

sealed interface PasswordChangeRequestSideEffect : BaseSideEffect {
    data class NavigateToOtp(
        val login: String,
        val newPassword: String,
        val oldPassword: String
    ) : PasswordChangeRequestSideEffect
    data class ShowError(val message: String) : PasswordChangeRequestSideEffect

}
