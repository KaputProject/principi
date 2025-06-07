package ui.pages.userPages

import TransactionGenerator
import Transactions
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ui.api.getTransactions
import ui.api.getStatements
import ui.dataClasses.transaction.Transaction
import ui.dataClasses.user.User
import ui.dataClasses.statemant.Statement
import ui.pages.Generators.AccountGenerator
import ui.pages.Generators.LocationGenerator
import ui.pages.statementPages.StatementEdit
import ui.pages.statementPages.StatementShow
import ui.pages.transactionPages.TransactionCreate
import ui.pages.transactionPages.TransactionEdit
import ui.pages.transactionPages.TransactionShow

@Composable
fun UserMenu(
    user: User,
    onEditClick: (User) -> Unit,
    onAccountClick: (User) -> Unit,
    onLocationClick: (User) -> Unit,
    onBackClick: () -> Unit,
    onCreateTransactionClick: () -> Unit,

    ) {
    var transactions by remember { mutableStateOf<List<Transaction>>(emptyList()) }
    var statements by remember { mutableStateOf<List<Statement>>(emptyList()) }
    var selectedStatement by remember { mutableStateOf<Statement?>(null) }
    var editingStatement by remember { mutableStateOf<Statement?>(null) }
    var selectedTransaction by remember { mutableStateOf<Transaction?>(null) }
    var editingTransaction by remember { mutableStateOf<Transaction?>(null) }
    var creatingTransactionForStatement by remember { mutableStateOf<Statement?>(null) }

    var generatingLocations by remember { mutableStateOf(false) }
    var generatingAccounts by remember { mutableStateOf(false) }
    var generatingTransactions by remember { mutableStateOf(false) }  // NOVO stanje

    var reloadTrigger by remember { mutableStateOf(0) }

    LaunchedEffect(user.id, reloadTrigger) {
        try {
            transactions = getTransactions(userId = user.id ?: "")
            statements = getStatements(userId = user.id ?: "")
        } catch (e: Exception) {
            println("Napaka pri pridobivanju podatkov: ${e.message}")
        }
    }


    editingTransaction?.let { transaction ->
        TransactionEdit(
            initialTransaction = transaction,
            onBackClick = { editingTransaction = null },
            onTransactionUpdated = { updated ->
                transactions = transactions.map { if (it.id == updated.id) updated else it }
                editingTransaction = null
                selectedTransaction = null
            },
            onTransactionDeleted = {
                transactions = transactions.filter { it.id != transaction.id }
                editingTransaction = null
                selectedTransaction = null
            }
        )
        return
    }

    editingStatement?.let { statement ->
        StatementEdit(
            initialStatement = statement,
            onBackClick = { editingStatement = null },
            onStatementUpdated = { updated ->
                statements = statements.map { if (it.id == updated.id) updated else it }
                editingStatement = null
                selectedStatement = null
            }
        )
        return
    }
    if (generatingLocations) {
        LocationGenerator(userId = user.id ?: "")
        return
    }

    if (generatingAccounts) {
        AccountGenerator(userId = user.id ?: "")
        return
    }


    creatingTransactionForStatement?.let { statement ->
        TransactionCreate(
            user = user,
            statement = statement,
            onBackClick = { creatingTransactionForStatement = null },
            onTransactionCreated = { newTransaction ->
                creatingTransactionForStatement = null
                reloadTrigger++
            }

        )
        return
    }

    selectedStatement?.let { statement ->
        StatementShow(
            statement = statement,
            onBackClick = { selectedStatement = null },
            onEditClick = { editingStatement = it },
            onCreateTransactionClick = { stmt ->
                creatingTransactionForStatement = stmt
                selectedStatement = null
            },
            onTransactionClick = {
                selectedTransaction = it.toTransaction()
                selectedStatement = null
            }

        )
        return
    }

    selectedTransaction?.let { transaction ->
        user.id?.let {
            TransactionShow(
                transaction = transaction,
                onBackClick = { selectedTransaction = null },
                onEditClick = { editingTransaction = it },
                userId = it
            )
        }
        return
    }
    if (generatingTransactions) {
        TransactionGenerator(
            userId = user.id ?: "",
            onBackClick = { generatingTransactions = false },
            onGenerated = {
                generatingTransactions = false
                reloadTrigger++ // osveži podatke
            },
            accounts = user.accounts,
            locations = user.locations
        )
        return
    }
    // UI
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "Uporabniški meni",
            style = MaterialTheme.typography.h4,
            color = colors.primary
        )
        Spacer(modifier = Modifier.height(16.dp))

        Card(
            elevation = 4.dp,
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = colors.surface
        ) {
            Row(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    InfoRow("Ime", user.name)
                    InfoRow("Priimek", user.surname)
                    InfoRow("Uporabniško ime", user.username)
                    InfoRow("Email", user.email)
                    InfoRow("Datum rojstva", user.dateOfBirth)
                }
                Column(modifier = Modifier.weight(1f)) {
                    InfoRow("Identifikator", user.identifier)
                    InfoRow("Administrator", if (user.isAdmin) "Da" else "Ne")
                    InfoRow("Število računov", user.accounts.size.toString())
                    InfoRow("Število lokacij", user.locations.size.toString())
                }
            }
        }


        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { onEditClick(user) },
                modifier = Modifier.weight(1f)
            ) {
                Text("Uredi uporabnika")
            }
            Button(
                onClick = { onAccountClick(user) },
                modifier = Modifier.weight(1f)
            ) {
                Text("Računi")
            }
            Button(
                onClick = { onLocationClick(user) },
                modifier = Modifier.weight(1f)
            ) {
                Text("Lokacije")
            }
            OutlinedButton(
                onClick = onBackClick,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = colors.secondary,
                ),
            ) {
                Text("Nazaj")
            }

        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ){
            Button(
                onClick = {
                    onCreateTransactionClick()
                },
                modifier = Modifier.weight(1f)

            ) {
                Text("Ustvari transakcijo brez izpiska")
            }
            Button(
                onClick = { generatingLocations = true },
                modifier = Modifier.weight(1f)
            ) {
                Text("Generiraj lokacije")
            }
            Button(
                onClick = { generatingAccounts = true },
                modifier = Modifier.weight(1f)
            ) {
                Text("Generiraj račune")
            }
            Button(
                onClick = { generatingTransactions = true },
                modifier = Modifier.weight(1f)
            ) {
                Text("Generiraj transakcije")  // Tvoj novi gumb
            }
        }

        }



        Spacer(modifier = Modifier.height(24.dp))


        Row(modifier = Modifier.fillMaxSize()) {
            Statements(
                statements = statements,
                onStatementSelected = { selectedStatement = it },
                modifier = Modifier.weight(1f).fillMaxHeight()
            )
            Transactions(
                transactions = transactions,
                onTransactionSelected = { selectedTransaction = it },
                modifier = Modifier.weight(1f).fillMaxHeight()
            )
        }
    }
}

@Composable
fun InfoRow(label: String, value: String?) {
    Text(
        text = label,
        style = MaterialTheme.typography.h6,
        color = colors.primary
    )
    Text(
        text = value ?: "Ni podatka",
        style = MaterialTheme.typography.body1.copy(fontSize = MaterialTheme.typography.body1.fontSize.times(1.2f)),
        color = colors.onSurface
    )
    Spacer(modifier = Modifier.height(12.dp))
}

