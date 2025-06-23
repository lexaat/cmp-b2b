package features.auth.presentation.password.change

sealed class ChangePasswordState {
    object WaitingForPasswordOtp : ChangePasswordState()
    object EnterCredentials : ChangePasswordState()
}

sealed class ChangePasswordIntent {
    data class SubmitNewPassword(
        val username: String,
        val password: String,
        val newPassword: String) : ChangePasswordIntent()
    object ClearState : ChangePasswordIntent()
}