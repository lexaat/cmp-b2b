package features.home.domain.repository

import data.model.ApiResponse
import features.home.domain.model.Client

interface HomeRepository {
    suspend fun getClients(): ApiResponse<List<Client>>
}
