package data.theme

import data.storage.SecureStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ThemeRepositoryImpl(
    private val storage: SecureStorage
) : ThemeRepository {

    companion object {
        private const val DARK_THEME_KEY = "dark_theme_enabled"
    }

    override suspend fun isDarkTheme(): Boolean = withContext(Dispatchers.Default) {
        storage.get(DARK_THEME_KEY)?.toBoolean() ?: false
    }

    override suspend fun setDarkTheme(enabled: Boolean) = withContext(Dispatchers.Default) {
        storage.put(DARK_THEME_KEY, enabled.toString())
    }
}