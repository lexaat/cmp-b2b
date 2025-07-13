package features.auth.presentation.password.confirm

import dev.icerock.moko.resources.StringResource

data class PasswordChangeOtpState(
    val otpInput: String = "",
    val otpError: StringResource? = null,
    val isLoading: Boolean = false,
    val generalError: StringResource? = null,
)