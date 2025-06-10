package ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun RoundedSquareLittleButton(icon: @Composable () -> Unit,
                              onClick: () -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(36.dp)  // Adjust size as needed
            .clip(shape = RoundedCornerShape(10.dp))
            .background(
                color = Color(0xFFfcfbfb),  // Light background color
            )
            .clickable(onClick = onClick)
    ) {
//        Icon(
//            painter = painterResource(icon),
//            contentDescription = "Icon",
//            tint = Color(0xFF4A4A4A),  // Icon color
//            modifier = Modifier.size(24.dp)  // Adjust icon size
//        )
        icon()
    }
}
