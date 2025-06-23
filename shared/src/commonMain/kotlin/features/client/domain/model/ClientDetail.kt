package features.client.domain.model

data class ClientDetail(
    val id: String,
    val name: String,
    val inn: String,
    val phone: String? = null,
    val email: String? = null
)
