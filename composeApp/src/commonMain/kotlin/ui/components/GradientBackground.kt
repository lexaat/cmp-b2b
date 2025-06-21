package ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.max

@Composable
fun GradientBackground(cardsBottomCoordinate: Dp, minHeight: Dp) {

    val gradientHeight = max(cardsBottomCoordinate.value + 24.dp.value, minHeight.value)

    // Выбор градиента на основе текущей темы
    val isDark = MaterialTheme.colorScheme.background == ui.theme.DarkBackground
    val gradientColors = if (isDark) {
        listOf(
            ui.theme.DarkGradientStart,
            ui.theme.DarkGradientEnd
        )
    } else {
        listOf(
            ui.theme.LightGradientStart,
            ui.theme.LightGradientEnd
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(gradientHeight.dp)
            .background(
                brush = Brush.linearGradient(
                    colors = gradientColors,
                    start = Offset(0f, 0f),  // Верхний левый угол
                    end = Offset.Infinite  // Нижний правый угол
                ),
                shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
            )
    )
}