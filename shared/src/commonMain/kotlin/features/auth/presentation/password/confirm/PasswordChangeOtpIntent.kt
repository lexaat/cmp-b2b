package features.auth.presentation.password.confirm

sealed interface PasswordChangeOtpIntent {
    object ScreenEntered : PasswordChangeOtpIntent
    data class OtpInputChanged(val value: String) : PasswordChangeOtpIntent
    object ConfirmButtonClicked : PasswordChangeOtpIntent
}