package features.common.data.auth

import features.common.domain.auth.TokenManager

class InMemoryTokenManager : TokenManager {
    private var accessToken: String? = null
    private var refreshToken: String? = null

    override suspend fun saveTokens(accessToken: String, refreshToken: String) {
        this.accessToken = accessToken
        this.refreshToken = refreshToken
    }

    override suspend fun getAccessToken() = accessToken
    override suspend fun getRefreshToken() = refreshToken

    override suspend fun clearTokens() {
        accessToken = null
        refreshToken = null
    }
}
