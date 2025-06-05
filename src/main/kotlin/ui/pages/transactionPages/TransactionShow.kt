package ui.pages.transactionPages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ui.dataClasses.transaction.Transaction

@Composable
fun TransactionShow(
    transaction: Transaction,
    onBackClick: () -> Unit,
    onEditClick: (Transaction) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.surface)
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = "Podrobnosti transakcije",
                style = MaterialTheme.typography.h5,
                color = MaterialTheme.colors.onBackground
            )
            Spacer(modifier = Modifier.height(16.dp))

            InfoRow("ID", transaction.id)
            InfoRow("Uporabnik", transaction.user.toString())
            InfoRow("Račun", transaction.account.iban)
            InfoRow("Lokacija", transaction.location?.name ?: "N/A")
            InfoRow("Datum in čas", transaction.datetime)
            InfoRow("Opis", transaction.description)

            Text(
                text = "Znesek: ${if (transaction.outgoing) "-" else "+"}${transaction.change}",
                style = MaterialTheme.typography.body1,
                color = if (transaction.outgoing) MaterialTheme.colors.error else MaterialTheme.colors.primary,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            InfoRow("Sklic", transaction.reference ?: "N/A")
        }

        Column {
            Divider(Modifier.padding(vertical = 16.dp))
            Button(
                onClick = { onEditClick(transaction) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Uredi")
            }
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(
                onClick = onBackClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = colors.secondary,
                ),
            ) {
                Text("Nazaj")
            }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.subtitle1,
            color = MaterialTheme.colors.primary
        )
        Text(
            text = value,
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.onBackground
        )
    }
}
