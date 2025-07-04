package ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import ui.components.ButtonWithLoader

@Preview(showBackground = true)
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

@Preview(showBackground = true, name = "Loader")
@Composable
fun ButtonWithLoaderLoadingPreview() {
    MaterialTheme {
        ButtonWithLoader(
            buttonText = "Войти",
            backgroundColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onSurface,
            showLoader = true,
            showBorder = true,
            onClick = {},
            enabled = true
        )
    }
}