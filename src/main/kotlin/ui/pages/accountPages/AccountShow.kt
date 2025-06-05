package ui.pages.accountPages

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ui.dataClasses.account.Account


@Composable
fun ShowAccount(
    account: Account,
    onBackClick: () -> Unit,
    onEditClick: (Account) -> Unit,
) {

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Podrobnosti računa", style = MaterialTheme.typography.h5)
        Spacer(modifier = Modifier.height(16.dp))

        Text("IBAN: ${account.iban}", style = MaterialTheme.typography.body1)
        Text("Valuta: ${account.currency}", style = MaterialTheme.typography.body1)
        Text("Stanje: ${account.balance}", style = MaterialTheme.typography.body1)
        Text("Št. izpiskov: ${account.statements.size}", style = MaterialTheme.typography.body1)

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onEditClick.bind(account),
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        ) {
            Text("Uredi")
        }

        Button(
            onClick = onBackClick,
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        ) {
            Text("Nazaj")
        }
    }
}

// Razširitev funkcije za bolj čitljivo uporabo lambda z argumentom
private fun ((Account) -> Unit).bind(account: Account): () -> Unit = {
    this(account)
}
