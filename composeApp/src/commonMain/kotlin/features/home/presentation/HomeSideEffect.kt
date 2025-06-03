package features.home.presentation

import core.presentation.BaseSideEffect

sealed interface HomeSideEffect : BaseSideEffect {
    data class NavigateToClientDetail(val clientId: String) : HomeSideEffect

    data class ShowError(val message: String) : HomeSideEffect, BaseSideEffect
    object SessionExpired : HomeSideEffect, BaseSideEffect
    object NavigateBack   : HomeSideEffect, BaseSideEffect
}
