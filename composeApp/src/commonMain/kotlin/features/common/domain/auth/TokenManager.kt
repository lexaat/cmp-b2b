package features.common.domain.auth

interface TokenManager {
    suspend fun saveTokens(accessToken: String, refreshToken: String)
    suspend fun getAccessToken(): String?
    suspend fun getRefreshToken(): String?
    suspend fun clearAccessToken()
    suspend fun clearTokens()
}
