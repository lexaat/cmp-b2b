package features.auth.presentation.password.otp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import core.presentation.BaseSideEffect
import features.auth.domain.AuthRepository
import features.auth.domain.model.ChangePasswordRequest
import features.auth.domain.usecase.ChangePasswordUseCase
import features.auth.presentation.password.change.ChangePasswordIntent
import features.auth.presentation.password.change.ChangePasswordSideEffect
import features.auth.presentation.password.change.ChangePasswordState
import features.common.domain.auth.TokenManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.MainScope
import platform.BiometricAuthenticator
import platform.BiometricResult

class PasswordOtpViewModel(
    private val changePasswordUseCase: ChangePasswordUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow<PasswordOtpState>(PasswordOtpState.EnterCredentials)
    val state: StateFlow<PasswordOtpState> = _state

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _sideEffect = MutableSharedFlow<PasswordOtpSideEffect>()
    val sideEffect: SharedFlow<PasswordOtpSideEffect> = _sideEffect

    fun reduce(intent: ChangePasswordOtpIntent) {
        when (intent) {
            is ChangePasswordOtpIntent.SubmitNewPassword -> changePassword(
                username = intent.username,
                password = intent.password,
                newPassword = intent.newPassword,
                otp = intent.otp
            )

            ChangePasswordOtpIntent.ClearState -> TODO()
        }
    }

    fun changePassword(username: String, password: String, newPassword: String, otp: String) {
        viewModelScope.launch {
            val result = changePasswordUseCase(
                ChangePasswordRequest(
                    username = username,
                    password = password,
                    newPassword = newPassword,
                    otp = otp
                )
            )

            result.sideEffect?.let { sideEffect ->
                val mapped = when (sideEffect) {
                    is BaseSideEffect.ShowError -> PasswordOtpSideEffect.ShowError(sideEffect.message)
                    BaseSideEffect.SessionExpired -> PasswordOtpSideEffect.SessionExpired
                    BaseSideEffect.NavigateBack -> PasswordOtpSideEffect.NavigateBack
                    PasswordOtpSideEffect.NavigateToLogin -> PasswordOtpSideEffect.NavigateToLogin
                    else -> error("Unsupported side effect: $sideEffect")
                }
                _sideEffect.emit(mapped)
            }

            result.result?.let {
                _sideEffect.emit(PasswordOtpSideEffect.NavigateToLogin)
            }
        }
    }
}
