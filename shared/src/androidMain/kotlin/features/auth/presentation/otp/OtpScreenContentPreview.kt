package features.auth.presentation.otp

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.icerock.moko.resources.StringResource

@Preview(showBackground = true, device = "spec:width=411dp,height=891dp")
@Composable
fun OtpScreenContentPreview() {

    OtpFormContent(
        otp = "1234",
        onOtpChange = { },
        otpError = null,
        isLoading = false,
        confirmButtonText = "Подтвердить",
        onConfirm = {},
        maskedPhoneNumber = "Мы отправили код на номер +998 90 *** ** 31. Введите его",
        modifier = Modifier
    )
}