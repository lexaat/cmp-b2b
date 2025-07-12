// features/auth/presentation/otp/OtpScreen.kt

package features.auth.presentation.otp

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.text.style.TextAlign
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
import features.auth.presentation.password.change.PasswordChangeRequestScreen
import features.common.ui.collectInLaunchedEffect
import features.main.presentation.MainScreen
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf
import ui.components.AppTopBar
import ui.components.ButtonWithLoader
import ui.components.ScreenWrapper
import uz.hb.shared.SharedRes

data class OtpScreen(
    val login: String,
    val password: String,
    val maskedPhoneNumber: String
) : Screen {
    @Composable
    override fun Content() {
        val viewModel = koinInject<OtpViewModel> { parametersOf(login, password) }
        val state by viewModel.uiState.collectAsState()
        val navigator = LocalNavigator.currentOrThrow
        val snackbarHostState = remember { SnackbarHostState() }

        val globalErrorHandler = koinInject<GlobalErrorHandler>(
            parameters = { parametersOf(navigator) }
        )

        viewModel.sideEffect.collectInLaunchedEffect { effect ->
            globalErrorHandler.handle(effect)
            when (effect) {
                is OtpSideEffect.ShowError -> snackbarHostState.showSnackbar(effect.message)
                is OtpSideEffect.NavigateToMain -> navigator.push(MainScreen)
                is OtpSideEffect.NavigateToPasswordChange ->
                    navigator.push(PasswordChangeRequestScreen(login, password))

                else -> {}
            }
        }
        ScreenWrapper {
            OtpScreenContent(
                state = state,
                maskedPhoneNumber = maskedPhoneNumber,
                onOtpChange = { viewModel.processIntent(OtpIntent.OtpInputChanged(it)) },
                onConfirm = { viewModel.processIntent(OtpIntent.ConfirmButtonClicked) },
                snackbarHostState = snackbarHostState,
                onBackClick = { navigator.pop() }
            )
        }
    }
}

@Composable
fun OtpScreenContent(
    state: OtpState,
    maskedPhoneNumber: String,
    onOtpChange: (String) -> Unit,
    onConfirm: () -> Unit,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    onBackClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            AppTopBar(
                title = "",
                onBackClick = onBackClick,
                centered = true,
                menuItems = emptyList()
            )
        },
        contentWindowInsets = WindowInsets.systemBars,
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(modifier = Modifier.fillMaxWidth().weight(1f)) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.weight(1f))

                    Image(
                        painter = painterResource(SharedRes.images.logo_hayot_bank),
                        contentDescription = null,
                        modifier = Modifier.size(width = 240.dp, height = 80.dp),
                        contentScale = ContentScale.Fit
                    )

                    Text(
                        text = stringResource(
                            SharedRes.strings.otp_sent_to_phone,
                            maskedPhoneNumber
                        ),
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.padding(horizontal = 12.dp)
                            .height(100.dp)
                    )
                }
            }
            Box(modifier = Modifier.fillMaxWidth().weight(1f)) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OutlinedTextField(
                        value = state.otpInput,
                        onValueChange = onOtpChange,
                        label = { Text(stringResource(SharedRes.strings.sms_code)) },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .widthIn(max = 400.dp)
                            .fillMaxWidth()
                    )

                    state.otpError?.let {
                        Text(
                            text = stringResource(it),
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    ButtonWithLoader(
                        onClick = onConfirm,
                        buttonText = SharedRes.strings.confirm.desc().localized(),
                        contentColor = MaterialTheme.colorScheme.onSurface,
                        showBorder = true,
                        showLoader = state.isLoading,
                        enabled = state.otpInput.length >= 4
                    )
                }
            }
        }
    }
}

