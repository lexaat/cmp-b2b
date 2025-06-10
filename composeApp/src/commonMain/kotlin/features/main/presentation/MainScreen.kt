// commonMain/kotlin/features/main/presentation/MainScreen.kt
package features.main.presentation

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import core.i18n.LocaleController
import dev.icerock.moko.resources.compose.stringResource
import features.home.presentation.HomeScreen
import features.profile.presentation.ProfileScreen
import ui.components.AppleStyleBottomBar
import ui.components.navigation.CustomNavigationBar
import uz.hb.b2b.SharedRes

object MainScreen : Screen {

    @Composable
    override fun Content() {
        val currentLocale by LocaleController.locale.collectAsState()
        var selectedIndex by rememberSaveable { mutableStateOf(0) }

        var maxScrollState by remember { mutableStateOf(0.dp) }
        var currentScrollState by remember { mutableStateOf(0.dp) }
        var topAppBarHeight by remember { mutableStateOf(200.dp) }

        // Текущее значение скролла в dp
        val navigationBarAlpha by remember {
            derivedStateOf {
                if (maxScrollState - currentScrollState <= 5.dp) return@derivedStateOf 0f
                else if (maxScrollState - currentScrollState <= 7.dp) return@derivedStateOf 0.2f
                else if (maxScrollState - currentScrollState <= 9.dp) return@derivedStateOf 0.4f
                else if (maxScrollState - currentScrollState <= 11.dp) return@derivedStateOf 0.6f
                else if (maxScrollState - currentScrollState <= 13.dp) return@derivedStateOf 0.8f
                else return@derivedStateOf 1.0f
            }
        }

        // Управляем цветом панели с анимацией
        val navigationBarColor by animateColorAsState(
            targetValue = Color.White.copy(alpha = navigationBarAlpha)
        )

        val items = listOf(
            BottomNavItem(HomeScreen, stringResource(SharedRes.strings.home), Icons.Default.Home),
            BottomNavItem(ProfileScreen, stringResource(SharedRes.strings.profile), Icons.Default.Person)
        )

        key(currentLocale) {
            MainScreenContent(
                items = items,
                selectedIndex = selectedIndex,
                navigationBarColor = navigationBarColor,
                navigationBarAlpha = navigationBarAlpha,
                topAppBarHeight = topAppBarHeight,
                onItemSelected = { selectedIndex = it },
                onScrollStateReturned = { max, current ->
                    maxScrollState = max
                    currentScrollState = current
                }
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
    topAppBarHeight: Dp,
    onItemSelected: (Int) -> Unit,
    onScrollStateReturned: (Dp, Dp) -> Unit
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
                onSelect = onItemSelected,
                backgroundColor = navigationBarColor,
                alpha = navigationBarAlpha,
                topAppBarHeight = topAppBarHeight,
                onScrollStateReturned = onScrollStateReturned
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
