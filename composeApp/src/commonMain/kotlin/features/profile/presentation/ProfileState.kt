package features.profile.presentation

import domain.model.Client

sealed class ProfileState {
    object Loading : ProfileState()
    data class Data(val clients: List<Client>) : ProfileState()
    data class Error(val message: String) : ProfileState()
}
