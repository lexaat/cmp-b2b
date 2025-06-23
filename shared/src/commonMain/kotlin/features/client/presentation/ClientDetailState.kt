package features.client.presentation

import features.client.domain.model.ClientDetail

sealed class ClientDetailState {
    object Loading : ClientDetailState()
    data class Data(val client: ClientDetail) : ClientDetailState()
    data class Error(val message: String) : ClientDetailState()
}
