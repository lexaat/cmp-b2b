package features.auth.presentation

import features.auth.domain.AuthRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import utils.TokenManager

class AuthViewModel(
    private val authRepository: AuthRepository,
    private val tokenManager: TokenManager
) {
    private val _state = MutableStateFlow<AuthState>(AuthState.EnterCredentials)
    val state: StateFlow<AuthState> = _state

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _sideEffect = MutableSharedFlow<AuthSideEffect>()
    val sideEffect: SharedFlow<AuthSideEffect> = _sideEffect

    private val viewModelScope: CoroutineScope = MainScope()
    private var newPasswordBuffer: String? = null

    fun process(intent: AuthIntent) {
        when (intent) {
            is AuthIntent.SubmitCredentials -> login(intent.username, intent.password)
            is AuthIntent.SubmitOtp -> verifyOtp(intent.username, intent.password, intent.otp)
            is AuthIntent.SubmitNewPassword -> changePasswordStage1(intent.newPassword)
            is AuthIntent.SubmitPasswordOtp -> changePasswordStage2(intent.newPassword, intent.otp)
        }
    }

    private fun login(username: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = authRepository.login(username, password)
                when (result.error?.code) {
                    61712 -> _state.value = AuthState.WaitingForOtp
                    60150 -> _state.value = AuthState.RequirePasswordChange
                    null -> result.result?.let {
                        tokenManager.saveTokens(it.accessToken, it.refreshToken)
                        _state.value = AuthState.Authorized
                    } ?: run {
                        _sideEffect.emit(AuthSideEffect.ShowSnackbar("Empty result"))
                    }

                    else -> _sideEffect.emit(AuthSideEffect.ShowSnackbar(result.error.message))
                }
            } catch (e: Exception){
                _sideEffect.emit(AuthSideEffect.ShowSnackbar(e.message ?: "Неизвестная ошибка"))
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun verifyOtp(username: String, password: String, otp: String) {
        viewModelScope.launch {
            val result = authRepository.verifyOtp(username, password, otp)
            if (result.error == null && result.result != null) {
                tokenManager.saveTokens(result.result.accessToken, result.result.refreshToken)
                _state.value = AuthState.Authorized
            } else {
                _sideEffect.emit(AuthSideEffect.ShowSnackbar(result.error?.message ?: "Неизвестная ошибка"))
            }
        }
    }

    private fun changePasswordStage1(newPassword: String) {
        viewModelScope.launch {
            val result = authRepository.changePassword(newPassword, "")
            if (result.error == null) {
                newPasswordBuffer = newPassword
                _state.value = AuthState.WaitingForPasswordOtp
            } else {
                _sideEffect.emit(AuthSideEffect.ShowSnackbar(result.error.message))
            }
        }
    }

    private fun changePasswordStage2(newPassword: String, otp: String) {
        viewModelScope.launch {
            val result = authRepository.changePassword(newPassword, otp)
            if (result.error == null) {
                _state.value = AuthState.PasswordChanged
            } else {
                _sideEffect.emit(AuthSideEffect.ShowSnackbar(result.error.message))
            }
        }
    }
}
