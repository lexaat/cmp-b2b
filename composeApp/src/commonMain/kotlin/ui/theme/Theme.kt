package ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme

val DarkColorScheme = darkColorScheme(
    primary = DarkBlue,
    background = DarkBackground,
    secondary = Secondary,
    onSecondary = OnSecondary,
    onPrimary = TextPrimaryDark,
    onBackground = TextPrimaryDark,
)

val LightColorScheme = lightColorScheme(
    primary = Blue,
    background = LightBackground,
    secondary = Secondary,
    onSecondary = OnSecondary,
    onPrimary = TextPrimaryLight,
    onBackground = TextPrimaryLight,
)

@Composable
fun AppTheme(
    darkTheme: Boolean,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography = AppTypography,
        content = content
    )
}