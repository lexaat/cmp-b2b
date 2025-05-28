package features.auth.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import core.error.ApiErrorHandler
import core.presentation.BaseSideEffect
import features.auth.domain.AuthRepository
import features.common.domain.auth.TokenManager
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthRepository,
    private val tokenManager: TokenManager,
    private val errorHandler: ApiErrorHandler<BaseSideEffect>
) : ViewModel() {

    private val _state = MutableStateFlow<AuthState>(AuthState.EnterCredentials)
    val state: StateFlow<AuthState> = _state

    private val _sideEffect = MutableSharedFlow<AuthSideEffect>()
    val sideEffect: SharedFlow<AuthSideEffect> = _sideEffect

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
}
