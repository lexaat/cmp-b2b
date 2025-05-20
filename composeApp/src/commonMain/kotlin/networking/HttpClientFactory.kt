package networking

import features.common.domain.auth.TokenManager
import io.ktor.client.*

expect class HttpClientFactory(tokenManager: TokenManager) {
    fun create(): HttpClient
}