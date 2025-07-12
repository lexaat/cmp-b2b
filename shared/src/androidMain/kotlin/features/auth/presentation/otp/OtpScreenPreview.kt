package features.auth.presentation.otp

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview

@Preview(
    showBackground = true,
    device = "spec:width=411dp,height=891dp"
)
@Composable
fun OtpScreenPreview() {
    val state = OtpState(
        otpInput = "1234",
        otpError = null,
        isLoading = false
    )

    OtpScreenContent(
        state = state,
        maskedPhoneNumber = "+998 90 *** ** 31",
        onOtpChange = {},
        onConfirm = {},
        snackbarHostState = remember { SnackbarHostState() }
    )
}