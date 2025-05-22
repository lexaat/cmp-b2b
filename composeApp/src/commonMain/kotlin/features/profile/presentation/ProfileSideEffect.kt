package features.profile.presentation

sealed interface ProfileSideEffect {
    object NavigateToLogin : ProfileSideEffect
    object ShowLogoutConfirmation : ProfileSideEffect
}