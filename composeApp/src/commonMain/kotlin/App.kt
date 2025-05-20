
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.Navigator
import features.auth.presentation.LoginScreen
import org.koin.compose.KoinContext

@Composable
fun App() {

    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .background(MaterialTheme.colors.background)
        ) {
            KoinContext {
                Navigator(screen = LoginScreen)
            }
        }
    }
}
