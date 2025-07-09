package ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp

data class LinkPart(
    val text: String,
    val tag: String? = null, // Для идентификации типа ссылки, если нужно
    val annotation: String? = null // URL или другой идентификатор
)

@Composable
fun TextWithLinks(
    parts: List<LinkPart>,
    modifier: Modifier = Modifier
) {
    val uriHandler = LocalUriHandler.current
    val annotatedString = buildAnnotatedString {
        parts.forEachIndexed { index, part ->
            if (index > 0) {
                // Вставляем пробел, если предыдущая часть не закончилась пробелом и текущая не начинается с пробела
                val previous = parts[index - 1]
                if (!previous.text.endsWith(" ") && !part.text.startsWith(" ")) {
                    append(" ")
                }
            }

            if (part.annotation != null && part.tag != null) {
                pushStringAnnotation(tag = part.tag, annotation = part.annotation)
                withStyle(
                    style = SpanStyle(
                        color = MaterialTheme.colorScheme.primary,
                        //textDecoration = TextDecoration.Underline,
                        fontSize = 16.sp
                    )
                ) {
                    append(part.text)
                }
                pop()
            } else {
                withStyle(
                    // Обычный стиль текста, можно взять из MaterialTheme
                    style = SpanStyle(fontSize = 16.sp, color = MaterialTheme.colorScheme.outline)
                ) {
                    append(part.text)
                }
            }
        }
    }

    ClickableText(
        text = annotatedString,
        modifier = modifier.fillMaxWidth(),
        style = TextStyle(
            textAlign = TextAlign.Center, // <--- Текст ВНУТРИ ClickableText выравнивается по центру
            color = MaterialTheme.colorScheme.outline // Устанавливаем цвет текста, так как ClickableText по умолчанию может не иметь его
        ),
        onClick = { offset ->
            // Можно проверять разные теги, если у вас несколько типов ссылок
            annotatedString.getStringAnnotations("URL", offset, offset)
                .firstOrNull()
                ?.let { annotation ->
                    try {
                        uriHandler.openUri(annotation.item)
                    } catch (e: Exception) {
                        println("Could not open URL: ${annotation.item}, Error: ${e.message}")
                    }
                }
            // Пример для другого тега
            // annotatedString.getStringAnnotations("USER_ID", offset, offset)
            //    .firstOrNull()
            //    ?.let { annotation ->
            //        println("User ID clicked: ${annotation.item}")
            //    }
        }
    )
}