package ui.pages.transactionPages

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ui.dataClasses.transaction.Transaction

@Composable
fun TransactionShow(
    transaction: Transaction,
    onBackClick: () -> Unit,
    onEditClick: (Transaction) -> Unit // Dodano
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Transaction Details", style = MaterialTheme.typography.h5)
        Spacer(modifier = Modifier.height(16.dp))

        Text("ID: ${transaction.id}")
        Spacer(modifier = Modifier.height(8.dp))

        Text("User ID: ${transaction.user}")
        Spacer(modifier = Modifier.height(8.dp))

        Text("Account: ${transaction.account.iban}")
        Spacer(modifier = Modifier.height(8.dp))

        Text("Location: ${transaction.location?.name ?: "N/A"}")
        Spacer(modifier = Modifier.height(8.dp))

        Text("Date & Time: ${transaction.datetime}")
        Spacer(modifier = Modifier.height(8.dp))

        Text("Description: ${transaction.description}")
        Spacer(modifier = Modifier.height(8.dp))

        Text("Amount: ${if (transaction.outgoing) "-" else "+"}${transaction.change}")
        Spacer(modifier = Modifier.height(8.dp))

        Text("Reference: ${transaction.reference ?: "N/A"}")
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { onEditClick(transaction) },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        ) {
            Text("Uredi")
        }

        Button(
            onClick = onBackClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Nazaj")
        }
    }
}
