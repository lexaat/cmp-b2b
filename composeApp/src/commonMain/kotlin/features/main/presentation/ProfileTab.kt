package features.main.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen

object ProfileTab : Screen {
    @Composable
    override fun Content() {
        Column(Modifier.padding(16.dp)) {
            Text("👤 Профиль")
            Spacer(modifier = Modifier.height(8.dp))
            Text("Информация о пользователе")
        }
    }
}