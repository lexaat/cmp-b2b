// commonMain/kotlin/features/main/presentation/MainScreen.kt
package features.main.presentation

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import core.i18n.LocaleController
import dev.icerock.moko.resources.compose.stringResource
import features.home.presentation.HomeScreen
import features.home.presentation.HomeViewModel
import features.profile.presentation.ProfileScreen
import org.koin.compose.koinInject
import ui.components.AppleStyleBottomBar
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
            BottomNavItem(ProfileScreen, stringResource(SharedRes.strings.profile), Icons.Default.Person)
        )

        key(currentLocale) {
            MainScreenContent(
                items = items,
                selectedIndex = selectedIndex,
                navigationBarColor = MaterialTheme.colorScheme.background,
                navigationBarAlpha = navigationBarAlpha,
                onItemSelected = { selectedIndex = it }
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
    navigationBarColor: Color,
    navigationBarAlpha: Float,
    onItemSelected: (Int) -> Unit
) {

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
                backgroundColor = navigationBarColor,
                alpha = navigationBarAlpha,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }

    }
}
