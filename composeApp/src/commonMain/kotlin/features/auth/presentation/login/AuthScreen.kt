package features.auth.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import core.error.GlobalErrorHandler
import core.i18n.LocaleController
import dev.icerock.moko.resources.compose.localized
import dev.icerock.moko.resources.desc.desc
import features.auth.presentation.login.AuthIntent
import features.auth.presentation.login.AuthSideEffect
import features.auth.presentation.login.AuthState
import features.auth.presentation.login.AuthViewModel
import features.auth.presentation.otp.OtpScreen
import features.common.ui.collectInLaunchedEffect
import features.main.presentation.MainScreen
import org.koin.compose.koinInject
import ui.components.AppTopBar
import ui.components.ButtonWithLoader
import ui.components.LanguageSelector
import ui.components.ScreenWrapper
import uz.hb.b2b.SharedRes

object AuthScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel = koinInject<AuthViewModel>()
        ScreenWrapper {
            LoginScreenContent(viewModel = viewModel)
        }
    }
}

@Composable
fun LoginScreenContent(viewModel: AuthViewModel) {
    val navigator = LocalNavigator.currentOrThrow
    val snackbarHostState = remember { SnackbarHostState() }

    val state by viewModel.state.collectAsState()
    val isLoading = state is AuthState.Loading

    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Инъекция с параметрами!
    val globalErrorHandler = koinInject<GlobalErrorHandler>(
        parameters = { org.koin.core.parameter.parametersOf(navigator) }
    )

    viewModel.sideEffect.collectInLaunchedEffect { effect ->
        // 1. Делегируем глобальные сайд-эффекты
        globalErrorHandler.handle(effect)

        // 2. Фичевые эффекты
        when (effect) {
            is AuthSideEffect.NavigateToMain -> navigator.push(MainScreen)
            is AuthSideEffect.NavigateToOtp -> navigator.push(OtpScreen(login, password))

            AuthSideEffect.NavigateBack -> { /* обработано глобально */ }
            AuthSideEffect.SessionExpired -> { /* обработано глобально */ }
            is AuthSideEffect.ShowError -> {
                snackbarHostState.showSnackbar(effect.message)
            }
        }
    }

    val menuItems = listOf(
        Triple("Настройки", Icons.Default.Settings) { /* navigator.push(SettingsScreen()) */ },
        Triple("О приложении", Icons.Default.Info) { /* showAboutDialog() */ },
        Triple("Выход", Icons.AutoMirrored.Filled.ExitToApp) { /* logout() */ }
    )

    Scaffold(
        contentWindowInsets = WindowInsets.systemBars,
        topBar = {
                AppTopBar(
                    title = SharedRes.strings.authorization.desc().localized(),
                    onBackClick = null,
                    centered = true, // 👈 включаем iOS-стиль
                    menuItems = menuItems
                )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }) { paddingValues ->
        LoginForm(
            isLoading = isLoading,
            onSubmit = { l, p ->
                login = l
                password = p
                viewModel.reduce(AuthIntent.SubmitCredentials(l, p))
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
        )
    }
}

@Composable
fun LoginForm(
    isLoading: Boolean,
    onSubmit: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val viewModel = koinInject<AuthViewModel>()
    val canUseBiometrics by viewModel.canUseBiometrics.collectAsState()

    val locale by LocaleController.locale.collectAsState()

    key(locale) {
        LoginFormContent(
            login = login,
            password = password,
            onLoginChange = { login = it },
            onPasswordChange = { password = it },
            isLoading = isLoading,
            canUseBiometrics = canUseBiometrics,
            onSubmit = { onSubmit(login, password) },
            onBiometricLogin = { viewModel.loginWithBiometrics() },
            modifier = modifier,
            loginLabel = SharedRes.strings.login.desc().localized(),
            passwordLabel = SharedRes.strings.password.desc().localized(),
            loginButtonText = SharedRes.strings.login.desc().localized()
        )
    }
}

@Composable
fun LoginFormContent(
    login: String,
    password: String,
    onLoginChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    isLoading: Boolean,
    canUseBiometrics: Boolean,
    onSubmit: () -> Unit,
    onBiometricLogin: () -> Unit,
    modifier: Modifier = Modifier,
    loginLabel: String,
    passwordLabel: String,
    loginButtonText: String
) {
    Column(
        modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = login,
            onValueChange = onLoginChange,
            label = { Text(loginLabel) },
            modifier = Modifier.widthIn(max = 600.dp).fillMaxWidth()
        )
        OutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            label = { Text(passwordLabel) },
            modifier = Modifier.widthIn(max = 600.dp).fillMaxWidth()
        )
        ButtonWithLoader(
            onClick = onSubmit,
            buttonText = loginButtonText,
            contentColor = MaterialTheme.colorScheme.onSurface,
            showBorder = true,
            showLoader = isLoading
        )

        if (canUseBiometrics) {
            OutlinedButton(
                onClick = onBiometricLogin,

                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text("Войти по биометрии")
            }
        }

        LanguageSelector()
    }
}
