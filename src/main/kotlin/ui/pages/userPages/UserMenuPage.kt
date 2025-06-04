package ui.pages.userPages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ui.api.getTransactions
import ui.components.TransactionCard
import ui.dataClasses.transaction.Transaction
import ui.dataClasses.user.User

@Composable
fun UserMenuPage(
    user: User,
    onEditClick: (User) -> Unit,
    onAccountClick: (User) -> Unit,
    onLocationClick: (User) -> Unit,
    onBackClick: () -> Unit
) {
    println("UserMenuPage opened with user: ${user.name}, ${user.email}")

    val coroutineScope = rememberCoroutineScope()
    var transactions by remember { mutableStateOf<List<Transaction>>(emptyList()) }

    // Naloži transakcije za uporabnika
    LaunchedEffect(user.id) {
        try {
            transactions = getTransactions(userId = user.id ?: "")
        } catch (e: Exception) {
            println("Napaka pri pridobivanju transakcij: ${e.message}")
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(">>> User Menu Page <<<", style = MaterialTheme.typography.h5)
        Spacer(modifier = Modifier.height(16.dp))

        Text("Name: ${user.name ?: "Unknown"}")
        Text("Email: ${user.email ?: "Unknown"}")
        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = { onEditClick(user) }, modifier = Modifier.fillMaxWidth()) {
            Text("Edit User")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { onAccountClick(user) }, modifier = Modifier.fillMaxWidth()) {
            Text("See Accounts")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { onLocationClick(user) }, modifier = Modifier.fillMaxWidth()) {
            Text("See Locations")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onBackClick, modifier = Modifier.fillMaxWidth()) {
            Text("Nazaj")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Spodnji del: levi in desni del
        Row(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(end = 8.dp)
                    .background(MaterialTheme.colors.surface)
            ) {
                Text("Levi del", modifier = Modifier.padding(8.dp))
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(start = 8.dp)
                    .background(MaterialTheme.colors.surface)
            ) {
                if (transactions.isEmpty()) {
                    Text("Ni transakcij.", modifier = Modifier.padding(8.dp))
                } else {
                    LazyColumn(modifier = Modifier.padding(8.dp)) {
                        items(transactions) { transaction ->
                            TransactionCard(
                                transaction = transaction,
                                onClick = { /* Dodaj po potrebi */ }
                            )
                        }
                    }
                }
            }
        }
    }
}
