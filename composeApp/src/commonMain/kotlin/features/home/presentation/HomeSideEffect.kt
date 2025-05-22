package features.home.presentation

import core.presentation.BaseSideEffect

sealed class HomeSideEffect : BaseSideEffect {
    data class ShowHomeError(val message: String) : HomeSideEffect()
}