package features.auth.presentation.password.change

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import core.error.GlobalErrorHandler
import dev.icerock.moko.resources.compose.localized
import dev.icerock.moko.resources.compose.painterResource
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
    private val currentPassword: String? = null
) : Screen {
    @Composable
    override fun Content() {
        val viewModel = koinInject<PasswordChangeRequestViewModel> {
            parametersOf(
                login,
                currentPassword ?: ""
            )
        }

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
            }
        }

        ScreenWrapper {
            PasswordChangeRequestContent(
                state = state,
                showCurrentPassword = currentPassword == null,
                onCurrentPasswordChange = {
                    viewModel.processIntent(PasswordChangeRequestIntent.CurrentPasswordChanged(it))
                },
                onPasswordChange = {
                    viewModel.processIntent(
                        PasswordChangeRequestIntent.NewPasswordChanged(it)
                    )
                },
                onConfirmPasswordChange = {
                    viewModel.processIntent(PasswordChangeRequestIntent.ConfirmPasswordChanged(it))
                },
                onSubmit = {
                    viewModel.processIntent(PasswordChangeRequestIntent.SubmitClicked)
                },
                snackbarHostState = snackbarHostState,
                onBackClick = { navigator.pop() },
                onToggleShowCurrentPassword = {viewModel.processIntent(PasswordChangeRequestIntent.ToggleShowCurrentPassword)},
                onToggleShowNewPassword = {viewModel.processIntent(PasswordChangeRequestIntent.ToggleShowNewPassword)},
                onToggleShowConfirmPassword = {viewModel.processIntent(PasswordChangeRequestIntent.ToggleShowConfirmPassword)},
            )
        }
    }
}

@Composable
fun PasswordChangeRequestContent(
    state: PasswordChangeRequestState,
    showCurrentPassword: Boolean,
    onCurrentPasswordChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onSubmit: () -> Unit,
    snackbarHostState: SnackbarHostState,
    onBackClick: () -> Unit,
    onToggleShowCurrentPassword: () -> Unit,
    onToggleShowNewPassword: () -> Unit,
    onToggleShowConfirmPassword: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            AppTopBar(
                title = if (showCurrentPassword) {
                    stringResource(
                        SharedRes.strings.password_change
                    )
                } else "", onBackClick = onBackClick, centered = true
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        content = { paddingValues ->
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
                            text = if (!showCurrentPassword) {
                                stringResource(
                                    SharedRes.strings.password_expired
                                )
                            } else "",
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
                        if (showCurrentPassword) {
                            OutlinedTextField(
                                value = state.currentPassword,
                                onValueChange = onCurrentPasswordChange,
                                label = { Text(stringResource(SharedRes.strings.current_password)) },
                                singleLine = true,
                                visualTransformation = if (state.showCurrentPassword) VisualTransformation.None else PasswordVisualTransformation(),
                                trailingIcon = {
                                    IconButton(onClick = onToggleShowCurrentPassword) {
                                        Icon(
                                            imageVector = if (state.showCurrentPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                            contentDescription = null
                                        )
                                    }
                                },
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.widthIn(max = 400.dp).fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                        }

                        OutlinedTextField(
                            value = state.newPassword,
                            onValueChange = onPasswordChange,
                            label = { Text(stringResource(SharedRes.strings.new_password)) },
                            singleLine = true,
                            visualTransformation = if (state.showNewPassword) VisualTransformation.None else PasswordVisualTransformation(),
                            trailingIcon = {
                                IconButton(onClick = onToggleShowNewPassword) {
                                    Icon(
                                        imageVector = if (state.showNewPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                        contentDescription = null
                                    )
                                }
                            },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.widthIn(max = 400.dp).fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = state.confirmPassword,
                            onValueChange = onConfirmPasswordChange,
                            label = { Text(stringResource(SharedRes.strings.repeat_password)) },
                            singleLine = true,
                            visualTransformation = if (state.showConfirmPassword) VisualTransformation.None else PasswordVisualTransformation(),
                            trailingIcon = {
                                IconButton(onClick = onToggleShowConfirmPassword) {
                                    Icon(
                                        imageVector = if (state.showConfirmPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                        contentDescription = null
                                    )
                                }
                            },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.widthIn(max = 400.dp).fillMaxWidth()
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
                            enabled = state.newPassword.length >= 6 && state.newPassword == state.confirmPassword,
                            contentColor = MaterialTheme.colorScheme.onSurface,
                            showBorder = true
                        )
                    }
                }
            }
        }
    )
}