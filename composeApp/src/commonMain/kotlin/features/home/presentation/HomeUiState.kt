package features.home.presentation

import features.home.domain.model.Client

data class HomeUiState(
    val isLoading: Boolean = false,
    val clients: List<Client> = emptyList(),
    val error: String? = null
)
