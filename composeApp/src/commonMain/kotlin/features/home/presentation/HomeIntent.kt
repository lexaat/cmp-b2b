package features.home.presentation

sealed interface HomeIntent {
    object LoadClients : HomeIntent
    object Retry : HomeIntent
    data class SelectClient(val id: String) : HomeIntent
}
