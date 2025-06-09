package ui

import android.content.res.Configuration
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.tooling.preview.Wallpapers

import ui.components.AppTopBar

@Preview(showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
    wallpaper = Wallpapers.GREEN_DOMINATED_EXAMPLE, showSystemUi = true
)
@Composable
fun AppTopBarPreview() {

    val menuItems = listOf(
        Triple("Настройки", Icons.Default.Settings) { /* navigator.push(SettingsScreen()) */ },
        Triple("О приложении", Icons.Default.Info) { /* showAboutDialog() */ },
        Triple("Выход", Icons.AutoMirrored.Filled.ExitToApp) { /* logout() */ }
    )

    MaterialTheme {
        AppTopBar(
            title = "Авторизация",
            onBackClick = null,
            menuItems = menuItems,
            centered = true
        )
    }
}
