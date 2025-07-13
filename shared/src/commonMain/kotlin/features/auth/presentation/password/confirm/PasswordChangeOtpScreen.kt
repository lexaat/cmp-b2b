package features.auth.presentation.password.confirm

import androidx.compose.runtime.*
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import core.error.GlobalErrorHandler
import dev.icerock.moko.resources.compose.localized
import dev.icerock.moko.resources.compose.stringResource
import dev.icerock.moko.resources.desc.desc
import features.common.ui.collectInLaunchedEffect
import features.main.presentation.MainScreen
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf
import ui.components.AppTopBar
import ui.components.ButtonWithLoader
import ui.components.ScreenWrapper
import uz.hb.shared.SharedRes
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import dev.icerock.moko.resources.compose.painterResource
import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.text.style.TextAlign

class PasswordChangeOtpScreen(
    private val login: String,
    private val newPassword: String,
    private val oldPassword: String,
) : Screen {
    @Composable
    override fun Content() {
        val viewModel = koinInject<PasswordChangeOtpViewModel> {
            parametersOf(login, newPassword, oldPassword)
        }
        val state by viewModel.uiState.collectAsState()
        val navigator = LocalNavigator.currentOrThrow
        val snackbarHostState = remember { SnackbarHostState() }

        val globalErrorHandler = koinInject<GlobalErrorHandler> {
            parametersOf(navigator)
        }

        viewModel.sideEffect.collectInLaunchedEffect { effect ->
            globalErrorHandler.handle(effect)
            when (effect) {
                is PasswordChangeOtpSideEffect.ShowError ->
                    snackbarHostState.showSnackbar(effect.message)

                is PasswordChangeOtpSideEffect.PasswordChanged ->
                    navigator.push(MainScreen)

                PasswordChangeOtpSideEffect.NavigateBack -> navigator.pop()
                PasswordChangeOtpSideEffect.SessionExpired -> {} // можно добавить отдельную обработку
            }
        }

        ScreenWrapper {
            ChangePasswordOtpContent(
                state = state,
                onOtpChange = { viewModel.processIntent(PasswordChangeOtpIntent.OtpInputChanged(it)) },
                onConfirm = { viewModel.processIntent(PasswordChangeOtpIntent.ConfirmButtonClicked) },
                snackbarHostState = snackbarHostState,
                onBackClick = { navigator.pop() }
            )
        }
    }
}

@Composable
fun ChangePasswordOtpContent(
    state: PasswordChangeOtpState,
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
                            SharedRes.strings.otp_sent_to_phone
                        ),
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.padding(horizontal = 12.dp).height(100.dp)
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
                        modifier = Modifier.widthIn(max = 400.dp).fillMaxWidth()
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
