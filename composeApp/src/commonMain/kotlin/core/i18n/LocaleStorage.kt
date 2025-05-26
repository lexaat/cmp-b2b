package core.i18n

import com.russhwolf.settings.Settings
import com.russhwolf.settings.get
import com.russhwolf.settings.set

object LocaleStorage {
    private const val KEY_LOCALE = "app_locale"

    private val settings: Settings = Settings()

    fun saveLocale(lang: String) {
        settings[KEY_LOCALE] = lang
    }

    fun loadLocale(): String? {
        return settings[KEY_LOCALE]
    }
}