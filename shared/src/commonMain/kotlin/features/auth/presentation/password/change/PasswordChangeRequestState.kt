package features.auth.presentation.password.change

import dev.icerock.moko.resources.StringResource

data class PasswordChangeRequestState(
    val currentPassword: String = "",
    val newPassword: String = "",
    val confirmPassword: String = "",
    val passwordError: StringResource? = null,
    val isLoading: Boolean = false,
    val showCurrentPassword: Boolean = false,
    val showNewPassword: Boolean = false,
    val showConfirmPassword: Boolean = false,

    )