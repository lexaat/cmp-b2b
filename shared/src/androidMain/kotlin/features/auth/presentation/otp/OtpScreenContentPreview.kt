package features.auth.presentation.otp

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview

@Preview(showBackground = true, device = "spec:width=411dp,height=891dp")
@Composable
fun OtpScreenContentPreview() {
    val snackbarHostState = remember { SnackbarHostState() }

    OtpScreenContent(
        otp = "123456",
        onOtpChange = {},
        onSubmitClick = {},
        onBackClick = {},
        isLoading = false,
        snackbarHostState = snackbarHostState
    )
}