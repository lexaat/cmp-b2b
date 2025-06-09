package ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun InsetAwareBottomBar(
    baseHeight: Dp = 80.dp,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    contentAlignment: Alignment = Alignment.TopCenter,
    content: @Composable () -> Unit
) {
    val density = LocalDensity.current
    val bottomInsetPx = WindowInsets.navigationBars.getBottom(density)
    val bottomInsetDp = with(density) { bottomInsetPx.toDp() }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(baseHeight + bottomInsetDp)
            .background(backgroundColor),
        contentAlignment = contentAlignment
    ) {
        content()
    }
}