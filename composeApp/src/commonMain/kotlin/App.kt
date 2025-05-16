
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import cafe.adriel.voyager.navigator.Navigator
import org.koin.compose.KoinContext
import org.koin.compose.koinInject
import features.auth.presentation.LoginScreen
import features.auth.presentation.AuthViewModel

@Composable
fun App() {

    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(WindowInsets.safeDrawing.asPaddingValues()) // Учитывает скругления и вырезы
                .background(MaterialTheme.colors.background)
        ) {
            KoinContext {
                Navigator(screen = LoginScreen)
            }
        }
    }
}
