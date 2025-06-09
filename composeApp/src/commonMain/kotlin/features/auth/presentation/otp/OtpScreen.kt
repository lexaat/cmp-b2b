// features/auth/presentation/screens/OtpScreen.kt

package features.auth.presentation.otp

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import dev.icerock.moko.resources.compose.stringResource
import features.auth.presentation.login.AuthState
import features.auth.presentation.password.change.ChangePasswordScreen
import features.common.ui.collectInLaunchedEffect
import features.main.presentation.MainScreen
import org.koin.compose.koinInject
import ui.components.ButtonWithLoader
import ui.components.ScreenWrapper
import uz.hb.b2b.SharedRes

data class OtpScreen(val login: String, val password: String) : Screen {
    @Composable
    override fun Content() {
        val viewModel = koinInject<OtpViewModel>()
        val navigator = LocalNavigator.currentOrThrow
        var otp by remember { mutableStateOf("") }

        val snackbarHostState = remember { SnackbarHostState() }

        val state by viewModel.state.collectAsState()
        val isLoading = state is OtpState.Loading

        viewModel.sideEffect.collectInLaunchedEffect { effect ->
            when (effect) {
                is OtpSideEffect.ShowError -> snackbarHostState.showSnackbar(effect.message)
                is OtpSideEffect.NavigateToMain -> navigator.push(MainScreen)
                OtpSideEffect.NavigateBack -> { /* обработано глобально */ }
                OtpSideEffect.SessionExpired -> { /* обработано глобально */ }
                OtpSideEffect.NavigateToPasswordChange -> navigator.push(
                    ChangePasswordScreen(
                        login,
                        password
                    )
                )
            }
        }

        OtpScreenContent(
            otp = otp,
            onOtpChange = { otp = it },
            onSubmitClick = {
                viewModel.reduce(OtpIntent.SubmitOtp(login, password, otp))
            },
            snackbarHostState = snackbarHostState,
            isLoading = isLoading,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun OtpScreenContent(
    otp: String,
    onOtpChange: (String) -> Unit,
    onSubmitClick: () -> Unit,
    snackbarHostState: SnackbarHostState,
    isLoading: Boolean,
    modifier: Modifier = Modifier
) {

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    )
    { paddingValues ->
        ScreenWrapper(modifier = modifier.padding(paddingValues)) {
            Column(Modifier.padding(16.dp)) {
                OutlinedTextField(
                    value = otp,
                    onValueChange = onOtpChange,
                    label = { Text(stringResource(SharedRes.strings.sms_code)) }
                )
                ButtonWithLoader(
                    onClick = onSubmitClick,
                    buttonText = stringResource(SharedRes.strings.confirm),
                    backgroundColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                    showBorder = true,
                    showLoader = isLoading
                )
            }
        }
    }
}


