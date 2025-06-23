package features.client.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import dev.icerock.moko.resources.compose.stringResource
import org.koin.compose.koinInject
import uz.hb.shared.SharedRes

class ClientDetailScreen(private val clientId: String) : Screen {
    @Composable
    override fun Content() {
        val viewModel = koinInject<ClientDetailViewModel>()
        val state by viewModel.state.collectAsState()

        LaunchedEffect(clientId) {
            viewModel.dispatch(ClientDetailIntent.Load(clientId))
        }

        when (val s = state) {
            is ClientDetailState.Loading -> CircularProgressIndicator()
            is ClientDetailState.Error -> ErrorView(s.message) {
                viewModel.dispatch(ClientDetailIntent.Retry)
            }
            is ClientDetailState.Data -> ClientDetailView(s)
        }
    }
}

@Composable
fun ErrorView(message: String, onRetry: () -> Unit) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("${
            stringResource(
            SharedRes.strings.an_error)
        }: $message")
        Button(onClick = onRetry, modifier = Modifier.padding(top = 8.dp)) {
            Text(stringResource(
                SharedRes.strings.retry))
        }
    }
}

@Composable
fun ClientDetailView(state: ClientDetailState.Data) {
    val client = state.client
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Клиент: ${client.name}", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "ИНН: ${client.inn}")
        Text(text = "ID: ${client.id}")
    }
}
