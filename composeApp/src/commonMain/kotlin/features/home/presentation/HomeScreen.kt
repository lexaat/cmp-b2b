package features.home.presentation

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import features.client.presentation.ErrorView

@Composable
fun HomeScreen(viewModel: HomeViewModel) {
    val stateValue = viewModel.state.collectAsState().value

    when (stateValue) {
        is HomeState.Loading -> CircularProgressIndicator()
        is HomeState.Error -> ErrorView(
            message = stateValue.message, // smart cast сработает
            onRetry = { viewModel.dispatch(HomeIntent.Retry) }
        )
        is HomeState.Data -> ClientListScreen(stateValue.clients)
    }
}
