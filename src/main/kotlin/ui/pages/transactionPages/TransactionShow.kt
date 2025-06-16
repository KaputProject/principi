package ui.pages.transactionPages

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ui.api.showLocation
import ui.api.showUser
import ui.dataClasses.transaction.Transaction
import ui.dataClasses.user.User
import ui.pages.userPages.InfoRow

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

import androidx.compose.ui.Alignment

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.SemiBold)
        )
        Text(
            text = value,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.body1,
        )
    }
}

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
        transaction.location?.let { loc ->
            val locationResult = showLocation(userId, loc._id)
            locationName = locationResult.getOrNull()?.name ?: "N/A"
        } ?: run {
            locationName = "N/A"
        }
        val result = showUser(transaction.user)
        if (result.isSuccess) {
            user = result.getOrNull()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 32.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopStart)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Podrobnosti transakcije",
                style = MaterialTheme.typography.h4.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colors.onBackground
            )
            Spacer(modifier = Modifier.height(24.dp))

            InfoRow("ID", transaction.id)
            InfoRow("Uporabnik", user?.let { "${it.name} ${it.surname}" } ?: "Nalaganje...")
            InfoRow("Email", user?.email ?: "N/A")
            InfoRow("Uporabniško ime", user?.username ?: "N/A")
            InfoRow("Račun", transaction.account.iban)
            InfoRow("Lokacija", locationName ?: "Nalaganje...")
            InfoRow("Datum in čas", transaction.datetime)
            InfoRow("Opis", transaction.description)

            Spacer(modifier = Modifier.height(16.dp))

            // Posebno za znesek, da je stil drugačen
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Znesek",
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.SemiBold)
                )
                Text(
                    text = "${if (transaction.outgoing) "-" else "+"}${transaction.change}",
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.h6.copy(
                        color = if (transaction.outgoing) MaterialTheme.colors.error else MaterialTheme.colors.primary,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            InfoRow("Sklic", transaction.reference ?: "N/A")

            Spacer(modifier = Modifier.height(80.dp)) // prostor za gumbe spodaj
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) {
            Divider(Modifier.padding(vertical = 12.dp))

            Button(
                onClick = { onEditClick(transaction) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Text("Uredi", fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = onBackClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colors.secondary
                ),
                shape = MaterialTheme.shapes.medium,
                border = ButtonDefaults.outlinedBorder
            ) {
                Text("Nazaj", fontSize = 18.sp)
            }
        }
    }
}

