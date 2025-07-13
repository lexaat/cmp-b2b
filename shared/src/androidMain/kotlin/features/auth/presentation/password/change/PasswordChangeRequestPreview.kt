package features.auth.presentation.password.change

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Preview(
    showBackground = true,
    device = "spec:width=411dp,height=891dp"
)
@Composable
fun PasswordChangeRequestPreview() {
    PasswordChangeRequestContent(
        state = PasswordChangeRequestState(newPassword = "123", isLoading = false),
        onPasswordChange = {},
        onSubmit = {},
        snackbarHostState = remember { SnackbarHostState() },
        onBackClick = {},
        showCurrentPassword = false,
        onCurrentPasswordChange = {},
        onConfirmPasswordChange = {},
        modifier = Modifier,
        onToggleShowCurrentPassword = {},
        onToggleShowNewPassword = {},
        onToggleShowConfirmPassword = {},
    )
}
