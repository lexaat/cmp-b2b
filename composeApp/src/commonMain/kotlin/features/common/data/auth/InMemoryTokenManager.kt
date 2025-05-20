package features.common.data.auth

import features.common.domain.auth.TokenManager

class InMemoryTokenManager : TokenManager {
    private var accessToken: String? = null
    private var refreshToken: String? = null

    override fun saveTokens(accessToken: String, refreshToken: String) {
        this.accessToken = accessToken
        this.refreshToken = refreshToken
    }

    override fun getAccessToken() = accessToken
    override fun getRefreshToken() = refreshToken

    override fun clear() {
        accessToken = null
        refreshToken = null
    }
}
