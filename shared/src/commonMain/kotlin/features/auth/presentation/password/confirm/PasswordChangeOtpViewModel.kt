package features.auth.presentation.password.confirm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import core.presentation.BaseSideEffect
import features.auth.domain.model.ChangePasswordRequest
import features.auth.domain.usecase.ChangePasswordUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import uz.hb.shared.SharedRes

class PasswordChangeOtpViewModel(
    private val changePasswordUseCase: ChangePasswordUseCase,
    private val login: String,
    private val newPassword: String,
    private val oldPassword: String
) : ViewModel() {

    private val _uiState = MutableStateFlow(PasswordChangeOtpState())
    val uiState: StateFlow<PasswordChangeOtpState> = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<PasswordChangeOtpSideEffect>()
    val sideEffect: SharedFlow<PasswordChangeOtpSideEffect> = _sideEffect

    fun processIntent(intent: PasswordChangeOtpIntent) {
        when (intent) {
            PasswordChangeOtpIntent.ScreenEntered -> _uiState.value = PasswordChangeOtpState()
            is PasswordChangeOtpIntent.OtpInputChanged -> {
                _uiState.update {
                    it.copy(otpInput = intent.value, otpError = null)
                }
            }
            PasswordChangeOtpIntent.ConfirmButtonClicked -> verifyOtp()
        }
    }

    private fun verifyOtp() {
        val otp = _uiState.value.otpInput
        if (otp.isBlank()) {
            _uiState.update {
                it.copy(otpError = SharedRes.strings.error_otp_empty)
            }
            return
        }

        _uiState.update { it.copy(isLoading = true, generalError = null) }

        viewModelScope.launch {
            try {
                val result = changePasswordUseCase(
                    ChangePasswordRequest(
                        username = login,
                        password = oldPassword,
                        newPassword = newPassword,
                        otp = otp
                    )
                )

                result.sideEffect?.let { effect ->
                    val mapped = when (effect) {
                        is BaseSideEffect.ShowError -> PasswordChangeOtpSideEffect.ShowError(effect.message)
                        BaseSideEffect.SessionExpired -> PasswordChangeOtpSideEffect.SessionExpired
                        BaseSideEffect.NavigateBack -> PasswordChangeOtpSideEffect.NavigateBack
                        else -> error("Unsupported side effect: $effect")
                    }
                    _sideEffect.emit(mapped)
                }

                if (result.result?.result == "Password changed successfully") {
                    _sideEffect.emit(PasswordChangeOtpSideEffect.PasswordChanged)
                }

            } catch (e: Exception) {
                _sideEffect.emit(PasswordChangeOtpSideEffect.ShowError("${e.localizedMessage}"))
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }
}
