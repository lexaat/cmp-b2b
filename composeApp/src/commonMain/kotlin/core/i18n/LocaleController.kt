package core.i18n

import dev.icerock.moko.resources.desc.StringDesc
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object LocaleController {
    private val _locale = MutableStateFlow("ru")
    val locale: StateFlow<String> = _locale

    fun setLocale(code: String) {
        StringDesc.localeType = StringDesc.LocaleType.Custom(code)
        _locale.value = code
    }

    fun useSystemLocale() {
        StringDesc.localeType = StringDesc.LocaleType.System
    }
}