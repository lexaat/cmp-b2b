package features.auth.presentation.password.change

sealed class PasswordChangeState {
    object WaitingForPasswordOtp : PasswordChangeState()
    object EnterCredentials : PasswordChangeState()
}

sealed class PasswordChangeIntent {
    data class SubmitNewPassword(val username: String, val password: String) : PasswordChangeIntent()
    object ClearState : PasswordChangeIntent()
}