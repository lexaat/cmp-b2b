package features.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import core.error.ApiCallHandler
import core.presentation.BaseSideEffect
import features.auth.presentation.login.AuthSideEffect
import domain.model.Client
import features.home.domain.usecase.GetClientsUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class HomeViewModel(
    private val getClientsUseCase: GetClientsUseCase,
    private val apiCallHandler: ApiCallHandler,
) : ViewModel() {

    private val _state = MutableStateFlow<HomeState>(HomeState.Loading)
    val state: StateFlow<HomeState> = _state

    private val _sideEffect = MutableSharedFlow<BaseSideEffect>()
    val sideEffect: SharedFlow<BaseSideEffect> = _sideEffect

    var clients by mutableStateOf<List<Client>>(emptyList())
        private set

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

            val result = apiCallHandler.handleApiCall(
                call = { getClientsUseCase() },
                retry = { getClientsUseCase() },
                effectMapper = { baseEffect ->
                    when (baseEffect) {
                        is BaseSideEffect.ShowError -> AuthSideEffect.ShowError(baseEffect.message)
                        BaseSideEffect.SessionExpired -> AuthSideEffect.SessionExpired
                        BaseSideEffect.NavigateBack -> AuthSideEffect.NavigateBack
                        else -> error("Unsupported side effect: $baseEffect")
                    }
                }
            )

            result.sideEffect?.let { _sideEffect.emit(it) }

            result.result?.let { clients ->
                if (clients.isEmpty()) {
                    _state.value = HomeState.Empty
                } else {
                    _state.value = HomeState.Data(clients)
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
