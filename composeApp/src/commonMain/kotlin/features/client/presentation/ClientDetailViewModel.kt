package features.client.presentation

import androidx.lifecycle.viewModelScope
import core.error.ApiErrorHandler
import core.presentation.BaseViewModel
import features.client.domain.repository.ClientRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ClientDetailViewModel(
    private val repository: ClientRepository,
    errorHandler: ApiErrorHandler<ClientSideEffect>
) : BaseViewModel<ClientSideEffect>(errorHandler) {

    private val _state = MutableStateFlow<ClientDetailState>(ClientDetailState.Loading)
    val state: StateFlow<ClientDetailState> = _state

    private var lastClientId: String? = null

    fun dispatch(intent: ClientDetailIntent) {
        when (intent) {
            is ClientDetailIntent.Load -> loadClient(intent.clientId)
            is ClientDetailIntent.Retry -> lastClientId?.let { loadClient(it) }
        }
    }

    private fun loadClient(clientId: String) {
        _state.value = ClientDetailState.Loading
        lastClientId = clientId
        viewModelScope.launch {
            try {
                val client = repository.getClientDetail(clientId)
                _state.value = ClientDetailState.Data(client)
            } catch (e: Exception) {
                _state.value = ClientDetailState.Error(e.message ?: "Ошибка загрузки клиента")
            }
        }
    }
}
