package features.profile.presentation

sealed interface ProfileSideEffect {
    data class ShowError(val message: String) : ProfileSideEffect
    object NavigateToLogin : ProfileSideEffect
    object ShowLogoutConfirmation : ProfileSideEffect
}
