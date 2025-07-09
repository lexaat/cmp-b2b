package ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import core.i18n.LocaleController
import dev.icerock.moko.resources.ImageResource
import kotlinx.coroutines.launch
import dev.icerock.moko.resources.compose.painterResource
import uz.hb.shared.SharedRes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageSelector(modifier: Modifier = Modifier) {
    val currentLocale by LocaleController.locale.collectAsState()
    var showSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    val langCode = currentLocale.uppercase()
    val flag: Painter = when (currentLocale) {
        "ru" -> painterResource(SharedRes.images.ru) // ваш файл ru.svg
        "uz" -> painterResource(SharedRes.images.uz)
        "en" -> painterResource(SharedRes.images.gb)
        else -> painterResource(SharedRes.images.gb)
    }

    Box(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable { showSheet = true }
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
//            Image(
//                painter = flag,
//                contentDescription = null,
//                modifier = Modifier
//                    .size(28.dp)
//                    .border(1.dp, MaterialTheme.colorScheme.outline, shape = MaterialTheme.shapes.small)
//                    .padding(4.dp)
//            )
            Spacer(Modifier.width(8.dp))
            Text(langCode)
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = null
            )
        }
    }

    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSheet = false },
            sheetState = sheetState
        ) {
            Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                LanguageItem("Русский", SharedRes.images.ru, "ru", currentLocale) {
                    LocaleController.setLocale("ru")
                    scope.launch { sheetState.hide(); showSheet = false }
                }
                LanguageItem("O'zbek", SharedRes.images.uz, "uz", currentLocale) {
                    LocaleController.setLocale("uz")
                    scope.launch { sheetState.hide(); showSheet = false }
                }
                LanguageItem("English", SharedRes.images.gb, "en", currentLocale) {
                    LocaleController.setLocale("en")
                    scope.launch { sheetState.hide(); showSheet = false }
                }
            }
        }
    }
}

@Composable
private fun LanguageItem(language: String, image: ImageResource, localeCode: String, currentLocale: String, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(32.dp) // Чуть больше, чтобы вместить обводку
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline,
                    shape = CircleShape
                )
                .padding(2.dp)
        ) {
            Image(
                painter = painterResource(image),
                contentDescription = null,
                modifier = Modifier.fillMaxSize().clip(CircleShape)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = language,
            modifier = Modifier.weight(1f)
        )

        if (currentLocale == localeCode) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Selected",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

