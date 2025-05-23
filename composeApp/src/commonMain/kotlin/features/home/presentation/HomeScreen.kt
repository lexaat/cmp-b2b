package features.home.presentation

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.screen.Screen
import androidx.compose.material3.Text
import dev.icerock.moko.resources.compose.stringResource
import org.koin.compose.koinInject
import uz.hb.b2b.SharedRes

object HomeScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel = koinInject<HomeViewModel>()
        val state by viewModel.state.collectAsState()

        when (state) {
            is HomeState.Loading -> CircularProgressIndicator()
            is HomeState.Data -> ClientListScreen((state as HomeState.Data).clients)
            is HomeState.Error -> Text("${
                stringResource(
                SharedRes.strings.an_error)
            }: ${(state as HomeState.Error).message}")
        }
    }
}

