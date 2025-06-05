package ui.pages.userPages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ui.api.getTransactions
import ui.components.TransactionCard
import ui.dataClasses.transaction.Transaction
import ui.dataClasses.user.User
import ui.api.getStatements
import ui.components.StatementCard
import ui.dataClasses.statemant.Statement
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.ui.Alignment

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
    var statements by remember { mutableStateOf<List<Statement>>(emptyList()) }

    // Naloži transakcije in izpiske za uporabnika
    LaunchedEffect(user.id) {
        try {
            transactions = getTransactions(userId = user.id ?: "")
            statements = getStatements(userId = user.id ?: "")
        } catch (e: Exception) {
            println("Napaka pri pridobivanju podatkov: ${e.message}")
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

        // Levi in desni stolpec
        Row(modifier = Modifier.fillMaxSize()) {

            // Levi: Statements
            val statementListState = rememberLazyListState()

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(end = 8.dp)
                    .background(MaterialTheme.colors.surface)
            ) {
                if (statements.isEmpty()) {
                    Text("Ni izpiskov.", modifier = Modifier.padding(8.dp))
                } else {
                    Box(Modifier.fillMaxSize()) {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize().padding(end = 12.dp),
                            state = statementListState
                        ) {
                            items(statements) { statement ->
                                StatementCard(
                                    statement = statement,
                                    onClick = { /* Po potrebi */ }
                                )
                            }
                        }

                        VerticalScrollbar(
                            adapter = rememberScrollbarAdapter(statementListState),
                            modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight()
                        )
                    }
                }
            }


            // Desni: Transactions
            val transactionListState = rememberLazyListState()

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
                    Box(Modifier.fillMaxSize()) {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize().padding(end = 12.dp),
                            state = transactionListState
                        ) {
                            items(transactions) { transaction ->
                                TransactionCard(
                                    transaction = transaction,
                                    onClick = { /* Po potrebi */ }
                                )
                            }
                        }

                        VerticalScrollbar(
                            adapter = rememberScrollbarAdapter(transactionListState),
                            modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight()
                        )
                    }
                }
            }
        }
    }
}
