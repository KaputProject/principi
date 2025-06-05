package ui.pages.userPages

import Transactions
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ui.api.getTransactions
import ui.api.getStatements
import ui.dataClasses.transaction.Transaction
import ui.dataClasses.user.User
import ui.dataClasses.statemant.Statement
import ui.pages.statementPages.StatementShow
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
    var selectedTransaction by remember { mutableStateOf<Transaction?>(null) }  // novo stanje

    LaunchedEffect(user.id) {
        try {
            transactions = getTransactions(userId = user.id ?: "")
            statements = getStatements(userId = user.id ?: "")
        } catch (e: Exception) {
            println("Napaka pri pridobivanju podatkov: ${e.message}")
        }
    }

    // Če je izbran statement, pokaži StatementShow
    selectedStatement?.let { statement ->
        StatementShow(
            statement = statement,
            onBackClick = { selectedStatement = null }
        )
        return
    }

    // Če je izbrana transakcija, pokaži TransactionShow
    selectedTransaction?.let { transaction ->
        TransactionShow(
            transaction = transaction,
            onBackClick = { selectedTransaction = null }
        )
        return
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

        Row(modifier = Modifier.fillMaxSize()) {
            Statements(
                statements = statements,
                onStatementSelected = { selectedStatement = it },
                modifier = Modifier.weight(1f).fillMaxHeight()
            )
            Transactions(
                transactions = transactions,
                onTransactionSelected = { selectedTransaction = it },  // dodamo handler za klik
                modifier = Modifier.weight(1f).fillMaxHeight()
            )
        }
    }
}

