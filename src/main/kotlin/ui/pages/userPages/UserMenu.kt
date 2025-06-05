package ui.pages.userPages

import Transactions
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ui.api.getTransactions
import ui.api.getStatements
import ui.dataClasses.transaction.Transaction
import ui.dataClasses.user.User
import ui.dataClasses.statemant.Statement
import ui.pages.statementPages.StatementShow
import ui.pages.transactionPages.TransactionEdit
import ui.pages.transactionPages.TransactionShow

@Composable
fun UserMenu(
    user: User,
    onEditClick: (User) -> Unit,
    onAccountClick: (User) -> Unit,
    onLocationClick: (User) -> Unit,
    onBackClick: () -> Unit
) {
    var transactions by remember { mutableStateOf<List<Transaction>>(emptyList()) }
    var statements by remember { mutableStateOf<List<Statement>>(emptyList()) }
    var selectedStatement by remember { mutableStateOf<Statement?>(null) }
    var selectedTransaction by remember { mutableStateOf<Transaction?>(null) }
    var editingTransaction by remember { mutableStateOf<Transaction?>(null) }

    LaunchedEffect(user.id) {
        try {
            transactions = getTransactions(userId = user.id ?: "")
            statements = getStatements(userId = user.id ?: "")
        } catch (e: Exception) {
            println("Napaka pri pridobivanju podatkov: ${e.message}")
        }
    }

    // 1. Prikaz urejevalnika transakcije
    editingTransaction?.let { transaction ->
        TransactionEdit(
            initialTransaction = transaction,
            onBackClick = { editingTransaction = null },
            onTransactionUpdated = { updated ->
                transactions = transactions.map { if (it._id == updated._id) updated else it }
                editingTransaction = null
                selectedTransaction = null
            },
            onTransactionDeleted = {
                transactions = transactions.filter { it._id != transaction._id }
                editingTransaction = null
                selectedTransaction = null
            }
        )
        return
    }

    // 2. Prikaz izbranega izpiska
    selectedStatement?.let { statement ->
        StatementShow(
            statement = statement,
            onBackClick = { selectedStatement = null }
        )
        return
    }

    // 3. Prikaz izbrane transakcije
    selectedTransaction?.let { transaction ->
        TransactionShow(
            transaction = transaction,
            onBackClick = { selectedTransaction = null },
            onEditClick = { editingTransaction = it }
        )
        return
    }

    // 4. Glavni meni uporabnika
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

        Row(modifier = Modifier.fillMaxSize()) {
            Statements(
                statements = statements,
                onStatementSelected = { selectedStatement = it },
                modifier = Modifier.weight(1f).fillMaxHeight()
            )
            Transactions(
                transactions = transactions,
                onTransactionSelected = {
                    selectedTransaction = it
                },
                modifier = Modifier.weight(1f).fillMaxHeight(),

            )
        }
    }
}
