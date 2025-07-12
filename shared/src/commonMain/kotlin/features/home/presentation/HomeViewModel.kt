package features.home.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import core.presentation.BaseSideEffect
import features.auth.presentation.login.LoginSideEffect
import features.home.domain.usecase.GetClientsUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getClientsUseCase: GetClientsUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow<HomeState>(HomeState.Loading)
    val state: StateFlow<HomeState> = _state

    private val _sideEffect = MutableSharedFlow<BaseSideEffect>()
    val sideEffect: SharedFlow<BaseSideEffect> = _sideEffect

    var navigationBarAlpha by mutableStateOf(1f)
        private set

    fun updateNavigationBarAlpha(alpha: Float) {
        navigationBarAlpha = alpha
    }
    init {
        loadClients()
        reduce(HomeIntent.LoadClients)
    }

    fun reduce(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.LoadClients,
            is HomeIntent.Retry -> loadClients()
            is HomeIntent.SelectClient -> viewModelScope.launch {
                _sideEffect.emit(HomeSideEffect.NavigateToClientDetail(intent.id))
            }
        }
    }

    private fun loadClients() {
        _state.value = when (_state.value) {
            is HomeState.Data -> HomeState.Refreshing
            else -> HomeState.Loading
        }

        viewModelScope.launch {
            val result = getClientsUseCase()

            result.sideEffect?.let { sideEffect ->
                val mapped = when (sideEffect) {
                    is BaseSideEffect.ShowError -> LoginSideEffect.ShowError(sideEffect.message)
                    BaseSideEffect.SessionExpired -> LoginSideEffect.SessionExpired
                    BaseSideEffect.NavigateBack -> LoginSideEffect.NavigateBack
                    is LoginSideEffect.NavigateToOtp -> sideEffect
                    else -> error("Unsupported side effect: $sideEffect")
                }
                _sideEffect.emit(mapped)
            }

            result.result?.let { clients ->
                if (clients.isEmpty()) {
                    _state.value = HomeState.Empty
                } else {
                    val repeatedClients = List(3) { clients }.flatten()
                    _state.value = HomeState.Data(repeatedClients)
                }
            } ?: run {
                if (result.sideEffect == null) {
                    _state.value = HomeState.Error("Неизвестная ошибка")
                }
            }
        }
    }

    private fun selectClient(id: String) {
        viewModelScope.launch {
            _sideEffect.emit(HomeSideEffect.NavigateToClientDetail(id))
        }
    }
}
