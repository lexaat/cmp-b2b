package features.home.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import core.presentation.BaseSideEffect
import dev.icerock.moko.resources.compose.stringResource
import features.auth.presentation.login.LoginScreen
import org.koin.compose.koinInject
import uz.hb.b2b.SharedRes

object HomeScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel = koinInject<HomeViewModel>()
        val state by viewModel.state.collectAsState()
        val navigator = LocalNavigator.currentOrThrow
        val snackbarHostState = remember { SnackbarHostState() }

        LaunchedEffect(Unit) {
            viewModel.sideEffect.collect { effect ->
                when (effect) {
                    is BaseSideEffect.NavigateToLogin -> {
                        navigator.popUntilRoot()
                        navigator.push(LoginScreen)
                    }
                    is BaseSideEffect.ShowError -> {
                        snackbarHostState.showSnackbar(effect.message)
                    }
                }
            }
        }

        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) { padding ->
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(padding)) {
                when (state) {
                    is HomeState.Loading -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }

                    is HomeState.Refreshing -> {
                        // Отобразим список как есть + индикатор сверху
                        val clients = (viewModel.state.value as? HomeState.Data)?.clients ?: emptyList()
                        Column {
                            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                            ClientListScreen(clients = clients)
                        }
                    }

                    is HomeState.Data -> {
                        ClientListScreen(clients = (state as HomeState.Data).clients)
                    }

                    is HomeState.Empty -> {
                        Text(
                            text = stringResource(SharedRes.strings.no_data),
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }

                    is HomeState.Error -> {
                        Column(modifier = Modifier.align(Alignment.Center)) {
                            Text("${stringResource(SharedRes.strings.an_error)}: ${(state as HomeState.Error).message}")
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(onClick = { viewModel.reduce(HomeIntent.Retry) }) {
                                Text(stringResource(SharedRes.strings.repeat))
                            }
                        }
                    }
                }
            }
        }
    }
}
