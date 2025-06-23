package core.i18n

import data.storage.SecureStorage
import kotlinx.coroutines.runBlocking

object LocaleStorage {
    private const val KEY_LOCALE = "app_locale"

    lateinit var storage: SecureStorage

    fun init(storage: SecureStorage) {
        this.storage = storage
    }

    fun saveLocale(lang: String) = runBlocking {
        storage.put(KEY_LOCALE, lang)
    }

    fun loadLocale(): String? = runBlocking {
        storage.get(KEY_LOCALE)
    }
}