package features.home.data

import b2b.database.AccountQueries
import b2b.database.ClientQueries
import b2b.database.DataSyncMetaQueries
import config.AppConfig
import data.mapper.toDomain
import data.mapper.toEntity
import data.model.ApiResponse
import domain.model.Account
import domain.model.Client
import features.home.data.dto.ClientsResponseDto
import features.home.domain.repository.HomeRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.datetime.Clock

class HomeRepositoryImpl(
    private val httpClient: HttpClient,
    private val config: AppConfig,
    private val clientQueries: ClientQueries,
    private val accountQueries: AccountQueries,
    private val metaQueries: DataSyncMetaQueries,
    private val clock: Clock = Clock.System
) : HomeRepository {

    private val cacheDurationMillis = 5 * 60 * 1000L

    override suspend fun getClients(forceRefresh: Boolean): ApiResponse<List<Client>> {
        val lastUpdated = metaQueries
            .selectLastUpdatedForTable("ClientEntity")
            .executeAsOneOrNull()

        val currentTime = clock.now().toEpochMilliseconds()

        val cachedClients = clientQueries.selectAllClients().executeAsList()

        val shouldRefresh = forceRefresh ||
                cachedClients.isEmpty() ||
                lastUpdated == null ||
                (currentTime - lastUpdated) > cacheDurationMillis

        return if (shouldRefresh) {
            val response = httpClient.post("${config.baseUrl}/GetClients1C") {
                setBody("{}")
            }.body<ApiResponse<ClientsResponseDto>>()

            response.result?.clients?.let { clientsDto ->
                val clients = clientsDto.map { it.toDomain() }

                // очистка старых данных
                clientQueries.deleteAllClients()
                clientQueries.deleteAllAccounts()

                clients.forEach { client ->
                    clientQueries.insertClient(client.toEntity())
                    client.accounts.forEach { account ->
                        accountQueries.insertAccount(account.toEntity(client.id))
                    }
                }

                // сохраняем новое время обновления
                metaQueries.updateLastUpdatedForTable("ClientEntity", currentTime)

                ApiResponse(result = clients, error = response.error, id = response.id)
            } ?: ApiResponse(result = emptyList(), error = response.error, id = response.id)
        } else {
            ApiResponse(
                result = cachedClients.map { dbClient ->
                    val accounts = clientQueries
                        .selectAccountsByClientId(dbClient.id)
                        .executeAsList()
                        .map { it.toDomain() }

                    dbClient.toDomain(accounts)
                },
                error = null,
                id = null
            )
        }
    }


    override suspend fun getClientsFromCache(): List<Client> {
        return clientQueries.selectAllClients()
            .executeAsList()
            .map { dbClient ->
                val accounts = clientQueries
                    .selectAccountsByClientId(dbClient.id) // важно: использовать `id`, а не `inn`
                    .executeAsList()
                    .map { it.toDomain() }

                dbClient.toDomain(accounts)
            }
    }

    override suspend fun getAccountsForClient(clientId: Long): List<Account> {
        return clientQueries.selectAccountsByClientId(clientId)
            .executeAsList()
            .map { it.toDomain() }
    }
}
