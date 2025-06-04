package ui.pages.transactionPages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ui.api.getTransactions
import ui.dataClasses.account.Account
import ui.dataClasses.transaction.Transaction
import ui.components.TransactionCard
import ui.dataClasses.user.User

@Composable
fun Transactions(
    account: Account,
    user: User,
    onBackClick: () -> Unit,
    onTransactionClick: (Transaction) -> Unit
) {
    var transactions by remember { mutableStateOf<List<Transaction>>(emptyList()) }

    LaunchedEffect(user.id) {
        try {
            transactions = getTransactions(userId = user.id ?: "")
        } catch (e: Exception) {
            println("Napaka pri pridobivanju transakcij: ${e.message}")
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Vse transakcije za uporabnika: ${user.name}", style = MaterialTheme.typography.h5)
        Spacer(modifier = Modifier.height(16.dp))

        if (transactions.isEmpty()) {
            Text("Ni transakcij za uporabnika.")
        } else {
            LazyColumn {
                items(transactions) { transaction ->
                    Text("Transakcija: ${transaction.description} | Račun: ${transaction.account?.iban ?: "Neznan"} | Znesek: ${transaction.change}")
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = onBackClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Nazaj")
        }
    }
}

