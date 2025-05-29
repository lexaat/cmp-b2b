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
import features.home.domain.model.Account
import home.HomeQueries
import kotlinx.datetime.Clock

class HomeRepositoryImpl(
    private val httpClient: HttpClient,
    private val config: AppConfig,
    private val homeQueries: HomeQueries,
    private val clock: Clock = Clock.System
) : HomeRepository {

    private val cacheDurationMillis = 5 * 60 * 1000L

    override suspend fun getClients(forceRefresh: Boolean): ApiResponse<List<Client>> {
        val lastUpdated = homeQueries.selectClientLastUpdated().executeAsOneOrNull()?.maxUpdated

        val currentTime = clock.now().toEpochMilliseconds()

        val cachedClients = homeQueries.selectAllClients().executeAsList()

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

                clients.forEach { client ->
                    homeQueries.insertClient(
                        inn = client.inn,
                        name = client.name,
                        address = client.address,
                        last_updated = currentTime
                    )

                    homeQueries.deleteAccountsByClientInn(client.inn)

                    client.accounts.forEach { account ->
                        homeQueries.insertAccount(
                            account = account.account,
                            client_inn = client.inn,
                            balance_in = account.balanceIn,
                            balance_out = account.balanceOut
                        )
                    }
                }

                ApiResponse(result = clients, error = response.error, id = response.id)
            } ?: ApiResponse(result = emptyList(), error = response.error, id = response.id)
        } else {
            ApiResponse(
                result = cachedClients.map { dbClient ->
                    val accounts = homeQueries.selectAccountsByClientId(dbClient.inn).executeAsList().map { dbAccount ->
                        Account(
                            account = dbAccount.account,
                            balanceIn = dbAccount.balance_in,
                            balanceOut = dbAccount.balance_out
                        )
                    }
                    Client(
                        name = dbClient.name,
                        inn = dbClient.inn,
                        address = dbClient.address,
                        accounts = accounts
                    )
                },
                error = null,
                id = null
            )
        }
    }

    override suspend fun getClientsFromCache(): List<Client> {
        return homeQueries.selectAllClients().executeAsList().map { dbClient ->
            val accounts = homeQueries.selectAccountsByClientId(dbClient.inn).executeAsList().map { dbAccount ->
                Account(
                    account = dbAccount.account,
                    balanceIn = dbAccount.balance_in,
                    balanceOut = dbAccount.balance_out
                )
            }
            Client(
                name = dbClient.name,
                inn = dbClient.inn,
                address = dbClient.address,
                accounts = accounts
            )
        }
    }

    override suspend fun getAccountsForClient(clientId: Long): List<Account> {
        return homeQueries.selectAccountsByClientId("clientId").executeAsList().map { dbAccount ->
            Account(
                account = dbAccount.account,
                balanceIn = dbAccount.balance_in,
                balanceOut = dbAccount.balance_out
            )
        }
    }
}
