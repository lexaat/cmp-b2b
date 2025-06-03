package features.auth.presentation.password.change

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import core.presentation.BaseSideEffect
import features.auth.domain.model.ChangePasswordRequest
import features.auth.domain.usecase.ChangePasswordUseCase
import features.auth.presentation.login.AuthSideEffect
import features.auth.presentation.otp.OtpSideEffect
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ChangePasswordViewModel(
    private val changePasswordUseCase: ChangePasswordUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow<ChangePasswordState>(ChangePasswordState.EnterCredentials)
    val state: StateFlow<ChangePasswordState> = _state

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _sideEffect = MutableSharedFlow<ChangePasswordSideEffect>()
    val sideEffect: SharedFlow<ChangePasswordSideEffect> = _sideEffect

    fun reduce(intent: ChangePasswordIntent) {
        when (intent) {
            is ChangePasswordIntent.SubmitNewPassword -> changePassword(intent.username, intent.password, intent.newPassword)
            ChangePasswordIntent.ClearState -> TODO()
        }
    }

    fun changePassword(username: String, password: String, newPassword: String) {
        viewModelScope.launch {
            val result = changePasswordUseCase(
                ChangePasswordRequest(username = username, password = password, newPassword = newPassword, otp = ""))

            result.sideEffect?.let { sideEffect ->
                val mapped = when (sideEffect) {
                    is BaseSideEffect.ShowError -> ChangePasswordSideEffect.ShowError(sideEffect.message)
                    BaseSideEffect.SessionExpired -> ChangePasswordSideEffect.SessionExpired
                    BaseSideEffect.NavigateBack -> ChangePasswordSideEffect.NavigateBack
                    ChangePasswordSideEffect.NavigateToOtp -> ChangePasswordSideEffect.NavigateToOtp
                    else -> error("Unsupported side effect: $sideEffect")
                }
                _sideEffect.emit(mapped)
            }
        }
    }
}
