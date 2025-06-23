package ui.localization

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateOf
import dev.icerock.moko.resources.desc.StringDesc

val LocalLocaleState = compositionLocalOf {
    mutableStateOf(StringDesc.localeType)
}