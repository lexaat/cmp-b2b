package app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cafe.adriel.voyager.core.screen.Screen
import features.auth.domain.AuthRepository
import features.auth.presentation.login.LoginScreen
import features.common.domain.auth.TokenManager
import features.main.presentation.MainScreen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlinx.datetime.Clock

private var json: Json = Json { ignoreUnknownKeys = true }

class AppViewModel(
    private val tokenManager: TokenManager,
    private val authRepository: AuthRepository // нужен метод ping или аналог
) : ViewModel() {

    private val _startScreen = MutableStateFlow<Screen?>(null)
    val startScreen: StateFlow<Screen?> = _startScreen

    init {
        viewModelScope.launch {
            val accessToken = tokenManager.getAccessToken()
            val refreshToken = tokenManager.getRefreshToken()
            if (refreshToken.isNullOrBlank()) {
                _startScreen.value = LoginScreen
                return@launch
            }

            // 1. локальная проверка
            if (accessToken != null && !isTokenExpired(accessToken)) {
                _startScreen.value = MainScreen

                // 2. фоновая проверка на сервере
                launch {
                    val stillValid = checkTokenOnServer(refreshToken)
                    if (!stillValid) {
                        tokenManager.clearTokens()
                        _startScreen.value = LoginScreen
                    }
                }
            } else {
                _startScreen.value = LoginScreen
            }
        }
    }

    @OptIn(ExperimentalEncodingApi::class)
    private fun decodeJwtPayload(payloadBase64Url: String): String {
        var base64 = payloadBase64Url.replace('-', '+').replace('_', '/')
        while (base64.length % 4 != 0) {
            base64 += "="
        }
        return Base64.Default.decode(base64).decodeToString()
    }

    private fun isTokenExpired(token: String): Boolean {
        return try {
            val parts = token.split(".")
            if (parts.size != 3) return true

            val payloadJson = decodeJwtPayload(parts[1])
            val payload = json.decodeFromString<JwtPayload>(payloadJson)

            val currentTime = Clock.System.now().epochSeconds
            payload.exp < currentTime
        } catch (e: Exception) {
            true
        }
    }


    private suspend fun checkTokenOnServer(refreshToken: String): Boolean {
        return try {
            authRepository.refreshToken(refreshToken = refreshToken).error == null
        } catch (e: Exception) {
            false
        }
    }

    @Serializable
    private data class JwtPayload(val exp: Long)
}
