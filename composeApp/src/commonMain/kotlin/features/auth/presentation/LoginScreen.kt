package features.auth.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
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
import core.ui.collectInLaunchedEffect
import features.main.presentation.MainScreen
import org.koin.compose.koinInject

object LoginScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel = koinInject<AuthViewModel>()
        LoginScreenContent(viewModel = viewModel)

    }
}

@Composable
fun LoginScreenContent(viewModel: AuthViewModel) {
    val state by viewModel.state.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val navigator = LocalNavigator.currentOrThrow
    val snackbarHostState = remember { SnackbarHostState() }

    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    viewModel.sideEffect.collectInLaunchedEffect { effect ->
        when (effect) {
            is AuthSideEffect.ShowSnackbar -> snackbarHostState.showSnackbar(effect.message)
            is AuthSideEffect.NavigateToHome -> navigator.push(MainScreen)
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (state) {
                is AuthState.EnterCredentials -> LoginForm(
                    isLoading = isLoading,
                    onSubmit = { l, p ->
                        login = l
                        password = p
                        viewModel.process(AuthIntent.SubmitCredentials(l, p))
                    },
                    modifier = Modifier.padding(paddingValues)
                )

                is AuthState.WaitingForOtp -> OtpForm {
                    viewModel.process(AuthIntent.SubmitOtp(login, password, it))
                }

                is AuthState.RequirePasswordChange -> PasswordChangeForm {
                    viewModel.process(AuthIntent.SubmitNewPassword(it))
                }

                is AuthState.WaitingForPasswordOtp -> PasswordOtpForm {
                    viewModel.process(AuthIntent.SubmitPasswordOtp("dummy", it))
                }

                is AuthState.PasswordChanged -> Text("Пароль изменён. Авторизуйтесь заново.")
                else -> {}
            }
        }
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

@Composable
fun OtpForm(onSubmit: (String) -> Unit) {
    var otp by remember { mutableStateOf("") }
    Column(Modifier.padding(16.dp)) {
        OutlinedTextField(value = otp, onValueChange = { otp = it }, label = { Text("Код из СМС") })
        Button(onClick = { onSubmit(otp) }, modifier = Modifier.padding(top = 8.dp)) {
            Text("Подтвердить")
        }
    }
}

@Composable
fun PasswordChangeForm(onSubmit: (String) -> Unit) {
    var newPass by remember { mutableStateOf("") }
    Column(Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = newPass,
            onValueChange = { newPass = it },
            label = { Text("Новый пароль") })
        Button(onClick = { onSubmit(newPass) }, modifier = Modifier.padding(top = 8.dp)) {
            Text("Продолжить")
        }
    }
}

@Composable
fun PasswordOtpForm(onSubmit: (String) -> Unit) {
    var otp by remember { mutableStateOf("") }
    Column(Modifier.padding(16.dp)) {
        OutlinedTextField(value = otp, onValueChange = { otp = it }, label = { Text("Код из СМС") })
        Button(onClick = { onSubmit(otp) }, modifier = Modifier.padding(top = 8.dp)) {
            Text("Сменить пароль")
        }
    }
}