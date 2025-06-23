package features.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import features.common.domain.auth.TokenManager
import features.home.domain.repository.HomeRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _state = MutableStateFlow<ProfileState>(ProfileState.Loading)
    val state: StateFlow<ProfileState> = _state

    private val _sideEffect = MutableSharedFlow<ProfileSideEffect>()
    val sideEffect: SharedFlow<ProfileSideEffect> = _sideEffect

    fun logout() {
        CoroutineScope(Dispatchers.Main).launch {
            tokenManager.clearAccessToken()
            _sideEffect.emit(ProfileSideEffect.NavigateToLogin)
        }
    }

    fun confirmLogout() {
        viewModelScope.launch {
            _sideEffect.emit(ProfileSideEffect.ShowLogoutConfirmation)
        }
    }
}
