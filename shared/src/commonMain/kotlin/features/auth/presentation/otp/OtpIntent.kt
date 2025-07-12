package features.auth.presentation.otp

import features.auth.presentation.login.LoginIntent

sealed interface OtpIntent {
    object ScreenEntered : OtpIntent
    data class OtpInputChanged(val value: String) : OtpIntent
    object ConfirmButtonClicked : OtpIntent
}

