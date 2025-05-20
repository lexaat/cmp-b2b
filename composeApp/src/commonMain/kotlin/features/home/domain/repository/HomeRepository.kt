package features.home.domain.repository

import features.home.domain.model.Client

interface HomeRepository {
    suspend fun getClients(): List<Client>
}
