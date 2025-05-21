import androidx.compose.ui.window.ComposeUIViewController
import app.App
import di.initKoinIOS

fun MainViewController() = ComposeUIViewController(
    configure = {
         initKoinIOS()
    }
) {  App() }