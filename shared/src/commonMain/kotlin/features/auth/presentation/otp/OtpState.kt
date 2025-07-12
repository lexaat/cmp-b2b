package features.auth.presentation.otp

import dev.icerock.moko.resources.StringResource

data class OtpState(
    val otpInput: String = "",
    val otpError: StringResource? = null,
    val isLoading: Boolean = false,
    val generalError: StringResource? = null,
)