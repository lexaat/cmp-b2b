package features.home.presentation

import domain.model.Client

sealed interface HomeIntent {
    object LoadClients : HomeIntent
    object Retry : HomeIntent
    data class SelectClient(val id: String) : HomeIntent
}

sealed interface HomeState {
    object Loading : HomeState
    object Refreshing : HomeState
    data class Data(val clients: List<Client>) : HomeState
    data class Error(val message: String) : HomeState
    object Empty : HomeState
}