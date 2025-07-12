// features/auth/presentation/otp/OtpScreen.kt

package features.auth.presentation.otp

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import core.error.GlobalErrorHandler
import core.i18n.LocaleController
import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.compose.localized
import dev.icerock.moko.resources.compose.painterResource
import dev.icerock.moko.resources.compose.stringResource
import dev.icerock.moko.resources.desc.desc
import features.auth.presentation.password.change.ChangePasswordScreen
import features.common.ui.collectInLaunchedEffect
import features.main.presentation.MainScreen
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf
import ui.components.AppTopBar
import ui.components.ButtonWithLoader
import ui.components.ScreenWrapper
import uz.hb.shared.SharedRes

data class OtpScreen(val login: String,
                     val password: String,
                     val maskedPhoneNumber: String) : Screen {
    @Composable
    override fun Content() {
        val viewModel = koinInject<OtpViewModel> {
            parametersOf(login, password)
        }

        ScreenWrapper {
            OtpScreenContent(viewModel = viewModel, maskedPhoneNumber = maskedPhoneNumber)
        }
    }
}

@Composable
fun OtpScreenContent(viewModel: OtpViewModel,
                     maskedPhoneNumber: String) {
    val navigator = LocalNavigator.currentOrThrow
    val snackbarHostState = remember { SnackbarHostState() }
    val uiState by viewModel.uiState.collectAsState()

    val globalErrorHandler = koinInject<GlobalErrorHandler>(
        parameters = { parametersOf(navigator) }
    )

    viewModel.sideEffect.collectInLaunchedEffect { effect ->
        globalErrorHandler.handle(effect)
        when (effect) {
            is OtpSideEffect.ShowError -> snackbarHostState.showSnackbar(effect.message)
            is OtpSideEffect.NavigateToMain -> navigator.push(MainScreen)
            is OtpSideEffect.NavigateToPasswordChange -> navigator.push(ChangePasswordScreen(viewModel.login, viewModel.password))
            OtpSideEffect.NavigateBack,
            OtpSideEffect.SessionExpired -> Unit
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = SharedRes.strings.confirmation.desc().localized(),
                onBackClick = { navigator.pop() },
                centered = true,
                menuItems = emptyList(),
            )
        },
        contentWindowInsets = WindowInsets.systemBars,
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        OtpForm(
            state = uiState,
            maskedPhoneNumber = maskedPhoneNumber,
            onOtpChange = { viewModel.processIntent(OtpIntent.OtpInputChanged(it)) },
            onConfirm = { viewModel.processIntent(OtpIntent.ConfirmButtonClicked) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
        )
    }
}

@Composable
fun OtpForm(
    state: OtpState,
    maskedPhoneNumber: String,
    onOtpChange: (String) -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier
) {
    val locale by LocaleController.locale.collectAsState()

    key(locale) {
        OtpFormContent(
            otp = state.otpInput,
            onOtpChange = onOtpChange,
            otpError = state.otpError,
            isLoading = state.isLoading,
            confirmButtonText = SharedRes.strings.confirm.desc().localized(),
            onConfirm = onConfirm,
            maskedPhoneNumber = maskedPhoneNumber,
            modifier = modifier
        )
    }
}

@Composable
fun OtpFormContent(
    otp: String,
    onOtpChange: (String) -> Unit,
    otpError: StringResource?,
    isLoading: Boolean,
    confirmButtonText: String,
    onConfirm: () -> Unit,
    maskedPhoneNumber: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(2f)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(modifier = Modifier.fillMaxWidth().weight(1f))

                Image(
                    painter = painterResource(SharedRes.images.logo_hayot_bank),
                    contentDescription = null,
                    modifier = Modifier.size(width = 240.dp, height = 80.dp),
                    contentScale = ContentScale.Fit
                )

                Text(
                    text = stringResource(SharedRes.strings.otp_sent_to_phone, maskedPhoneNumber),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth() // Заполнить ширину
                        .weight(1f)
                )
            }
        }

        OutlinedTextField(
            value = otp,
            onValueChange = onOtpChange,
            label = { Text(stringResource(SharedRes.strings.sms_code)) },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .widthIn(max = 400.dp)
                .fillMaxWidth()
        )

        otpError?.let {
            Text(
                text = stringResource(it),
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        Box(modifier = Modifier.fillMaxWidth().weight(1f))

        ButtonWithLoader(
            onClick = onConfirm,
            buttonText = confirmButtonText,
            contentColor = MaterialTheme.colorScheme.onSurface,
            showBorder = true,
            showLoader = isLoading,
            enabled = otp.length >= 4
        )
    }
}
