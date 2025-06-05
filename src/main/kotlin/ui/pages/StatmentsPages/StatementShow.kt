package ui.pages.statementPages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ui.dataClasses.statemant.Statement

@Composable
fun StatementShow(
    statement: Statement,
    onBackClick: () -> Unit,
    onEditClick: (Statement) -> Unit,
    onCreateTransactionClick: (Statement) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.surface)
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = ">>> Izpisek št. ${statement.id ?: "-"} <<<",
                style = MaterialTheme.typography.h5,
                color = MaterialTheme.colors.primary
            )
            Spacer(modifier = Modifier.height(16.dp))

            InfoRow("Uporabnik", statement.user?.name ?: "neznano")
            InfoRow("Račun IBAN", statement.account?.iban ?: "neznano")

            Spacer(modifier = Modifier.height(8.dp))
            InfoRow(
                "Obdobje",
                "${statement.startDate?.take(10) ?: "-"} do ${statement.endDate?.take(10) ?: "-"}"
            )
            InfoRow("Mesec / Leto", "${statement.month}/${statement.year}")

            Spacer(modifier = Modifier.height(8.dp))
            InfoRow("Začetno stanje", statement.startBalance.toString())
            InfoRow("Končno stanje", statement.endBalance.toString())
            InfoRow("Prilivi", statement.inflow.toString())
            InfoRow("Odlivi", statement.outflow.toString())

            Spacer(modifier = Modifier.height(16.dp))
            Divider()
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Transakcije v tem izpisku:",
                style = MaterialTheme.typography.subtitle1,
                color = MaterialTheme.colors.primary
            )
            Spacer(modifier = Modifier.height(4.dp))

            if (statement.transactions.isEmpty()) {
                Text(
                    "- Ni transakcij.",
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                )
            } else {
                statement.transactions.forEach { transactionName ->
                    Text(
                        "- $transactionName",
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.onBackground
                    )
                }
            }
        }

        Column {
            Divider(Modifier.padding(vertical = 16.dp))
            Button(
                onClick = { onCreateTransactionClick(statement) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ustvari transakcijo")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { onEditClick(statement) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Uredi izpisek")
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                onClick = onBackClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = colors.secondary
                ),
            ) {
                Text("Nazaj")
            }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.subtitle2,
            color = MaterialTheme.colors.primary
        )
        Text(
            text = value,
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.onBackground
        )
    }
}
