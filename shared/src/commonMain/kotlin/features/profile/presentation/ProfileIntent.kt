package features.profile.presentation

sealed class ProfileIntent {
    object LoadClients : ProfileIntent()
    object Retry : ProfileIntent()
    data class SelectClient(val id: String) : ProfileIntent()
}
