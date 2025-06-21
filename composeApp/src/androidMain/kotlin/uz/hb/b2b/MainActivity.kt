package uz.hb.b2b

import android.graphics.Color
import android.util.Log
import app.App
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.bundle.Bundle
import androidx.core.view.WindowCompat
import androidx.fragment.app.FragmentActivity
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import org.koin.core.context.loadKoinModules
import di.bioModule
import platform.NotificationPermissionRequester

class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        // 🔥 Получаем push токен и выводим в лог
        Firebase.messaging.token
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val token = task.result
                    Log.d("PushService", "💥 Push token: $token")
                } else {
                    Log.e("PushService", "❌ Failed to get token", task.exception)
                }
            }
        NotificationPermissionRequester.requestPermissionIfNeeded(this)

        // Важно: это позволяет рисовать под системные insets (без их скрытия!)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                Color.TRANSPARENT, Color.TRANSPARENT
            ),
            navigationBarStyle = SystemBarStyle.light(
                Color.TRANSPARENT, Color.TRANSPARENT
            )
        )

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