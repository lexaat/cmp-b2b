package features.home.presentation

import core.presentation.BaseSideEffect

sealed interface HomeSideEffect : BaseSideEffect {
    data class NavigateToClientDetail(val clientId: String) : HomeSideEffect
}
