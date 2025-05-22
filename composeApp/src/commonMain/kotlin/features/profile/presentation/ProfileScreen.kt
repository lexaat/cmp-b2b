package features.profile.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.koinInject
import features.auth.presentation.login.LoginScreen
import cafe.adriel.voyager.navigator.currentOrThrow

object ProfileScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = koinInject<ProfileViewModel>()
        var showDialog by remember { mutableStateOf(false) }

        LaunchedEffect(Unit) {
            viewModel.sideEffect.collectLatest { effect ->
                when (effect) {
                    is ProfileSideEffect.NavigateToLogin -> {
                        navigator.replaceAll(LoginScreen)
                    }
                    is ProfileSideEffect.ShowLogoutConfirmation -> {
                        showDialog = true
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("👤 Профиль", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(24.dp))
            Button(
                onClick = { viewModel.confirmLogout() },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Выйти из приложения")
            }
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Выход") },
                text = { Text("Вы уверены, что хотите выйти?") },
                confirmButton = {
                    TextButton(onClick = {
                        showDialog = false
                        viewModel.logout()
                    }) {
                        Text("Да")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text("Отмена")
                    }
                }
            )
        }
    }

}

