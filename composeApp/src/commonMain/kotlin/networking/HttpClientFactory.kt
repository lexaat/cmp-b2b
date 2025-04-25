package networking

import io.ktor.client.*

expect object HttpClientFactory {
    fun create(): HttpClient
}