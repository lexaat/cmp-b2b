package app

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
expect fun UpdateSystemBars(backgroundColor: Color, isDark: Boolean)