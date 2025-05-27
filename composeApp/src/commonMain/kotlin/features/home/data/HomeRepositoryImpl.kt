package features.home.data

import config.AppConfig
import features.home.data.dto.ClientsResponseDto
import features.home.data.mapper.toDomain
import features.home.domain.model.Client
import features.home.domain.repository.HomeRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import data.model.ApiResponse

class HomeRepositoryImpl(private val httpClient: HttpClient,
                         private val config: AppConfig
) : HomeRepository {

    override suspend fun getClients(): ApiResponse<List<Client>> {
        val response = httpClient.post("${config.baseUrl}/GetClients1C") {
            setBody("{}")
        }.body<ApiResponse<ClientsResponseDto>>()

        // пробрасывать ошибку наружу больше не надо — это теперь задача ErrorHandler
        val mappedClients = response.result?.clients?.map { it.toDomain() } ?: emptyList()

        return ApiResponse(
            error = response.error,
            result = mappedClients,
            id = response.id
        )
    }
}
