package app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import org.koin.compose.KoinContext
import org.koin.compose.koinInject
import ui.theme.AppTheme
import ui.theme.LocalThemeViewModel

@Composable
fun App() {
    KoinContext {
        val themeViewModel = koinInject<ThemeViewModel>()
        InternalApp(themeViewModel)
    }
}

@Composable
private fun InternalApp(themeViewModel: ThemeViewModel) {
    val isDark by themeViewModel.isDark.collectAsState()

    CompositionLocalProvider(LocalThemeViewModel provides themeViewModel) {
        AppTheme(darkTheme = isDark) {
            println("🔄 Theme recomposed. Dark = $isDark")

            // Важно: Navigator должен быть ВНУТРИ
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(WindowInsets.safeDrawing.asPaddingValues())
                    .background(MaterialTheme.colorScheme.background)
            ) {
                AppRoot()
            }
        }
    }
}