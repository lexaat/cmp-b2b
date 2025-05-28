package features.auth.presentation.password.change

import androidx.lifecycle.ViewModel
import features.auth.domain.AuthRepository
import features.common.domain.auth.TokenManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.MainScope
import platform.BiometricAuthenticator
import platform.BiometricResult

class PasswordChangeViewModel(
    private val authRepository: AuthRepository,
    private val tokenManager: TokenManager,
    private val biometricAuthenticator: BiometricAuthenticator,
    private val coroutineScope: CoroutineScope = MainScope()
) : ViewModel() {

    private val _state = MutableStateFlow<PasswordChangeState>(PasswordChangeState.EnterCredentials)
    val state: StateFlow<PasswordChangeState> = _state

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _sideEffect = MutableSharedFlow<PasswordChangeSideEffect>()
    val sideEffect: SharedFlow<PasswordChangeSideEffect> = _sideEffect

    val canUseBiometrics = MutableStateFlow(false)

    init {
        coroutineScope.launch {
            val token = tokenManager.getRefreshToken()
            val biometricsAvailable = biometricAuthenticator.isBiometricAvailable()
            canUseBiometrics.value = token != null && biometricsAvailable
        }
    }

    fun dispatch(intent: PasswordChangeIntent) {
        when (intent) {
            PasswordChangeIntent.ClearState -> TODO()
            is PasswordChangeIntent.SubmitNewPassword -> TODO()
        }
    }
}
