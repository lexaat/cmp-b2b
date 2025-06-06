package features.auth.presentation.otp

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview

@Preview(showBackground = true)
@Composable
fun OtpScreenContentPreview() {
    val snackbarHostState = remember { SnackbarHostState() }

    OtpScreenContent(
        otp = "123456",
        onOtpChange = {},
        onSubmitClick = {},
        isLoading = false,
        snackbarHostState = snackbarHostState
    )
}