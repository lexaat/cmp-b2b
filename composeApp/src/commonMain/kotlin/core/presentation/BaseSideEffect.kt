package core.presentation

interface BaseSideEffect {
    object NavigateToLogin : BaseSideEffect
    data class ShowError(val message: String) : BaseSideEffect
}
