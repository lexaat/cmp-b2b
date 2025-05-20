package features.main.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Icon
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
                NavigationBar {
                    tabs.forEach { tab ->
                        NavigationBarItem(
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
