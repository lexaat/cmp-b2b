package features.auth.presentation.otp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import core.presentation.BaseSideEffect
import features.auth.domain.model.LoginRequest
import features.auth.domain.usecase.LoginUseCase
import features.auth.presentation.login.LoginIntent
import features.auth.presentation.login.LoginSideEffect
import features.auth.presentation.login.LoginState
import features.common.domain.auth.TokenManager
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uz.hb.shared.SharedRes

class OtpViewModel(
    private val loginUseCase: LoginUseCase,
    private val tokenManager: TokenManager,
    val login: String,
    val password: String
) : ViewModel() {

    private val _uiState = MutableStateFlow(OtpState()) // Используем data class
    val uiState: StateFlow<OtpState> = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<OtpSideEffect>()
    val sideEffect: SharedFlow<OtpSideEffect> = _sideEffect

    fun processIntent(intent: OtpIntent) {
        when (intent) {

            OtpIntent.ScreenEntered -> {
                _uiState.value = OtpState()
            }

            is OtpIntent.OtpInputChanged -> {
                _uiState.update { currentState ->
                    currentState.copy(
                        otpInput = intent.value,
                        otpError = null // Сбрасываем ошибку при вводе
                    )
                }
            }
            OtpIntent.ConfirmButtonClicked -> {
                verifyOtp()
            }
        }
    }

    fun verifyOtp() {
        val currentOtp = _uiState.value.otpInput
        val isOtpValid = validateOtp(currentOtp)

        if (!isOtpValid) {
            return
        }

        _uiState.update { it.copy(isLoading = true, generalError = null) }

        viewModelScope.launch {
            try {
                val result = loginUseCase(
                    LoginRequest(
                        username = login,
                        password = password,
                        otp = currentOtp
                    )
                )
                result.sideEffect?.let { sideEffect ->
                    val mapped = when (sideEffect) {
                        is BaseSideEffect.ShowError -> OtpSideEffect.ShowError(sideEffect.message)
                        BaseSideEffect.SessionExpired -> OtpSideEffect.SessionExpired
                        BaseSideEffect.NavigateBack -> OtpSideEffect.NavigateBack
                        OtpSideEffect.NavigateToPasswordChange -> OtpSideEffect.NavigateToPasswordChange
                        else -> error("Unsupported side effect: $sideEffect")
                    }
                    _sideEffect.emit(mapped)
                }

                result.result?.let { auth ->
                    if (auth.accessToken.isNotBlank()) {
                        tokenManager.saveTokens(auth.accessToken, auth.refreshToken)
                        _sideEffect.emit(OtpSideEffect.NavigateToMain)
                    } else {
                        _sideEffect.emit(OtpSideEffect.ShowError("Пустой токен"))
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

    private fun validateOtp(otp: String): Boolean {
        if (otp.isBlank()) {
            _uiState.update { it.copy(otpError = SharedRes.strings.error_otp_empty) }
            return false
        }
        _uiState.update { it.copy(otpError = null) }
        return true
    }
}
