package features.home.presentation

sealed class HomeIntent {
    object LoadClients : HomeIntent()
    object Retry : HomeIntent()
    data class SelectClient(val id: String) : HomeIntent()
}
