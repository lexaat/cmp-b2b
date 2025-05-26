package app

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.Navigator
import org.koin.compose.getKoin

@Composable
fun AppRoot(
    appViewModel: AppViewModel = getKoin().get()
) {
    val startScreen by appViewModel.startScreen.collectAsState()

    if (startScreen != null) {
        // если нужно, передай themeViewModel дальше
        Navigator(screen = startScreen!!)
    } else {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
}