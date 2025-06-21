package features.main.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import ui.components.AppTopBar

data class PaymentOption(val icon: ImageVector, val title: String, val subtitle: String? = null)

object PaymentCreateScreen : Screen {
    @Composable
    override fun Content() {
        val options = listOf(
            PaymentOption(Icons.Default.CompareArrows, "Платёж контрагенту"),
            PaymentOption(Icons.Default.Person, "Платёж физлицу"),
            PaymentOption(Icons.Default.AccountBalanceWallet, "Выплата зарплаты"),
            PaymentOption(Icons.Default.Gavel, "Платёж в бюджет"),
            PaymentOption(Icons.Default.Savings, "Пополнение ЕНС", "Единый налоговый счёт"),
            PaymentOption(Icons.Default.QrCode, "Оплата по QR"),
            PaymentOption(Icons.Default.ReceiptLong, "Распознавание счёта"),
            PaymentOption(Icons.Default.Home, "Оплата ЖКУ"),
            PaymentOption(Icons.Default.Input, "Импорт платежа 1С")
        )

        Scaffold(
            topBar = {
                AppTopBar(title = "Создать")
            }
        ) { padding ->
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Text(
                        text = "Платежи",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
                items(options) { option ->
                    PaymentItem(option)
                }
            }
        }
    }

    @Composable
    private fun PaymentItem(option: PaymentOption) {
        ListItem(
            headlineContent = { Text(option.title) },
            supportingContent = option.subtitle?.let { { Text(it) } },
            leadingContent = {
                Icon(
                    imageVector = option.icon,
                    contentDescription = null
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    // TODO: Handle navigation to the corresponding screen
                }
        )
        Divider()
    }
}