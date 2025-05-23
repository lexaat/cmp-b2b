package features.home.presentation

sealed class HomeSideEffect {
    data class ShowError(val message: String) : HomeSideEffect()
    data class NavigateToClientDetail(val clientId: String) : HomeSideEffect()
}
