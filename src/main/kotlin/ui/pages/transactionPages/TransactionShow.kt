package ui.pages.transactionPages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ui.api.showLocation
import ui.api.showUser
import ui.dataClasses.transaction.Transaction
import ui.pages.userPages.InfoRow
import ui.dataClasses.user.User

@Composable
fun TransactionShow(
    transaction: Transaction,
    onBackClick: () -> Unit,
    onEditClick: (Transaction) -> Unit,
    userId: String
) {
    val coroutineScope = rememberCoroutineScope()
    var locationName by remember { mutableStateOf<String?>(null) }
    var user by remember { mutableStateOf<User?>(null) }

    LaunchedEffect(transaction.location, transaction.user) {
        // Naloži lokacijo, če obstaja
        transaction.location?.let { loc ->
            val locationResult = showLocation(userId, loc._id)
            locationName = locationResult.getOrNull()?.name ?: "N/A"
        } ?: run {
            locationName = "N/A"
        }
        val result = showUser(transaction.user)
        if (result.isSuccess) {
            user = result.getOrNull()
            println("Uporabnik uspešno naložen: ${transaction.user}")
        } else {
            println("Napaka pri nalaganju uporabnika: ${result.exceptionOrNull()}")
        }
    }


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
                color = colors.onBackground
            )
            Spacer(modifier = Modifier.height(16.dp))

            InfoRow("ID", transaction.id)
            InfoRow("Uporabnik", user?.let { "${it.name} ${it.surname}" } ?: "Nalaganje...")
            InfoRow("Email", user?.email ?: "N/A")
            InfoRow("Uporabniško ime", user?.username ?: "N/A")
            InfoRow("Račun", transaction.account.iban)
            InfoRow("Lokacija", locationName ?: "Nalaganje...")
            InfoRow("Datum in čas", transaction.datetime)
            InfoRow("Opis", transaction.description)

            Text(
                text = "Znesek: ${if (transaction.outgoing) "-" else "+"}${transaction.change}",
                style = MaterialTheme.typography.body1,
                color = if (transaction.outgoing) colors.error else colors.primary,
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
