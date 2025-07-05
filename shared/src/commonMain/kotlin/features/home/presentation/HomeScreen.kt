package features.home.presentation

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import core.presentation.BaseSideEffect
import dev.icerock.moko.resources.compose.stringResource
import features.auth.presentation.login.LoginScreen
import org.koin.compose.koinInject
import ui.components.GradientBackground
import uz.hb.shared.SharedRes

object HomeScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel = koinInject<HomeViewModel>()

        val state by viewModel.state.collectAsState()
        val navigator = LocalNavigator.currentOrThrow
        val snackbarHostState = remember { SnackbarHostState() }

        var cardsBottomCoordinateInDp by remember { mutableStateOf(0.dp) }

        val listState = rememberLazyListState()
        val baseHeight = 260.dp
        val minHeight = 80.dp
        val maxOffsetPx = with(LocalDensity.current) { (baseHeight - minHeight).roundToPx() }

        val factor = if (listState.firstVisibleItemIndex > 0) 1f
        else (listState.firstVisibleItemScrollOffset / maxOffsetPx.toFloat()).coerceIn(0f, 1f)

        val heightDp = baseHeight - (baseHeight - minHeight) * factor

        val density = LocalDensity.current
        val bottomInset = WindowInsets.navigationBars.getBottom(density)
        val bottomInsetDp = with(density) { bottomInset.toDp() }
        val barHeight = 100.dp + bottomInsetDp
        val barHeightPx = with(density) { barHeight.toPx() }

        val lastItemIsAboveBar by remember {
            derivedStateOf {
                val layoutInfo = listState.layoutInfo
                val totalItems = layoutInfo.totalItemsCount
                val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()
                if (lastVisibleItem?.index == totalItems - 1) {
                    val itemBottom = lastVisibleItem.offset + lastVisibleItem.size
                    itemBottom <= layoutInfo.viewportEndOffset - barHeightPx
                } else false
            }
        }

        val barAlpha by animateFloatAsState(if (lastItemIsAboveBar) 0f else 1f)

        // Важно! Записываем alpha в ViewModel
        LaunchedEffect(barAlpha) {
            viewModel.updateNavigationBarAlpha(barAlpha)
            println("HomeScreen — barAlpha: $barAlpha")
        }

        LaunchedEffect(Unit) {
            viewModel.sideEffect.collect { effect ->
                when (effect) {
                    is BaseSideEffect.SessionExpired -> {
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
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.surface)
            ) {
                GradientBackground(
                    cardsBottomCoordinate = cardsBottomCoordinateInDp,
                    minHeight = heightDp
                )
                when (state) {
                    is HomeState.Loading -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }

                    is HomeState.Refreshing -> {
                        // Отобразим список как есть + индикатор сверху
                        val clients = (viewModel.state.value as? HomeState.Data)?.clients ?: emptyList()
                        Column {
                            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                            ClientListScreen(
                                clients = clients,
                                listState = listState,
                                bottomBarHeight = barHeight)
                        }
                    }

                    is HomeState.Data -> {
                        ClientListScreen(clients = (state as HomeState.Data).clients, listState = listState,
                            bottomBarHeight = barHeight)
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
