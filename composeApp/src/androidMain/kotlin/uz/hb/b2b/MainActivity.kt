package uz.hb.b2b

import app.App
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.bundle.Bundle
import androidx.core.view.WindowCompat
import androidx.fragment.app.FragmentActivity
import org.koin.core.context.loadKoinModules
import di.bioModule

class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Важно: это позволяет рисовать под системные insets (без их скрытия!)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        // ВНИМАНИЕ: подгружаем модуль с биометрией
        loadKoinModules(bioModule(this))

        setContent {
            App()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}