package features.home.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.icerock.moko.resources.compose.stringResource
import domain.model.Client
import domain.model.Account
import uz.hb.b2b.SharedRes
import core.utils.*

@Composable
fun ClientListScreen(
    clients: List<Client>,
    listState: LazyListState,
    bottomBarHeight: Dp
) {

    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize()
            .windowInsetsPadding(WindowInsets.statusBars) // сверху
            .padding(top = 30.dp, start = 16.dp, end = 16.dp), // Только верх/бока,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(bottom = bottomBarHeight) // <<< вот тут!
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
            Text("${
                stringResource(
                SharedRes.strings.inn)
            }: ${client.inn}", style = MaterialTheme.typography.bodyMedium)
            Text(client.address, style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.height(8.dp))
            if (client.accounts.isNotEmpty()) {
                Text("${stringResource(
                    SharedRes.strings.accounts)}:", style = MaterialTheme.typography.labelMedium)
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
        Text("${stringResource(
            SharedRes.strings.balance)}: ${formatSumFromTiyin(account.sOut)}", style = MaterialTheme.typography.bodySmall)
    }
}
