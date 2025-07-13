package features.auth.presentation.password.change

sealed interface PasswordChangeRequestIntent {
    data class CurrentPasswordChanged(val value: String) : PasswordChangeRequestIntent
    data class NewPasswordChanged(val value: String) : PasswordChangeRequestIntent
    data class ConfirmPasswordChanged(val value: String) : PasswordChangeRequestIntent
    data object SubmitClicked : PasswordChangeRequestIntent

    data object ToggleShowCurrentPassword : PasswordChangeRequestIntent
    data object ToggleShowNewPassword : PasswordChangeRequestIntent
    data object ToggleShowConfirmPassword : PasswordChangeRequestIntent

}