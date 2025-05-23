package features.main.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.screen.Screen
import features.home.presentation.HomeScreen

object MainScreen : Screen {

    @Composable
    override fun Content() {
        val tabItems = listOf("Главная", "Профиль")
        var selectedTab by remember { mutableStateOf(0) }
        val screens = listOf(HomeScreen, ProfileTab)

        Column {
            TabRow(selectedTabIndex = selectedTab) {
                tabItems.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title) }
                    )
                }
            }
            screens[selectedTab].Content()
        }
    }
}
