package ui.components

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.Box

@Composable
actual fun BlurredBackground(modifier: Modifier) {
    // Простой серый фон с прозрачностью как имитация
    Box(
        modifier = modifier.background(Color(0xEEFFFFFF))
    )
}