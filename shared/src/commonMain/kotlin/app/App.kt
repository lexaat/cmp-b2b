package app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import core.i18n.LocaleController
import dev.icerock.moko.resources.desc.StringDesc
import org.koin.compose.KoinContext
import org.koin.compose.koinInject
import ui.localization.LocalLocaleState
import ui.theme.AppTheme
import ui.theme.LocalThemeViewModel

@Composable
fun App() {
    KoinContext {
        val themeViewModel = koinInject<ThemeViewModel>()
        val isDark by themeViewModel.isDark.collectAsState()
        val locale by LocaleController.locale.collectAsState()

        val localeState = remember { mutableStateOf(StringDesc.localeType) }

        // Обновляем состояние при изменении из LocaleController
        LaunchedEffect(locale) {
            val type = StringDesc.LocaleType.Custom(locale)
            StringDesc.localeType = type
            localeState.value = type
        }

        CompositionLocalProvider(
            LocalThemeViewModel provides themeViewModel,
            LocalLocaleState provides localeState
        ) {
            AppTheme(darkTheme = isDark) {
                val backgroundColor = MaterialTheme.colorScheme.background
                currentBackgroundColorArgb = backgroundColor.toIntArgb()
                UpdateSystemBars(backgroundColor, isDark)

                AppRoot()
            }
        }
    }
}


