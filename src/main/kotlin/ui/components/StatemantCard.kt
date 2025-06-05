package ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ui.dataClasses.statemant.Statement

@Composable
fun StatementCard(
    statement: Statement,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() },
        elevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Mesec: ${statement.month}/${statement.year}")
            Text("Datum od: ${statement.startDate ?: "Ni podatka"}")
            Text("Datum do: ${statement.endDate ?: "Ni podatka"}")
            Text("Začetno stanje: ${statement.startBalance}")
            Text("Končno stanje: ${statement.endBalance}")
            Text("Prihodek: ${statement.inflow}")
            Text("Odhodek: ${statement.outflow}")
            Text("Št. transakcij: ${statement.transactions.size}")
        }
    }
}
