package ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import core.i18n.LocaleController

@Composable
fun LanguageSelector(modifier: Modifier = Modifier,) {
    val currentLocale by LocaleController.locale.collectAsState()
    var expanded by remember { mutableStateOf(false) }

    val currentLabel = when (currentLocale) {
        "ru" -> "Русский"
        "uz" -> "Oʻzbek"
        "en" -> "English"
        else -> "🌍 System"
    }

    Box(modifier = modifier) {
        TextButton(onClick = { expanded = true }) {
            Text("🌐 $currentLabel")
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Русский") },
                onClick = {
                    LocaleController.setLocale("ru")
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Oʻzbek") },
                onClick = {
                    LocaleController.setLocale("uz")
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("English") },
                onClick = {
                    LocaleController.setLocale("en")
                    expanded = false
                }
            )
        }
    }
}
