package features.home.data.dto

import data.model.dto.ClientDto
import kotlinx.serialization.Serializable

@Serializable
data class ClientsResponseDto(
    val clients: List<ClientDto> = emptyList()
)

