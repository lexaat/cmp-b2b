// features/auth/presentation/screens/OtpScreen.kt

package features.auth.presentation.otp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import dev.icerock.moko.resources.compose.localized
import dev.icerock.moko.resources.compose.stringResource
import dev.icerock.moko.resources.desc.desc
import features.auth.presentation.login.AuthState
import features.auth.presentation.password.change.ChangePasswordScreen
import features.common.ui.collectInLaunchedEffect
import features.main.presentation.MainScreen
import org.koin.compose.koinInject
import ui.components.AppTopBar
import ui.components.ButtonWithLoader
import ui.components.ScreenWrapper
import uz.hb.shared.SharedRes

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
            onBackClick = {
                navigator.pop()
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
    onBackClick: () -> Unit,
    snackbarHostState: SnackbarHostState,
    isLoading: Boolean,
    modifier: Modifier = Modifier
) {

    Scaffold(
        topBar = {
            AppTopBar(
                title = SharedRes.strings.confirmation.desc().localized(),
                onBackClick = onBackClick,
                centered = true, // 👈 включаем iOS-стиль
                menuItems = emptyList(),
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }) { paddingValues ->
        ScreenWrapper(modifier = modifier.padding(paddingValues)) {
            Column(Modifier.fillMaxWidth().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                OutlinedTextField(
                    value = otp,
                    onValueChange = onOtpChange,
                    modifier = Modifier
                        .widthIn(max = 600.dp)
                        .fillMaxWidth(),
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


