package features.main.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen

object HomeTab : Screen {
    @Composable
    override fun Content() {
        Column(Modifier.padding(16.dp)) {
            Text("🏠 Домашняя страница")
            Spacer(modifier = Modifier.height(8.dp))
            Text("Контент главного экрана")
        }
    }
}