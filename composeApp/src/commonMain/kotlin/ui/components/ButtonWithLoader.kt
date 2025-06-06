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
import androidx.compose.ui.unit.dp

@Composable
fun ButtonWithLoader(
    buttonText: String,
    backgroundColor: Color,
    contentColor: Color = White,
    showLoader: Boolean = false,
    showBorder: Boolean,
    onClick: () -> Unit,
) {
    if (showLoader) {
        Row(
            modifier = Modifier
                .widthIn(max = 600.dp)
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, top = 20.dp)
                .height(48.dp)
                .clickable { onClick() }
                .border(
                    if (showBorder) 1.dp else 0.dp,
                    MaterialTheme.colorScheme.onSurface.copy(0.08f),
                    RoundedCornerShape(50.dp)
                )
                .background(backgroundColor, RoundedCornerShape(50.dp)),
            horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically
        ) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary, modifier = Modifier.size(28.dp))
        }
    } else {
        Row(
            modifier = Modifier
                .widthIn(max = 600.dp)
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, top = 20.dp)
                .height(48.dp)
                .clickable { onClick() } // motionClickEvent is my custom click modifier, use clickable modifier over here
                .border(
                    if (showBorder) 1.dp else 0.dp,
                    MaterialTheme.colorScheme.onSurface.copy(0.08f),
                    RoundedCornerShape(50.dp)
                )
                .background(backgroundColor, RoundedCornerShape(50.dp)),
            horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = buttonText,
                style = MaterialTheme.typography.labelLarge,
                color = contentColor,
                modifier = Modifier.padding(horizontal = 12.dp)
            )
        }
    }
}