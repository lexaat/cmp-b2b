package features.home.presentation

import core.presentation.BaseViewModel
import core.error.ApiErrorHandler
import androidx.lifecycle.viewModelScope
import features.home.domain.repository.HomeRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: HomeRepository,
    errorHandler: ApiErrorHandler<HomeSideEffect>
) : BaseViewModel<HomeSideEffect>(errorHandler) {

    private val _state = MutableStateFlow<HomeState>(HomeState.Loading)
    val state: StateFlow<HomeState> = _state

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
                _sideEffect.emit(HomeSideEffect.ShowHomeError(e.message ?: "Ошибка"))
            }
        }
    }

    private fun selectClient(id: String) {
        viewModelScope.launch {
            //_sideEffect.emit(HomeSideEffect.NavigateToClientDetail(id))
        }
    }
}
