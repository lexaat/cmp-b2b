package data.theme

import com.russhwolf.settings.Settings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ThemeRepositoryImpl(
    private val settings: Settings
) : ThemeRepository {

    companion object {
        private const val DARK_THEME_KEY = "dark_theme_enabled"
    }

    override suspend fun isDarkTheme(): Boolean = withContext(Dispatchers.Default) {
        settings.getBoolean(DARK_THEME_KEY, false)
    }

    override suspend fun setDarkTheme(enabled: Boolean) = withContext(Dispatchers.Default) {
        settings.putBoolean(DARK_THEME_KEY, enabled)
    }
}