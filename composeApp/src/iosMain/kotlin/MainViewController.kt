import androidx.compose.ui.window.ComposeUIViewController
import di.initKoinIOS

fun MainViewController() = ComposeUIViewController(
    configure = {
         initKoinIOS()
    }
) {  App()}