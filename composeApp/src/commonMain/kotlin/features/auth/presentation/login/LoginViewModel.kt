package features.auth.presentation.login

import androidx.lifecycle.ViewModel
import features.auth.domain.AuthRepository
import features.common.domain.auth.TokenManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.MainScope

class LoginViewModel(
    private val authRepository: AuthRepository,
    private val tokenManager: TokenManager,
    private val coroutineScope: CoroutineScope = MainScope()
) : ViewModel() {

    private val _state = MutableStateFlow<LoginState>(LoginState.EnterCredentials)
    val state: StateFlow<LoginState> = _state

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _sideEffect = MutableSharedFlow<AuthSideEffect>()
    val sideEffect: SharedFlow<AuthSideEffect> = _sideEffect

    fun dispatch(intent: LoginIntent) {
        when (intent) {

            //Ввод логина и пароля
            is LoginIntent.SubmitCredentials -> launchWithLoader {
                val response = authRepository.login(intent.username, intent.password)

                val error = response.error
                val result = response.result

                when (error?.code) {
                    61712 -> {
                        _sideEffect.emit(AuthSideEffect.NavigateToOtp)
                    }
                    60150 -> {
                        _sideEffect.emit(AuthSideEffect.NavigateToPasswordChange)
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
            is LoginIntent.SubmitOtp -> launchWithLoader {
                val response = authRepository.verifyOtp(intent.username, intent.password, intent.otp)

                val error = response.error
                val result = response.result

                when (error?.code) {
                    61712 -> {
                        _sideEffect.emit(AuthSideEffect.NavigateToOtp)
                    }
                    60150 -> {
                        _sideEffect.emit(AuthSideEffect.NavigateToPasswordChange)
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

            is LoginIntent.SubmitNewPassword -> launchWithLoader {
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
                _state.value = LoginState.WaitingForPasswordOtp
            }

            is LoginIntent.SubmitPasswordOtp -> launchWithLoader {
//                val token = authRepository.confirmPasswordChange(
//                    intent.username,
//                    intent.password,
//                    intent.newPassword,
//                    intent.otp
//                )
//                tokenManager.saveToken(token)
                _sideEffect.emit(AuthSideEffect.NavigateToMain)
            }

            LoginIntent.ClearState -> {
                _state.value = LoginState.EnterCredentials
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
