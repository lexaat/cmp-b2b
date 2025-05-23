package features.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import features.home.domain.repository.HomeRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: HomeRepository
) : ViewModel() {

    private val _state = MutableStateFlow<HomeState>(HomeState.Loading)
    val state: StateFlow<HomeState> = _state

    private val _sideEffect = MutableSharedFlow<HomeSideEffect>()
    val sideEffect: SharedFlow<HomeSideEffect> = _sideEffect

    init {
        dispatch(HomeIntent.LoadClients)
    }

    fun dispatch(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.LoadClients -> loadClients()
            is HomeIntent.Retry -> loadClients()
            is HomeIntent.SelectClient -> selectClient(intent.id)
        }
    }

    private fun loadClients() {
        _state.value = HomeState.Loading
        viewModelScope.launch {
            try {
                val clients = repository.getClients()
                _state.value = HomeState.Data(clients)
            } catch (e: Exception) {
                _state.value = HomeState.Error(e.message ?: "Ошибка загрузки клиентов")
                _sideEffect.emit(HomeSideEffect.ShowError(e.message ?: "Ошибка"))
            }
        }
    }

    private fun selectClient(id: String) {
        viewModelScope.launch {
            _sideEffect.emit(HomeSideEffect.NavigateToClientDetail(id))
        }
    }
}
