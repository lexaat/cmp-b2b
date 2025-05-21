package features.auth.presentation

import androidx.lifecycle.ViewModel
import features.auth.domain.AuthRepository
import features.common.domain.auth.TokenManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.MainScope

class AuthViewModel(
    private val authRepository: AuthRepository,
    private val tokenManager: TokenManager,
    private val coroutineScope: CoroutineScope = MainScope()
) : ViewModel() {

    private val _state = MutableStateFlow<AuthState>(AuthState.EnterCredentials)
    val state: StateFlow<AuthState> = _state

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _sideEffect = MutableSharedFlow<AuthSideEffect>()
    val sideEffect: SharedFlow<AuthSideEffect> = _sideEffect

    fun dispatch(intent: AuthIntent) {
        when (intent) {

            //Ввод логина и пароля
            is AuthIntent.SubmitCredentials -> launchWithLoader {
                val response = authRepository.login(intent.username, intent.password)

                val error = response.error
                val result = response.result

                when (error?.code) {
                    61712 -> {
                        _state.value = AuthState.WaitingForOtp
                    }
                    60150 -> {
                        _state.value = AuthState.RequirePasswordChange
                    }
                    null -> {
                        if (result != null) {
                            tokenManager.saveTokens(
                                accessToken = result.accessToken,
                                refreshToken = result.refreshToken
                            )
                            _sideEffect.emit(AuthSideEffect.NavigateToMain)
                        } else {
                            _sideEffect.emit(AuthSideEffect.ShowError("Пустой результат от сервера"))
                        }
                    }
                    else -> {
                        _sideEffect.emit(AuthSideEffect.ShowError(error.message))
                    }
                }
            }

            //Ввод смс кода
            is AuthIntent.SubmitOtp -> launchWithLoader {
                val response = authRepository.verifyOtp(intent.username, intent.password, intent.otp)

                val error = response.error
                val result = response.result

                when (error?.code) {
                    61712 -> {
                        _state.value = AuthState.WaitingForOtp
                    }
                    60150 -> {
                        _state.value = AuthState.RequirePasswordChange
                    }
                    null -> {
                        if (result != null) {
                            tokenManager.saveTokens(
                                accessToken = result.accessToken,
                                refreshToken = result.refreshToken
                            )
                            _sideEffect.emit(AuthSideEffect.NavigateToMain)
                        } else {
                            _sideEffect.emit(AuthSideEffect.ShowError("Пустой результат от сервера"))
                        }
                    }
                    else -> {
                        _sideEffect.emit(AuthSideEffect.ShowError(error.message))
                    }
                }
            }

            is AuthIntent.SubmitNewPassword -> launchWithLoader {
                val response = authRepository.changePassword(
                    username = intent.username,
                    password = intent.password,
                    otp = "",
                    newPassword = intent.newPassword)

                response.error?.let {
                    _sideEffect.emit(AuthSideEffect.ShowError(it.message))
                    return@launchWithLoader
                }

                // симулируем, что всегда требуется подтверждение
                _state.value = AuthState.WaitingForPasswordOtp
            }

            is AuthIntent.SubmitPasswordOtp -> launchWithLoader {
//                val token = authRepository.confirmPasswordChange(
//                    intent.username,
//                    intent.password,
//                    intent.newPassword,
//                    intent.otp
//                )
//                tokenManager.saveToken(token)
                _sideEffect.emit(AuthSideEffect.NavigateToMain)
            }
        }
    }

    private fun launchWithLoader(block: suspend () -> Unit) = coroutineScope.launch {
        _isLoading.value = true
        try {
            block()
        } catch (e: Exception) {
            _sideEffect.emit(AuthSideEffect.ShowError(e.message ?: "Ошибка"))
        } finally {
            _isLoading.value = false
        }
    }
}
