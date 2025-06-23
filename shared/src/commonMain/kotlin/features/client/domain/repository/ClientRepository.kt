package features.client.domain.repository

import features.client.domain.model.ClientDetail

interface ClientRepository {
    suspend fun getClientDetail(id: String): ClientDetail
}
