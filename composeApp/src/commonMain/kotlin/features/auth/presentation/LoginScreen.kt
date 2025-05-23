package features.auth.presentation

import androidx.compose.foundation.layout.Box
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
import dev.icerock.moko.resources.compose.stringResource
import features.common.ui.collectInLaunchedEffect
import features.main.presentation.MainScreen
import org.koin.compose.koinInject
import ui.components.ScreenWrapper
import uz.hb.b2b.SharedRes

object LoginScreen : Screen {
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
    val state by viewModel.state.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val navigator = LocalNavigator.currentOrThrow
    val snackbarHostState = remember { SnackbarHostState() }

    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    viewModel.sideEffect.collectInLaunchedEffect { effect ->
        when (effect) {
            is AuthSideEffect.ShowError -> snackbarHostState.showSnackbar(effect.message)
            is AuthSideEffect.NavigateToMain -> navigator.push(MainScreen)
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
                        viewModel.dispatch(AuthIntent.SubmitCredentials(l, p))
                    },
                    modifier = Modifier.padding(paddingValues)
                )

                is AuthState.WaitingForOtp -> OtpForm {
                    viewModel.dispatch(AuthIntent.SubmitOtp(login, password, it))
                }

                is AuthState.RequirePasswordChange -> PasswordChangeForm {
                    viewModel.dispatch(AuthIntent.SubmitNewPassword(login, password, it))
                }

                is AuthState.WaitingForPasswordOtp -> PasswordOtpForm {
                    viewModel.dispatch(AuthIntent.SubmitPasswordOtp(login, password, "", it))
                }

                is AuthState.PasswordChanged -> Text(stringResource(SharedRes.strings.login))
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
        OutlinedTextField(value = login, onValueChange = { login = it }, label = { Text(stringResource(SharedRes.strings.login)) })
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(stringResource(SharedRes.strings.password)) })
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
                Text(stringResource(SharedRes.strings.enter))
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
            Text(stringResource(SharedRes.strings.approve))
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