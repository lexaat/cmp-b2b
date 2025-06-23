package core.i18n

import dev.icerock.moko.resources.desc.StringDesc
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object LocaleController {
    private val _locale = MutableStateFlow("ru")
    val locale: StateFlow<String> = _locale

    fun setLocale(code: String) {
        StringDesc.localeType = StringDesc.LocaleType.Custom(code)
        LocaleStorage.saveLocale(code)
        _locale.value = code
    }

    fun applySavedLocaleOrSystemDefault() {
        val saved = LocaleStorage.loadLocale()
        if (saved != null) {
            StringDesc.localeType = StringDesc.LocaleType.Custom(saved)
            _locale.value = saved
        } else {
            StringDesc.localeType = StringDesc.LocaleType.System
        }
    }
}