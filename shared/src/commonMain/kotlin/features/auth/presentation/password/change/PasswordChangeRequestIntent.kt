package features.auth.presentation.password.change

sealed interface PasswordChangeRequestIntent {
    data class PasswordChanged(val value: String) : PasswordChangeRequestIntent
    object SubmitClicked : PasswordChangeRequestIntent
}