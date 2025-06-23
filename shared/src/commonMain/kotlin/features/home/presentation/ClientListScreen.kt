package features.home.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import core.utils.formatSumFromTiyin
import dev.chrisbanes.haze.hazeSource
import dev.icerock.moko.resources.compose.stringResource
import domain.model.Account
import domain.model.Client
import ui.theme.LocalHazeState
import uz.hb.shared.SharedRes

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
            .padding(top = 16.dp, start = 16.dp, end = 16.dp)
            .hazeSource(state = LocalHazeState.current),
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
        Text("№ ${account.account}", style = MaterialTheme.typography.bodySmall, color = Color.Red)
        Text("${stringResource(
            SharedRes.strings.balance)}: ${formatSumFromTiyin(account.sOut)}", style = MaterialTheme.typography.bodySmall)
    }
}
