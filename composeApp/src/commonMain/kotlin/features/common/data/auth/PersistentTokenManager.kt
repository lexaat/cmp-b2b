package features.common.data.auth

import com.russhwolf.settings.Settings
import com.russhwolf.settings.get
import com.russhwolf.settings.set
import features.common.domain.auth.TokenManager

class PersistentTokenManager(private val settings: Settings) : TokenManager {
    private val accessKey = "access_token"
    private val refreshKey = "refresh_token"

    override fun saveTokens(accessToken: String, refreshToken: String) {
        settings[accessKey] = accessToken
        settings[refreshKey] = refreshToken
    }

    override fun getAccessToken(): String? = settings[accessKey]
    override fun getRefreshToken(): String? = settings[refreshKey]

    override fun clear() {
        settings.remove(accessKey)
        settings.remove(refreshKey)
    }
}
