package features.client.presentation

import core.presentation.BaseSideEffect

sealed class ClientSideEffect : BaseSideEffect {
    data class ShowClientError(val message: String) : ClientSideEffect()
}