package app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import org.koin.compose.KoinContext
import org.koin.compose.koinInject
import ui.theme.AppTheme
import ui.theme.LocalThemeViewModel

@Composable
fun App() {

    KoinContext {
        val themeViewModel = koinInject<ThemeViewModel>()
        val isDark by themeViewModel.isDark.collectAsState()

        CompositionLocalProvider(LocalThemeViewModel provides themeViewModel) {
            val background = MaterialTheme.colorScheme.background
            currentBackgroundColorArgb = background.toIntArgb()

            AppTheme(darkTheme = isDark) {
                println("🔄 Theme recomposed. Dark = $isDark")

                val backgroundColor = MaterialTheme.colorScheme.background
                UpdateSystemBars(backgroundColor, isDark)

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(backgroundColor)
                        .windowInsetsPadding(WindowInsets.safeDrawing)
                ) {
                    AppRoot()
                }
            }
        }
    }
}
