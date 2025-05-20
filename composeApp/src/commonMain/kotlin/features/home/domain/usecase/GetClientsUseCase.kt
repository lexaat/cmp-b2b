package features.home.domain.usecase

import features.home.domain.model.Client
import features.home.domain.repository.HomeRepository

class GetClientsUseCase(private val repository: HomeRepository) {
    suspend operator fun invoke(): List<Client> = repository.getClients()
}
