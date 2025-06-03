package app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cafe.adriel.voyager.core.screen.Screen
import core.i18n.LocaleController
import features.auth.domain.AuthRepository
import features.auth.domain.model.RefreshTokenRequest
import features.auth.domain.usecase.LoginUseCase
import features.auth.domain.usecase.RefreshTokenUseCase
import features.auth.presentation.AuthScreen
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
import platform.BiometricAuthenticator
import platform.BiometricResult

private var json: Json = Json { ignoreUnknownKeys = true }

class AppViewModel(
    private val tokenManager: TokenManager,
    private val biometricAuthenticator: BiometricAuthenticator,
    private val refreshTokenUseCase: RefreshTokenUseCase,
) : ViewModel() {

    private val _startScreen = MutableStateFlow<Screen?>(null)
    val startScreen: StateFlow<Screen?> = _startScreen

    init {
        LocaleController.applySavedLocaleOrSystemDefault()

        viewModelScope.launch {
            val accessToken = tokenManager.getAccessToken()
            val refreshToken = tokenManager.getRefreshToken()

            if (refreshToken.isNullOrBlank()) {
                _startScreen.value = AuthScreen
                return@launch
            }

            if (accessToken != null && !isTokenExpired(accessToken)) {
                _startScreen.value = MainScreen

                launch {
                    val stillValid = checkTokenOnServer(refreshToken)
                    if (!stillValid) {
                        tokenManager.clearAccessToken()
                        _startScreen.value = AuthScreen
                    }
                }
            } else {
                // ✅ Попытка входа по биометрии
                val result = biometricAuthenticator.authenticate("Вход с использованием биометрии")
                if (result is BiometricResult.Success) {
                    try {
                        val response = refreshTokenUseCase(RefreshTokenRequest(refreshToken = refreshToken))
                        val responseResult = response.result

                        if (responseResult != null) {
                            tokenManager.saveTokens(responseResult.accessToken, responseResult.refreshToken)
                            _startScreen.value = MainScreen
                        } else {
                            // возможно, ошибка в ответе от сервера
                            tokenManager.clearAccessToken()
                            _startScreen.value = AuthScreen
                        }
                    } catch (e: Exception) {
                        tokenManager.clearAccessToken()
                        _startScreen.value = AuthScreen
                    }
                } else {
                    // отказ или ошибка → оставляем на экране логина
                    _startScreen.value = AuthScreen
                }
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
            refreshTokenUseCase(RefreshTokenRequest(refreshToken = refreshToken)).error == null
        } catch (e: Exception) {
            false
        }
    }

    @Serializable
    private data class JwtPayload(val exp: Long)
}
