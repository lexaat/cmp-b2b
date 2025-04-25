package utils

interface TokenManager {
    fun saveTokens(accessToken: String, refreshToken: String)
    fun getAccessToken(): String?
    fun getRefreshToken(): String?
    fun clear()
}

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