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
import dev.icerock.moko.resources.compose.stringResource
import features.home.presentation.HomeScreen
import features.profile.presentation.ProfileScreen
import uz.hb.b2b.SharedRes

object MainScreen : Screen {

    @Composable
    override fun Content() {
        val tabItems = listOf(
            stringResource(
            SharedRes.strings.home), stringResource(
                SharedRes.strings.profile))
        var selectedTab by remember { mutableStateOf(0) }
        val screens = listOf(HomeScreen, ProfileScreen)

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
