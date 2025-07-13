package features.auth.presentation.password.change

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import core.presentation.BaseSideEffect
import features.auth.domain.model.ChangePasswordRequest
import features.auth.domain.usecase.ChangePasswordUseCase
import features.auth.presentation.login.LoginSideEffect
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
            is PasswordChangeRequestIntent.CurrentPasswordChanged -> {
                _uiState.update { it.copy(currentPassword = intent.value) }
            }

            is PasswordChangeRequestIntent.NewPasswordChanged -> {
                _uiState.update { it.copy(newPassword = intent.value) }
            }

            is PasswordChangeRequestIntent.ConfirmPasswordChanged -> {
                _uiState.update { it.copy(confirmPassword = intent.value) }
            }

            is PasswordChangeRequestIntent.ToggleShowCurrentPassword -> {
                _uiState.update { it.copy(showCurrentPassword = !it.showCurrentPassword) }
            }
            is PasswordChangeRequestIntent.ToggleShowNewPassword -> {
                _uiState.update { it.copy(showNewPassword = !it.showNewPassword) }
            }
            is PasswordChangeRequestIntent.ToggleShowConfirmPassword -> {
                _uiState.update { it.copy(showConfirmPassword = !it.showConfirmPassword) }
            }

            PasswordChangeRequestIntent.SubmitClicked -> {
                handleSubmit()
            }
        }
    }

    private fun handleSubmit() {
        val state = _uiState.value
        if (state.newPassword.length < 6) {
            _uiState.update { it.copy(passwordError = SharedRes.strings.error_password_too_short) }
            return
        }

        val currentPassword = state.currentPassword.ifBlank {
            password // из конструктора
        }

        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            try {
                val result = changePasswordUseCase(
                    ChangePasswordRequest(
                        username = login,
                        password = currentPassword,
                        newPassword = state.newPassword,
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

                val api = result.result
                if (api?.error == null) {
                    println("👁 newPassword=$state.newPassword, oldPassword=$currentPassword")
                    _sideEffect.emit(
                        PasswordChangeRequestSideEffect.NavigateToOtp(
                            login = login,
                            newPassword = state.newPassword,
                            oldPassword = currentPassword
                        )
                    )
                }

            } catch (e: Exception) {
                _sideEffect.emit(PasswordChangeRequestSideEffect.ShowError("${'$'}{e.localizedMessage}"))
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }
}
