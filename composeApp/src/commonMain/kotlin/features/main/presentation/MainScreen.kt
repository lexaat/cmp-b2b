package features.main.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen

object MainScreen : Screen {

    @Composable
    override fun Content() {
        println("🔥 MainScreen Content() запущен")

        val tabs = listOf(HomeTab, ProfileTab)
        var selectedTab by remember { mutableStateOf<Screen>(HomeTab) }

        Scaffold(
            bottomBar = {
                BottomNavigation {
                    tabs.forEach { tab ->
                        BottomNavigationItem(
                            selected = tab == selectedTab,
                            onClick = {
                                selectedTab = tab
                            },
                            icon = {
                                Text(
                                    when (tab) {
                                        is HomeTab -> "🏠"
                                        is ProfileTab -> "👤"
                                        else -> "❓"
                                    }
                                )
                            },
                            label = {
                                Text(
                                    when (tab) {
                                        is HomeTab -> "Домой"
                                        is ProfileTab -> "Профиль"
                                        else -> "Экран"
                                    }
                                )
                            }
                        )
                    }
                }
            }
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                selectedTab.Content()
            }
        }
    }
}
