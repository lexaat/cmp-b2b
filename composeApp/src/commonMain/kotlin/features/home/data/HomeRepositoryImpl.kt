package features.home.data

import config.AppConfig
import features.home.data.dto.ClientsResponseDto
import features.home.data.mapper.toDomain
import features.home.domain.model.Client
import features.home.domain.repository.HomeRepository
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import model.ApiResponse

class HomeRepositoryImpl(private val httpClient: HttpClient,
                         private val config: AppConfig
) : HomeRepository {
    override suspend fun getClients(): List<Client> {
        val response = httpClient.post("${config.baseUrl}/GetClients1C") {
            contentType(ContentType.Application.Json)
            setBody("{}")
        }.body<ApiResponse<ClientsResponseDto>>()

        if (response.error?.code != null && response.error.code != 0) {
            throw Exception("API error: ${response.error.message} (code ${response.error.code})")
        }

        return response.result?.clients?.map { it.toDomain() } ?: emptyList()
    }
}
