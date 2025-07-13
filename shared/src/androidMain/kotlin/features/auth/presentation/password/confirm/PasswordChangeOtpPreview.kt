package features.auth.presentation.password.confirm

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Preview(
    showBackground = true,
    device = "spec:width=411dp,height=891dp"
)
@Composable
fun PasswordChangeOtpPreview() {
    ChangePasswordOtpContent(
        state = PasswordChangeOtpState(
            otpInput = "1234",
            otpError = null,
            isLoading = false
        ),
        onOtpChange = {},
        onConfirm = {},
        onBackClick = {}
    )
}