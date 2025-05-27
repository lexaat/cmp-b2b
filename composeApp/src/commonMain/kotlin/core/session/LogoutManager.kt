package core.session

import features.common.domain.auth.TokenManager

class LogoutManager(
    private val tokenManager: TokenManager
) {
    suspend fun clearSession() {
        tokenManager.clearTokens()
        // Можно добавить logout-вызов на сервер или очистку других данных
    }
}
