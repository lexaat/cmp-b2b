package features.client.presentation

sealed class ClientDetailIntent {
    data class Load(val clientId: String) : ClientDetailIntent()
    object Retry : ClientDetailIntent()
}
