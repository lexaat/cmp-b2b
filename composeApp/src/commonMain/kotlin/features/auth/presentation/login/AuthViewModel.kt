package features.auth.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import core.error.ApiErrorHandler
import core.presentation.BaseSideEffect
import features.auth.domain.AuthRepository
import features.common.domain.auth.TokenManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import platform.BiometricAuthenticator
import platform.BiometricResult

class AuthViewModel(
    private val authRepository: AuthRepository,
    private val tokenManager: TokenManager,
    private val biometricAuthenticator: BiometricAuthenticator,
    private val errorHandler: ApiErrorHandler<AuthSideEffect>,
    private val coroutineScope: CoroutineScope = MainScope()
) : ViewModel() {

    private val _state = MutableStateFlow<AuthState>(AuthState.EnterCredentials)
    val state: StateFlow<AuthState> = _state

    private val _sideEffect = MutableSharedFlow<AuthSideEffect>()
    val sideEffect: SharedFlow<AuthSideEffect> = _sideEffect

    val canUseBiometrics = MutableStateFlow(false)

    init {
        coroutineScope.launch {
            val token = tokenManager.getRefreshToken()
            val biometricsAvailable = biometricAuthenticator.isBiometricAvailable()
            canUseBiometrics.value = token != null && biometricsAvailable
        }
    }

    fun reduce(intent: AuthIntent) {
        when (intent) {
            is AuthIntent.SubmitCredentials -> login(intent.username, intent.password)
        }
    }

    private fun login(username: String, password: String) {
        viewModelScope.launch {
            _state.value = AuthState.Loading

            val result = errorHandler.handleApiCall(
                call = { authRepository.login(username, password) },
                retry = { authRepository.login(username, password) }
            )

            result.sideEffect?.let { _sideEffect.emit(it) }

            result.result?.let {
                when {
                    it.accessToken.isNotBlank() -> {
                        tokenManager.saveTokens(it.accessToken, it.refreshToken)
                        _sideEffect.emit(AuthSideEffect.NavigateToMain)
                    }
                    else -> _sideEffect.emit(AuthSideEffect.ShowError("Пустой токен"))
                }
            }

            _state.value = AuthState.EnterCredentials
        }
    }

    fun loginWithBiometrics() {
        coroutineScope.launch {
            val result = biometricAuthenticator.authenticate("Войти с использованием биометрии")
            if (result is BiometricResult.Success) {
                val refreshToken = tokenManager.getRefreshToken()
                if (refreshToken != null) {
                    try {
                        val response = authRepository.refreshToken(refreshToken)
                        val auth = response.result
                        if (auth != null) {
                            tokenManager.saveTokens(auth.accessToken, auth.refreshToken)
                            _sideEffect.emit(AuthSideEffect.NavigateToMain)
                        } else {
                            _sideEffect.emit(AuthSideEffect.ShowError("Пустой ответ от сервера"))
                        }
                    } catch (e: Exception) {
                        _sideEffect.emit(AuthSideEffect.ShowError("Ошибка при обновлении токена"))
                    }
                } else {
                    _sideEffect.emit(AuthSideEffect.ShowError("Нет refresh токена"))
                }
            } else if (result is BiometricResult.Failed) {
                _sideEffect.emit(AuthSideEffect.ShowError(result.message))
            }
        }
    }
}
