package features.auth.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import features.auth.presentation.password.change.PasswordChangeScreen
import features.common.ui.collectInLaunchedEffect
import features.main.presentation.MainScreen
import org.koin.compose.koinInject
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

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { paddingValues ->
        LoginForm(
            isLoading = isLoading,
            onSubmit = { l, p ->
                login = l
                password = p
                viewModel.reduce(AuthIntent.SubmitCredentials(l, p))
            },
            modifier = Modifier.padding(paddingValues)
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

    Column(modifier.padding(16.dp)) {
        key(locale) {
        OutlinedTextField(
            value = login,
            onValueChange = { login = it },
            label = { Text(SharedRes.strings.login.desc().localized()) }
        )
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(SharedRes.strings.password.desc().localized()) }
        )
        Button(
            onClick = { onSubmit(login, password) },
            enabled = !isLoading,
            modifier = Modifier.padding(top = 8.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            } else {
                Text(SharedRes.strings.login.desc().localized())
            }
        }}

        if (canUseBiometrics) {
            Button(
                onClick = { viewModel.loginWithBiometrics() },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text("Войти по биометрии") // можно использовать локализованную строку
            }
        }

        LanguageSelector()
    }
}

