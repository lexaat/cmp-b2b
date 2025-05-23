// features/auth/presentation/screens/OtpScreen.kt

package features.auth.presentation.login_otp

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import features.common.ui.collectInLaunchedEffect
import features.main.presentation.MainScreen
import org.koin.compose.koinInject
import ui.components.ScreenWrapper

data class OtpScreen(val login: String, val password: String) : Screen {
    @Composable
    override fun Content() {
        val viewModel = koinInject<OtpViewModel>()
        val navigator = LocalNavigator.currentOrThrow
        var otp by remember { mutableStateOf("") }

        val snackbarHostState = remember { SnackbarHostState() }

//        // 🛑 Перехватываем кнопку "назад"
//        BackHandler {
//            viewModel.dispatch(OtpIntent.ClearState) // сбрасываем стейт (если нужно)
//            navigator.pop() // возвращаемся назад
//        }

        viewModel.sideEffect.collectInLaunchedEffect { effect ->
            when (effect) {
                is OtpSideEffect.ShowError -> snackbarHostState.showSnackbar(effect.message)
                is OtpSideEffect.NavigateToMain -> navigator.push(MainScreen)
            }
        }

        Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { paddingValues ->
            ScreenWrapper {
                Column(Modifier.padding(16.dp)) {
                    OutlinedTextField(value = otp, onValueChange = { otp = it }, label = { Text("Код из СМС") })
                    Button(onClick = {
                        viewModel.dispatch(OtpIntent.SubmitOtp(login, password, otp))
                    }, modifier = Modifier.padding(top = 8.dp)) {
                        Text("Подтвердить")
                    }
                }
            }
        }
    }
}
