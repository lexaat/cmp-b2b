package features.home.presentation

import features.home.domain.model.Client

sealed class HomeState {
    object Loading : HomeState()
    data class Data(val clients: List<Client>) : HomeState()
    data class Error(val message: String) : HomeState()
}
