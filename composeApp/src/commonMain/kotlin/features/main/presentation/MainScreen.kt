// commonMain/kotlin/features/main/presentation/MainScreen.kt
package features.main.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import core.i18n.LocaleController
import dev.chrisbanes.haze.rememberHazeState
import dev.icerock.moko.resources.compose.stringResource
import features.home.presentation.HomeScreen
import features.home.presentation.HomeViewModel
import features.profile.presentation.ProfileScreen
import org.koin.compose.koinInject
import ui.components.AppleStyleBottomBar
import ui.theme.LocalHazeState
import uz.hb.b2b.SharedRes

object MainScreen : Screen {

    @Composable
    override fun Content() {

        val currentLocale by LocaleController.locale.collectAsState()
        var selectedIndex by rememberSaveable { mutableStateOf(0) }

        val viewModel = koinInject<HomeViewModel>() // тот же, что в HomeScreen
        val navigationBarAlpha by viewModel::navigationBarAlpha

        val items = listOf(
            BottomNavItem(HomeScreen, stringResource(SharedRes.strings.home), Icons.Default.Home),
            BottomNavItem(PaymentCreateScreen, "+", Icons.Default.Add),
            BottomNavItem(ProfileScreen, stringResource(SharedRes.strings.profile), Icons.Default.Person)
        )

        key(currentLocale) {
            MainScreenContent(
                items = items,
                selectedIndex = selectedIndex,
                navigationBarAlpha = navigationBarAlpha,
                onItemSelected = { selectedIndex = it },
            )
        }
    }

    data class BottomNavItem(
        val screen: Screen,
        val label: String,
        val icon: ImageVector
    )
}

@Composable
fun MainScreenContent(
    items: List<MainScreen.BottomNavItem>,
    selectedIndex: Int,
    navigationBarAlpha: Float,
    onItemSelected: (Int) -> Unit,
) {

    val hazeState = rememberHazeState()
    CompositionLocalProvider(LocalHazeState provides hazeState) {
    Scaffold(
        contentWindowInsets = WindowInsets.systemBars,
        containerColor = MaterialTheme.colorScheme.background,

    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            items[selectedIndex].screen.Content()

            println("MainScreen — navigationBarAlpha: $navigationBarAlpha")
            AppleStyleBottomBar(
                selectedIndex = selectedIndex,
                items = items,
                onSelect = onItemSelected,
                alpha = navigationBarAlpha,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
    }
}
