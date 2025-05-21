package networking

import features.common.domain.auth.TokenManager
import io.ktor.client.HttpClient

import io.ktor.client.engine.darwin.*
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.request.HttpRequestPipeline
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.encodedPath
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

actual class HttpClientFactory actual constructor(
    private val tokenManager: TokenManager
) {
    actual fun create(): HttpClient {
        return HttpClient(Darwin) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                })
            }

            install(Logging) {
                logger = Logger.SIMPLE
                level = LogLevel.INFO // Заголовки + статус
            }

            install(LoggingBodyPlugin) // ← подключаем наш кастомный логгер

            install(DefaultRequest) {
                header(HttpHeaders.ContentType, ContentType.Application.Json)
            }

            // 💥 Перехват всех запросов
            install("AuthHeader") {
                requestPipeline.intercept(HttpRequestPipeline.State) {
                    val url = context.url.encodedPath.lowercase()
                    if (!url.contains("/login") && !url.contains("/refresh")) {
                        val token = tokenManager.getAccessToken()
                        if (!token.isNullOrBlank()) {
                            context.headers.append(HttpHeaders.Authorization, "Bearer $token")
                        }
                    }
                    proceed()
                }
            }
        }
    }
}