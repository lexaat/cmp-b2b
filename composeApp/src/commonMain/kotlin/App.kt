
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.Navigator
import features.auth.presentation.LoginScreen
import org.koin.compose.KoinContext

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface

@Composable
fun App() {

    MaterialTheme {
        Surface {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(WindowInsets.safeDrawing.asPaddingValues())
                    .background(MaterialTheme.colorScheme.background)
            ) {
                KoinContext {
                    Navigator(screen = LoginScreen)
                }
            }
        }
    }
}
