package features.auth.presentation.password.change

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import core.presentation.BaseSideEffect
import features.auth.domain.model.ChangePasswordRequest
import features.auth.domain.usecase.ChangePasswordUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uz.hb.shared.SharedRes

class PasswordChangeRequestViewModel(
    private val login: String,
    private val password: String,
    private val changePasswordUseCase: ChangePasswordUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(PasswordChangeRequestState())
    val uiState: StateFlow<PasswordChangeRequestState> = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<PasswordChangeRequestSideEffect>()
    val sideEffect: SharedFlow<PasswordChangeRequestSideEffect> = _sideEffect

    fun processIntent(intent: PasswordChangeRequestIntent) {
        when (intent) {
            is PasswordChangeRequestIntent.PasswordChanged -> {
                _uiState.update { it.copy(newPassword = intent.value, passwordError = null) }
            }
            PasswordChangeRequestIntent.SubmitClicked -> {
                handleSubmit()
            }
        }
    }

    private fun handleSubmit() {
        val newPassword = _uiState.value.newPassword

        if (newPassword.length < 6) {
            _uiState.update { it.copy(passwordError = SharedRes.strings.error_password_too_short) }
            return
        }

        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            try {
                val result = changePasswordUseCase(
                    ChangePasswordRequest(
                        username = login,
                        password = password,
                        newPassword = newPassword,
                        otp = ""
                    )
                )


                result.sideEffect?.let { effect ->
                    val mapped = when (effect) {
                        is BaseSideEffect.ShowError -> PasswordChangeRequestSideEffect.ShowError(effect.message)
                        is BaseSideEffect.NavigateBack -> PasswordChangeRequestSideEffect.ShowError("${'$'}{effect}")
                        else -> error("Unsupported side effect: ${'$'}effect")
                    }
                    _sideEffect.emit(mapped)
                }

                if (
                    result.sideEffect is BaseSideEffect.ShowError &&
                    (result.sideEffect.message.contains("61712") || result.sideEffect.message.contains("по СМС"))
                ) {
                    val maskedPhone = result.sideEffect.message.substringAfterLast("на номер ").trim()
                    _sideEffect.emit(PasswordChangeRequestSideEffect.NavigateToOtp(maskedPhone))
                }

            } catch (e: Exception) {
                _sideEffect.emit(PasswordChangeRequestSideEffect.ShowError("${'$'}{e.localizedMessage}"))
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }
}
