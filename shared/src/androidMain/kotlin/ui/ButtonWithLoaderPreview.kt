package ui

import android.content.res.Configuration
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import ui.components.ButtonWithLoader

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_TYPE_NORMAL,
    backgroundColor = 0xFFFFFFFF
)
@Composable
fun ButtonWithLoaderPreview() {
    MaterialTheme {
        ButtonWithLoader(
            buttonText = "Войти",
            backgroundColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onSurface,
            showLoader = false,
            showBorder = true,
            onClick = {},
            enabled = true
        )
    }
}

@Preview(showBackground = true, name = "Loader", backgroundColor = 0xFFFFFFFF)
@Composable
fun ButtonWithLoaderLoadingPreview() {
    MaterialTheme {
        ButtonWithLoader(
            buttonText = "Войти",
            backgroundColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onSurface,
            showLoader = false,
            showBorder = true,
            onClick = {},
            enabled = false
        )
    }
}