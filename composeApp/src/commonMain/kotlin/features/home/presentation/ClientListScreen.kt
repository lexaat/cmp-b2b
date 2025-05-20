package features.home.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import features.home.domain.model.*

@Composable
fun ClientListScreen(clients: List<Client>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(clients) { client ->
            ClientItem(client)
        }
    }
}

@Composable
fun ClientItem(client: Client) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(client.name, style = MaterialTheme.typography.titleLarge)
            Text("ИНН: ${client.inn}", style = MaterialTheme.typography.bodyMedium)
            Text(client.address, style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.height(8.dp))
            if (client.accounts.isNotEmpty()) {
                Text("Счета:", style = MaterialTheme.typography.labelMedium)
                client.accounts.forEach { account ->
                    AccountItem(account)
                }
            }
        }
    }
}

@Composable
fun AccountItem(account: Account) {
    Column(modifier = Modifier.padding(start = 12.dp, top = 4.dp)) {
        Text("№ ${account.account}", style = MaterialTheme.typography.bodySmall)
        Text("Баланс: ${account.balanceOut}", style = MaterialTheme.typography.bodySmall)
    }
}
