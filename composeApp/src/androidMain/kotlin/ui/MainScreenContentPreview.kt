package ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import features.main.presentation.MainScreen
import features.main.presentation.MainScreenContent

@Preview(showBackground = true, device = "spec:width=1080px,height=2340px,dpi=440",
    showSystemUi = true, wallpaper = Wallpapers.RED_DOMINATED_EXAMPLE,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun MainScreenContentPreview() {
    MaterialTheme {
        val items = listOf(
            MainScreen.BottomNavItem(
                screen = object : Screen {
                    @Composable
                    override fun Content() {
                        Text("Главная", modifier = Modifier.padding(32.dp))
                    }
                },
                label = "Главная",
                icon = Icons.Default.Home
            ),
            MainScreen.BottomNavItem(
                screen = object : Screen {
                    @Composable
                    override fun Content() {
                        Text("Профиль", modifier = Modifier.padding(32.dp))
                    }
                },
                label = "Профиль",
                icon = Icons.Default.Person
            )
        )
        var selectedIndex by rememberSaveable { mutableStateOf(0) }

        MainScreenContent(
            items = items,
            selectedIndex = selectedIndex,
            onItemSelected = { selectedIndex = it },
            navigationBarColor = Color.Green,
            navigationBarAlpha = 0.6f,
        )
    }
}
