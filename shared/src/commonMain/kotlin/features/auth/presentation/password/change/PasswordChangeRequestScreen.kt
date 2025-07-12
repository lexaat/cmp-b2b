package features.auth.presentation.password.change

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import core.error.GlobalErrorHandler
import dev.icerock.moko.resources.compose.localized
import dev.icerock.moko.resources.compose.stringResource
import dev.icerock.moko.resources.desc.desc
import features.auth.presentation.password.confirm.PasswordOtpScreen
import features.common.ui.collectInLaunchedEffect
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf
import ui.components.AppTopBar
import ui.components.ButtonWithLoader
import ui.components.ScreenWrapper
import uz.hb.shared.SharedRes


class PasswordChangeRequestScreen(
    private val login: String,
    private val password: String,
) : Screen {
    @Composable
    override fun Content() {
        val viewModel = koinInject<PasswordChangeRequestViewModel> { parametersOf(login, password) }

        val state by viewModel.uiState.collectAsState()
        val snackbarHostState = remember { SnackbarHostState() }
        val navigator = LocalNavigator.currentOrThrow

        val globalErrorHandler = koinInject<GlobalErrorHandler> {
            parametersOf(navigator)
        }

        viewModel.sideEffect.collectInLaunchedEffect { effect ->
            globalErrorHandler.handle(effect)
            when (effect) {
                is PasswordChangeRequestSideEffect.NavigateToOtp ->
                    navigator.push(PasswordOtpScreen(login, state.newPassword, effect.maskedPhone))
                is PasswordChangeRequestSideEffect.ShowError ->
                    snackbarHostState.showSnackbar(effect.message)
                else -> {}
            }
        }

        ScreenWrapper {
            PasswordChangeRequestContent(
                state = state,
                onPasswordChange = {
                    viewModel.processIntent(PasswordChangeRequestIntent.PasswordChanged(it))
                },
                onSubmit = {
                    viewModel.processIntent(PasswordChangeRequestIntent.SubmitClicked)
                },
                snackbarHostState = snackbarHostState,
                onBackClick = { navigator.pop() }
            )
        }
    }
}

@Composable
fun PasswordChangeRequestContent(
    state: PasswordChangeRequestState,
    onPasswordChange: (String) -> Unit,
    onSubmit: () -> Unit,
    snackbarHostState: SnackbarHostState,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            AppTopBar(title = "", onBackClick = onBackClick, centered = true)
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        content = { paddingValues ->
            Column(
                modifier = modifier
                    .padding(paddingValues)
                    .padding(16.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.weight(1f))

                OutlinedTextField(
                    value = state.newPassword,
                    onValueChange = onPasswordChange,
                    label = { Text(stringResource(SharedRes.strings.new_password)) },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .widthIn(max = 400.dp)
                        .fillMaxWidth()
                )

                state.passwordError?.let {
                    Text(
                        text = stringResource(it),
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                ButtonWithLoader(
                    onClick = onSubmit,
                    buttonText = SharedRes.strings.to_continue.desc().localized(),
                    showLoader = state.isLoading,
                    enabled = state.newPassword.length >= 6,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                    showBorder = true
                )
            }
        }
    )
}
