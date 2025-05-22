package features.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import features.common.domain.auth.TokenManager
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _sideEffect = MutableSharedFlow<ProfileSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    fun logout() {
        viewModelScope.launch {
            tokenManager.clearTokens()
            _sideEffect.emit(ProfileSideEffect.NavigateToLogin)
        }
    }

    fun confirmLogout() {
        viewModelScope.launch {
            _sideEffect.emit(ProfileSideEffect.ShowLogoutConfirmation)
        }
    }
}
