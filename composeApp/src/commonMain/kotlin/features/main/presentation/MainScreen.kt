// commonMain/kotlin/features/main/presentation/MainScreen.kt
package features.main.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import core.i18n.LocaleController
import dev.icerock.moko.resources.compose.stringResource
import features.home.presentation.HomeScreen
import features.profile.presentation.ProfileScreen
import uz.hb.b2b.SharedRes
import androidx.compose.ui.Alignment
import ui.components.AppleStyleBottomBar

object MainScreen : Screen {

    @Composable
    override fun Content() {
        val currentLocale by LocaleController.locale.collectAsState()
        var selectedIndex by rememberSaveable { mutableStateOf(0) }

        val items = listOf(
            BottomNavItem(HomeScreen, stringResource(SharedRes.strings.home), Icons.Default.Home),
            BottomNavItem(ProfileScreen, stringResource(SharedRes.strings.profile), Icons.Default.Person)
        )

        key(currentLocale) {
            MainScreenContent(
                items = items,
                selectedIndex = selectedIndex,
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
    onItemSelected: (Int) -> Unit
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            AppleStyleBottomBar(
                selectedIndex = selectedIndex,
                items = listOf(
                    MainScreen.BottomNavItem(HomeScreen, "Главная", Icons.Default.Home),
                    MainScreen.BottomNavItem(ProfileScreen, "Профиль", Icons.Default.Person)
                ),
                onSelect = onItemSelected
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            items[selectedIndex].screen.Content()
        }
    }
}
