package features.client.data

import features.client.domain.model.ClientDetail
import features.client.domain.repository.ClientRepository
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

class ClientRepositoryImpl(
    private val httpClient: HttpClient
) : ClientRepository {

    override suspend fun getClientDetail(id: String): ClientDetail {
        // TODO: заменить на реальный endpoint
        return httpClient.get("/client/detail") {
            parameter("id", id)
        }.body()
    }
}
