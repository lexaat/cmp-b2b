package ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun ButtonWithLoader(
    buttonText: String,
    backgroundColor: Color = Color.Transparent,
    contentColor: Color = White,
    showLoader: Boolean = false,
    showBorder: Boolean,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    val bg = if (enabled) backgroundColor else backgroundColor.copy(alpha = 0.2f)
    val textColor = if (enabled) contentColor else contentColor.copy(alpha = 0.5f)
    val borderColor = if (enabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
    val radius = 12.dp

    if (showLoader) {
        Row(
            modifier = Modifier
                .widthIn(max = 400.dp)
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 20.dp)
                .height(48.dp)
                .border(
                    if (showBorder) 2.dp else 0.dp,
                    borderColor,
                    RoundedCornerShape(radius)
                )
                .background(bg, RoundedCornerShape(radius)),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircularProgressIndicator(color = borderColor, modifier = Modifier.size(28.dp))
        }
    } else {
        Row(
            modifier = Modifier
                .widthIn(max = 400.dp)
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 20.dp)
                .height(48.dp)
                .clickable(enabled = enabled) { onClick() }
                .border(
                    if (showBorder) 2.dp else 0.dp,
                    borderColor,
                    RoundedCornerShape(radius)
                )
                .background(bg, RoundedCornerShape(radius)),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = buttonText.uppercase(),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold),
                color = textColor,
                modifier = Modifier.padding(horizontal = 12.dp)
            )
        }
    }
}
