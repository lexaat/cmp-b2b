import androidx.compose.ui.window.ComposeUIViewController
import app.App
import di.initKoinIOS
import platform.UIKit.UIView
import platform.UIKit.UIViewController
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import platform.UIKit.UIColor

fun MainViewController() = ComposeUIViewController(
    configure = { initKoinIOS() }
) { App() }.apply {
    view.backgroundColor = UIColor.clearColor
}