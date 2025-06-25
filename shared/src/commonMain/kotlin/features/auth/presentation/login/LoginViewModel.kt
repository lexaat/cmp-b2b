package features.auth.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import core.presentation.BaseSideEffect
import dev.icerock.moko.resources.compose.localized
import dev.icerock.moko.resources.desc.desc
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
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import platform.BiometricAuthenticator
import platform.BiometricResult
import uz.hb.shared.SharedRes

class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val refreshTokenUseCase: RefreshTokenUseCase,
    private val tokenManager: TokenManager,
    private val biometricAuthenticator: BiometricAuthenticator,
    private val coroutineScope: CoroutineScope = MainScope()
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginState()) // Используем data class
    val uiState: StateFlow<LoginState> = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<LoginSideEffect>()
    val sideEffect: SharedFlow<LoginSideEffect> = _sideEffect

    val canUseBiometrics = MutableStateFlow(false)

    init {
        coroutineScope.launch {
            val token = tokenManager.getRefreshToken()
            val biometricsAvailable = biometricAuthenticator.isBiometricAvailable()
            canUseBiometrics.value = token != null && biometricsAvailable
        }
    }

    fun processIntent(intent: LoginIntent) {
        when (intent) {

            LoginIntent.ScreenEntered -> {
                _uiState.value = LoginState() // Просто создаем новый дефолтный стейт
            }

            is LoginIntent.LoginInputChanged -> {
                _uiState.update { currentState ->
                    currentState.copy(
                        loginInput = intent.value,
                        loginError = null // Сбрасываем ошибку при вводе
                    )
                }
            }
            is LoginIntent.PasswordInputChanged -> {
                _uiState.update { currentState ->
                    currentState.copy(
                        passwordInput = intent.value,
                        passwordError = null // Сбрасываем ошибку при вводе
                    )
                }
            }
            LoginIntent.LoginButtonClicked -> {
                performLogin()
            }
        }
    }

    private fun performLogin() {
        // Временная заглушка для демонстрации
        val currentLogin = _uiState.value.loginInput
        val currentPassword = _uiState.value.passwordInput

        // Валидация
        val isLoginValid = validateLogin(currentLogin)
        val isPasswordValid = validatePassword(currentPassword)

        if (!isLoginValid || !isPasswordValid) {
            // Если есть ошибки валидации, ViewModel уже обновил _uiState
            // в функциях validateLogin/validatePassword. Ничего больше не делаем здесь.
            return
        }

        _uiState.update { it.copy(isLoading = true, generalError = null) }

        viewModelScope.launch {
            try {
            val result =
                loginUseCase(LoginRequest(username = currentLogin, password = currentPassword, otp = ""))

            result.sideEffect?.let { sideEffect ->
                val mapped = when (sideEffect) {
                    is BaseSideEffect.ShowError -> LoginSideEffect.ShowError(sideEffect.message)
                    BaseSideEffect.SessionExpired -> LoginSideEffect.SessionExpired
                    BaseSideEffect.NavigateBack -> LoginSideEffect.NavigateBack
                    LoginSideEffect.NavigateToOtp -> LoginSideEffect.NavigateToOtp
                    else -> error("Unsupported side effect: $sideEffect")
                }
                _sideEffect.emit(mapped)
            }

            result.result?.let { auth ->
                if (auth.accessToken.isNotBlank()) {
                    tokenManager.saveTokens(auth.accessToken, auth.refreshToken)
                    _sideEffect.emit(LoginSideEffect.NavigateToMain)
                } else {
                    _sideEffect.emit(LoginSideEffect.ShowError("Пустой токен"))
                }
            }
            } catch (e: Exception) { // Ловим другие неожиданные исключения от UseCase
                // Log.e("LoginViewModel", "Login failed with exception", e) // Используйте ваш логгер
                _uiState.update { it.copy(generalError = SharedRes.strings.an_error) } // Общая ошибка сети/сервера
                // Также можно отправить ошибку в Snackbar, если предпочитаете
                // _sideEffect.emit(LoginSideEffect.ShowError(stringProvider.getString(MR.strings.error_network)))
            } finally {
                _uiState.update { it.copy(isLoading = false) } // <--- СБРОС ЛОАДЕРА ЗДЕСЬ
            }
        }

    }

    private fun validateLogin(login: String): Boolean {
        if (login.isBlank()) {
            _uiState.update { it.copy(loginError = SharedRes.strings.error_login_empty) }
            return false
        }
        // Другие проверки для логина (например, формат email)
        // if (!android.util.Patterns.EMAIL_ADDRESS.matcher(login).matches()) { // Пример для Android
        // _uiState.update { it.copy(loginError = "Неверный формат email") }
        // return false
        // }
        _uiState.update { it.copy(loginError = null) } // Очищаем ошибку, если все хорошо
        return true
    }

    private fun validatePassword(password: String): Boolean {
        if (password.isBlank()) {
            _uiState.update { it.copy(passwordError = SharedRes.strings.error_login_empty) }
            return false
        }
        if (password.length < 6) {
            _uiState.update { it.copy(passwordError = SharedRes.strings.error_login_empty) }
            return false
        }
        _uiState.update { it.copy(passwordError = null) } // Очищаем ошибку, если все хорошо
        return true
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
                            _sideEffect.emit(LoginSideEffect.NavigateToMain)
                        } else {
                            _sideEffect.emit(LoginSideEffect.ShowError("Пустой ответ от сервера"))
                        }
                    } catch (e: Exception) {
                        _sideEffect.emit(LoginSideEffect.ShowError("Ошибка при обновлении токена"))
                    }
                } else {
                    _sideEffect.emit(LoginSideEffect.ShowError("Нет refresh токена"))
                }
            } else if (result is BiometricResult.Failed) {
                _sideEffect.emit(LoginSideEffect.ShowError(result.message))
            }
        }
    }
}
