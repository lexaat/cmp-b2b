package features.home.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ClientsResponseDto(
    val clients: List<ClientDto> = emptyList()
)

