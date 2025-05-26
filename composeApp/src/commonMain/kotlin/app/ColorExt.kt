package app

import androidx.compose.ui.graphics.Color

fun Color.toIntArgb(): Int {
    val a = (alpha * 255.0f + 0.5f).toInt() and 0xFF
    val r = (red * 255.0f + 0.5f).toInt() and 0xFF
    val g = (green * 255.0f + 0.5f).toInt() and 0xFF
    val b = (blue * 255.0f + 0.5f).toInt() and 0xFF
    return (a shl 24) or (r shl 16) or (g shl 8) or b
}