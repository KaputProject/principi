package ui.pages.accountPages

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ui.api.getStatements
import ui.components.cards.StatementCard
import ui.dataClasses.account.Account
import ui.dataClasses.statemant.Statement


@Composable
fun ShowAccount(
    account: Account,
    onBackClick: () -> Unit,
    onEditClick: (Account) -> Unit,
    onStatementClick:(Statement)-> Unit

) {
    var statements by remember { mutableStateOf<List<Statement>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }
    val userId = account.user
    LaunchedEffect(userId) {
        loading = true
        statements = getStatements(userId).filter { statement ->
            // Filtriramo izpiske, ki pripadajo temu računu po IBANu
            statement.account?.iban == account.iban
        }
        loading = false
    }


    Column(modifier = Modifier.padding(16.dp)) {
        Text("Podrobnosti računa", style = MaterialTheme.typography.h5)
        Spacer(modifier = Modifier.height(16.dp))

        Text("IBAN: ${account.iban}", style = MaterialTheme.typography.body1)
        Text("Valuta: ${account.currency}", style = MaterialTheme.typography.body1)
        Text("Stanje: ${account.balance}", style = MaterialTheme.typography.body1)
        Text("Št. izpiskov: ${statements.size}", style = MaterialTheme.typography.body1)

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onEditClick.bind(account),
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        ) {
            Text("Uredi")
        }

        if (loading) {
            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
        } else if (statements.isEmpty()) {
            Text("Ni izpiskov za ta račun.", style = MaterialTheme.typography.body2)
        } else {
            Text("Izpiski:", style = MaterialTheme.typography.h6)
            Spacer(modifier = Modifier.height(8.dp))
            // Prikaz izpiskov z uporabo StatementCard
            statements.forEach { statement ->
                StatementCard(
                    statement = statement,
                    onClick = { onStatementClick(statement) }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
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

private fun ((Account) -> Unit).bind(account: Account): () -> Unit = {
    this(account)
}