package ui.pages.statementPages

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ui.dataClasses.statemant.Statement

@Composable
fun StatementShow(
    statement: Statement,
    onBackClick: () -> Unit
) {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        Text(">>> Izpisek št. ${statement.id ?: "-"} <<<", style = MaterialTheme.typography.h5)
        Spacer(modifier = Modifier.height(16.dp))

        Text("Uporabnik ID: ${statement.user ?: "neznano"}")
        Text("Račun ID: ${statement.account ?: "neznano"}")
        Spacer(modifier = Modifier.height(8.dp))

        Text("Obdobje: ${statement.startDate?.take(10)} do ${statement.endDate?.take(10)}")
        Text("Mesec / Leto: ${statement.month}/${statement.year}")
        Spacer(modifier = Modifier.height(8.dp))

        Text("Začetno stanje: ${statement.startBalance}")
        Text("Končno stanje: ${statement.endBalance}")
        Text("Prilivi: ${statement.inflow}")
        Text("Odlivi: ${statement.outflow}")
        Spacer(modifier = Modifier.height(16.dp))

        Text("Transakcije v tem izpisku:")
        if (statement.transactions.isEmpty()) {
            Text("- Ni transakcij.")
        } else {
            statement.transactions.forEach { id ->
                Text("- $id")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onBackClick) {
            Text("Nazaj")
        }
    }
}
