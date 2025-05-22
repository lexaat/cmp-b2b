package features.auth.presentation.login

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import features.auth.presentation.login_otp.OtpScreen
import features.auth.presentation.screens.PasswordChangeScreen
import features.common.ui.collectInLaunchedEffect
import features.main.presentation.MainScreen
import org.koin.compose.koinInject
import ui.components.ScreenWrapper

object LoginScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel = koinInject<LoginViewModel>()
        ScreenWrapper {
            LoginScreenContent(viewModel = viewModel)
        }
    }
}

@Composable
fun LoginScreenContent(viewModel: LoginViewModel) {
    val isLoading by viewModel.isLoading.collectAsState()
    val navigator = LocalNavigator.currentOrThrow
    val snackbarHostState = remember { SnackbarHostState() }

    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    viewModel.sideEffect.collectInLaunchedEffect { effect ->
        when (effect) {
            is AuthSideEffect.ShowError -> snackbarHostState.showSnackbar(effect.message)
            is AuthSideEffect.NavigateToMain -> navigator.push(MainScreen)
            is AuthSideEffect.NavigateToOtp -> navigator.push(OtpScreen(login, password))
            is AuthSideEffect.NavigateToPasswordChange -> navigator.push(PasswordChangeScreen(login, password))
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { paddingValues ->
        LoginForm(
            isLoading = isLoading,
            onSubmit = { l, p ->
                login = l
                password = p
                viewModel.dispatch(LoginIntent.SubmitCredentials(l, p))
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
    Column(modifier.padding(16.dp)) {
        OutlinedTextField(value = login, onValueChange = { login = it }, label = { Text("Логин") })
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Пароль") })
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
                Text("Войти")
            }
        }
    }
}