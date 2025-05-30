package features.home.domain.usecase

import data.model.ApiResponse
import domain.model.Client
import features.home.domain.repository.HomeRepository

class GetClientsUseCase(private val repository: HomeRepository) {
    suspend operator fun invoke(forceRefresh: Boolean = false): ApiResponse<List<Client>> =
        repository.getClients(forceRefresh)
}