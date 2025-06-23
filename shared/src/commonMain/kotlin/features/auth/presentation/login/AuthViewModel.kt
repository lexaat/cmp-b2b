package features.auth.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import core.presentation.BaseSideEffect
import features.auth.domain.model.LoginRequest
import features.auth.domain.model.RefreshTokenRequest
import features.auth.domain.usecase.LoginUseCase
import features.auth.domain.usecase.RefreshTokenUseCase
import features.common.domain.auth.TokenManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import platform.BiometricAuthenticator
import platform.BiometricResult

class AuthViewModel(
    private val loginUseCase: LoginUseCase,
    private val refreshTokenUseCase: RefreshTokenUseCase,
    private val tokenManager: TokenManager,
    private val biometricAuthenticator: BiometricAuthenticator,
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
        _state.value = AuthState.Loading
        when (intent) {
            is AuthIntent.SubmitCredentials -> login(intent.username, intent.password)
        }
    }

    fun login(username: String, password: String) {
        viewModelScope.launch {
            val result =
                loginUseCase(LoginRequest(username = username, password = password, otp = ""))

            result.sideEffect?.let { sideEffect ->
                val mapped = when (sideEffect) {
                    is BaseSideEffect.ShowError -> AuthSideEffect.ShowError(sideEffect.message)
                    BaseSideEffect.SessionExpired -> AuthSideEffect.SessionExpired
                    BaseSideEffect.NavigateBack -> AuthSideEffect.NavigateBack
                    AuthSideEffect.NavigateToOtp -> AuthSideEffect.NavigateToOtp
                    else -> error("Unsupported side effect: $sideEffect")
                }
                _sideEffect.emit(mapped)
            }

            result.result?.let { auth ->
                if (auth.accessToken.isNotBlank()) {
                    tokenManager.saveTokens(auth.accessToken, auth.refreshToken)
                    _sideEffect.emit(AuthSideEffect.NavigateToMain)
                } else {
                    _sideEffect.emit(AuthSideEffect.ShowError("Пустой токен"))
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
                        val response = refreshTokenUseCase(RefreshTokenRequest(refreshToken = refreshToken))
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
