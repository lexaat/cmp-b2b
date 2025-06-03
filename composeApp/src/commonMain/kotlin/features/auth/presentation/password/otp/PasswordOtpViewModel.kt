package features.auth.presentation.password.otp

import androidx.lifecycle.ViewModel
import features.auth.domain.AuthRepository
import features.common.domain.auth.TokenManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.MainScope
import platform.BiometricAuthenticator
import platform.BiometricResult

class PasswordOtpViewModel(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _state = MutableStateFlow<PasswordOtpState>(PasswordOtpState.EnterCredentials)
    val state: StateFlow<PasswordOtpState> = _state

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _sideEffect = MutableSharedFlow<PasswordOtpSideEffect>()
    val sideEffect: SharedFlow<PasswordOtpSideEffect> = _sideEffect

}
