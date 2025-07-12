package features.auth.presentation.password.change

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun PasswordChangeRequestPreview() {
    PasswordChangeRequestContent(
        state = PasswordChangeRequestState(newPassword = "", isLoading = false),
        onPasswordChange = {},
        onSubmit = {},
        snackbarHostState = remember { SnackbarHostState() },
        onBackClick = {}
    )
}
