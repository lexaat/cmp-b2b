package features.auth.presentation.otp

import androidx.lifecycle.ViewModel
import features.auth.domain.AuthRepository
import features.common.domain.auth.TokenManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.MainScope

class OtpViewModel(
    private val authRepository: AuthRepository,
    private val tokenManager: TokenManager,
    private val coroutineScope: CoroutineScope = MainScope()
) : ViewModel() {

    private val _state = MutableStateFlow<OtpState>(OtpState.EnterOtp)
    val state: StateFlow<OtpState> = _state

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _sideEffect = MutableSharedFlow<OtpSideEffect>()
    val sideEffect: SharedFlow<OtpSideEffect> = _sideEffect

    fun dispatch(intent: OtpIntent) {
        when (intent) {

            //Ввод смс кода
            is OtpIntent.SubmitOtp -> launchWithLoader {
                val response = authRepository.verifyOtp(intent.username, intent.password, intent.otp)

                val error = response.error
                val result = response.result

                when (error?.code) {
                    null -> {
                        if (result != null) {
                            tokenManager.saveTokens(
                                accessToken = result.accessToken,
                                refreshToken = result.refreshToken
                            )
                            _sideEffect.emit(OtpSideEffect.NavigateToMain)
                        } else {
                            _sideEffect.emit(OtpSideEffect.ShowError("Пустой результат от сервера"))
                        }
                    }
                    else -> {
                        _sideEffect.emit(OtpSideEffect.ShowError(error.message))
                    }
                }
            }
        }
    }

    private fun launchWithLoader(block: suspend () -> Unit) = coroutineScope.launch {
        _isLoading.value = true
        try {
            block()
        } catch (e: Exception) {
            _sideEffect.emit(OtpSideEffect.ShowError(e.message ?: "Ошибка"))
        } finally {
            _isLoading.value = false
        }
    }
}
