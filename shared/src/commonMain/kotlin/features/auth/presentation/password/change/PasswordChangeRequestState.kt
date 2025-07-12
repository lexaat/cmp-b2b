package features.auth.presentation.password.change

import dev.icerock.moko.resources.StringResource

data class PasswordChangeRequestState(
    val newPassword: String = "",
    val passwordError: StringResource? = null,
    val isLoading: Boolean = false
)
