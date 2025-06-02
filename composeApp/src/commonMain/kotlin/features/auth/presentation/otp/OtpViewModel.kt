package features.auth.presentation.otp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import core.error.ApiCallHandler
import core.presentation.BaseSideEffect
import features.auth.domain.model.LoginRequest
import features.auth.domain.usecase.LoginUseCase
import features.common.domain.auth.TokenManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class OtpViewModel(
    private val loginUseCase: LoginUseCase,
    private val tokenManager: TokenManager,
    private val apiCallHandler: ApiCallHandler,
    private val coroutineScope: CoroutineScope = MainScope()
) : ViewModel() {

    private val _state = MutableStateFlow<OtpState>(OtpState.EnterOtp)
    val state: StateFlow<OtpState> = _state

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _sideEffect = MutableSharedFlow<OtpSideEffect>()
    val sideEffect: SharedFlow<OtpSideEffect> = _sideEffect

    fun reduce(intent: OtpIntent) {
        when (intent) {
            is OtpIntent.SubmitOtp -> verifyOtp(username = intent.username, password = intent.password, otp = intent.otp)
        }
    }

    fun verifyOtp(username: String, password: String, otp: String) {
        viewModelScope.launch {
            val result = apiCallHandler.handleApiCall(
                call = { loginUseCase(
                    LoginRequest(
                        username = username, password = password, otp = otp
                    ))  },
                retry = { loginUseCase(
                    LoginRequest(username = username, password = password, otp = otp)) },
                effectMapper = { baseEffect ->
                    when (baseEffect) {
                        is BaseSideEffect.ShowError -> OtpSideEffect.ShowError(baseEffect.message)
                        BaseSideEffect.SessionExpired -> OtpSideEffect.SessionExpired
                        BaseSideEffect.NavigateBack -> OtpSideEffect.NavigateBack
                        OtpSideEffect.NavigateToPasswordChange -> OtpSideEffect.NavigateToPasswordChange
                        else -> error("Unsupported side effect: $baseEffect")
                    }
                }
            )
            result.sideEffect?.let { _sideEffect.emit(it) }
            result.result?.let { auth ->
                // обработка успешного входа
                if (auth.accessToken.isNotBlank()) {
                    tokenManager.saveTokens(auth.accessToken, auth.refreshToken)
                    _sideEffect.emit(OtpSideEffect.NavigateToMain)
                } else {
                    _sideEffect.emit(OtpSideEffect.ShowError("Пустой токен"))
                }
            }
        }
    }
}
