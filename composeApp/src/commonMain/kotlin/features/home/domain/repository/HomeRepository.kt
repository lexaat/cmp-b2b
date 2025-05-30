package features.home.domain.repository

import data.model.ApiResponse
import domain.model.Account
import domain.model.Client

interface HomeRepository {
    suspend fun getClients(forceRefresh: Boolean): ApiResponse<List<Client>>
    suspend fun getClientsFromCache(): List<Client>
    suspend fun getAccountsForClient(clientId: Long): List<Account>
}
