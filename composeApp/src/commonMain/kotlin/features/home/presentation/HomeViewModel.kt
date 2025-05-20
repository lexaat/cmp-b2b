package features.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import features.home.domain.repository.HomeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val homeRepository: HomeRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState(isLoading = true))
    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        loadClients()
    }

    private fun loadClients() {
        viewModelScope.launch {
            try {
                val clients = homeRepository.getClients()
                _uiState.value = HomeUiState(clients = clients)
            } catch (e: Exception) {
                _uiState.value = HomeUiState(error = e.message)
            }
        }
    }
}
