package ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ui.dataClasses.transaction.Transaction

@Composable
fun TransactionCard(
    transaction: Transaction,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() },
        elevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Opis: ${transaction.description}")
            Text("Znesek: ${transaction.change}")
            Text("Datum: ${transaction.datetime.take(10)}") // samo YYYY-MM-DD
            Text("IBAN: ${transaction.account?.iban ?: "Neznan račun"}")
            Text("Tip: ${if (transaction.outgoing) "Odhodna" else "Dohodna"}")
        }
    }
}

