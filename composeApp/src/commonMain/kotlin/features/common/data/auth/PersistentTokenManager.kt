package features.common.data.auth

import features.common.domain.auth.TokenManager
import data.storage.SecureStorage

class PersistentTokenManager(private val storage: SecureStorage) : TokenManager {
    private val accessKey = "access_token"
    private val refreshKey = "refresh_token"

    override suspend fun saveTokens(accessToken: String, refreshToken: String) {
        storage.put(accessKey, accessToken)
        storage.put(refreshKey, refreshToken)
    }

    override suspend fun getAccessToken(): String? = storage.get(accessKey)

    override suspend fun getRefreshToken(): String? = storage.get(refreshKey)

    override suspend fun clearTokens() {
        storage.remove(accessKey)
        storage.remove(refreshKey)
    }
}
