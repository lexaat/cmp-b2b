package data.theme

interface ThemeRepository {
    suspend fun isDarkTheme(): Boolean
    suspend fun setDarkTheme(enabled: Boolean)
}