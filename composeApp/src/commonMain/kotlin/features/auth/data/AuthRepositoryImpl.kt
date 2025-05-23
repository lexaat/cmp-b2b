package features.auth.data

import config.AppConfig
import features.auth.domain.AuthRepository
import features.auth.model.AuthResponse
import features.auth.model.ChangePasswordRequest
import features.auth.model.LoginRequest
import features.auth.model.RefreshTokenRequest
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.*
import io.ktor.client.statement.bodyAsText
import io.ktor.http.*
import io.ktor.util.encodeBase64

class AuthRepositoryImpl(
    private val httpClient: HttpClient,
    private val config: AppConfig
) : AuthRepository {

    init {
        println("AuthRepositoryImpl initialized with ${config.baseUrl}")
    }

    override suspend fun login(username: String, password: String): AuthResponse {
        val credentials = "$username:$password"
        val encoded = credentials.encodeToByteArray().encodeBase64()

        return try {
            val response = httpClient.post("${config.baseUrl}/login") {
                contentType(ContentType.Application.Json)
                header(HttpHeaders.Authorization, "Basic $encoded")
                setBody(LoginRequest(otp = ""))
            }

            val responseBody = response.bodyAsText()
            println("Raw Response Body: $responseBody")

            response.body()
        } catch (e: ClientRequestException) {
            // Ошибка 4xx
            println("Client error: ${e.response.status}")
            throw e
        } catch (e: ServerResponseException) {
            // Ошибка 5xx
            println("Server error: ${e.response.status}")
            throw e
        } catch (e: Exception) {
            // Другая ошибка (например, network error)
            println("Unknown error: ${e.message}")
            throw e
        }
    }

    override suspend fun verifyOtp(username: String, password: String, otp: String): AuthResponse {
        val credentials = "$username:$password"
        val encoded = credentials.encodeToByteArray().encodeBase64()

        return try {
            val response = httpClient.post("${config.baseUrl}/login") {
                contentType(ContentType.Application.Json)
                header(HttpHeaders.Authorization, "Basic $encoded")
                setBody(LoginRequest(otp = otp))
            }

            val responseBody = response.bodyAsText()
            println("Raw Response Body: $responseBody")

            response.body()
        } catch (e: ClientRequestException) {
            // Ошибка 4xx
            println("Client error: ${e.response.status}")
            throw e
        } catch (e: ServerResponseException) {
            // Ошибка 5xx
            println("Server error: ${e.response.status}")
            throw e
        } catch (e: Exception) {
            // Другая ошибка (например, network error)
            println("Unknown error: ${e.message}")
            throw e
        }
    }

    override suspend fun changePassword(username: String, password: String, newPassword: String, otp: String): AuthResponse {
        val credentials = "$username:$password"
        val encoded = credentials.encodeToByteArray().encodeBase64()
        return httpClient.post("${config.baseUrl}/ChangePassword") {
            contentType(ContentType.Application.Json)
            header("Authorization", "Basic $encoded")
            setBody(ChangePasswordRequest(newPassword, otp))
        }.body()
    }

    override suspend fun refreshToken(refreshToken: String): AuthResponse {
        return try {
            val response = httpClient.post("${config.baseUrl}/RefreshToken") {
                setBody(RefreshTokenRequest(refresh_token = refreshToken))
            }
            response.body()
        } catch (e: Exception) {
            println("Unknown error: ${e.message}")
            throw e
        }
    }
}



