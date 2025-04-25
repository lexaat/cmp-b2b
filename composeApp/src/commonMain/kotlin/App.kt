
import androidx.compose.runtime.Composable
import org.koin.compose.KoinContext
import org.koin.compose.koinInject
import features.auth.presentation.LoginScreen
import features.auth.presentation.AuthViewModel

@Composable
fun App() {
    KoinContext {
        val viewModel = koinInject<AuthViewModel>()
        LoginScreen(viewModel = viewModel)
    }
}
