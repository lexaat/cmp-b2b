package features.profile.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import dev.icerock.moko.resources.compose.stringResource
import features.auth.presentation.login.LoginScreen
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.koinInject
import uz.hb.b2b.SharedRes

object ProfileScreen: Screen {
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

                    is ProfileSideEffect.ShowError -> TODO()
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
            Text("👤 ${
                stringResource(
                SharedRes.strings.profile)
            }", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(24.dp))
            Button(
                onClick = { viewModel.confirmLogout() },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text(stringResource(
                    SharedRes.strings.logout_app))
            }
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text(stringResource(
                    SharedRes.strings.logout)) },
                text = { Text(stringResource(
                    SharedRes.strings.logout_confirm)) },
                confirmButton = {
                    TextButton(onClick = {
                        showDialog = false
                        viewModel.logout()
                    }) {
                        Text(stringResource(
                            SharedRes.strings.yes))
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text(stringResource(
                            SharedRes.strings.cancel))
                    }
                }
            )
        }
    }

}

