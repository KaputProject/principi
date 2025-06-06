package ui.pages.statementPages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ui.api.showTransaction
import ui.dataClasses.statemant.Statement
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import ui.components.cards.TransactionCardUser
import ui.dataClasses.transaction.TransactionUser

@Composable
fun StatementShow(
    statement: Statement,
    onBackClick: () -> Unit,
    onEditClick: (Statement) -> Unit,
    onCreateTransactionClick: (Statement) -> Unit,
    onTransactionClick: (TransactionUser) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val transactionState = remember { mutableStateOf<List<TransactionUser>>(emptyList()) }
    val loadingState = remember { mutableStateOf(true) }
    val scrollState = rememberScrollState()  // Scroll state za scrollable Column

    LaunchedEffect(statement.transactions) {
        loadingState.value = true
        val loadedTransactions = mutableListOf<TransactionUser>()
        statement.transactions.forEach { id ->
            val result = showTransaction(statement.user?.id ?: "", id)
            result.getOrNull()?.let { loadedTransactions.add(it) }
        }
        transactionState.value = loadedTransactions
        loadingState.value = false
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.surface)
            .padding(16.dp)
    ) {
        // Glavni scrollabilen del postavimo v Column z modifierjem weight(1f), da zasede prostor nad gumbi
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = " Izpisek št. ${statement.id ?: "-"} ",
                style = MaterialTheme.typography.h5,
                color = MaterialTheme.colors.primary
            )
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    InfoRow("Uporabnik", statement.user?.name ?: "neznano")
                    InfoRow("Račun IBAN", statement.account?.iban ?: "neznano")
                    InfoRow(
                        "Obdobje",
                        "${statement.startDate?.take(10) ?: "-"} do ${statement.endDate?.take(10) ?: "-"}"
                    )
                    InfoRow("Mesec / Leto", "${statement.month}/${statement.year}")
                }

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    InfoRow("Začetno stanje", statement.startBalance.toString())
                    InfoRow("Končno stanje", statement.endBalance.toString())
                    InfoRow("Prilivi", statement.inflow.toString())
                    InfoRow("Odlivi", statement.outflow.toString())
                }
            }


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
                // Zamenjajmo Column + verticalScroll z LazyColumn in weight(1f)
                LazyColumn(
                    modifier = Modifier.weight(1f)
                ) {
                    items(transactionState.value) { transaction ->
                        TransactionCardUser(
                            transaction = transaction,
                            onClick = { onTransactionClick(transaction) }  // pass the clicked transaction here
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }

            Divider(Modifier.padding(vertical = 16.dp))
        }

        // Tukaj so gumbi na dnu
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { onCreateTransactionClick(statement) },
                modifier = Modifier.weight(1f)
            ) {
                Text("Ustvari transakcijo")
            }

            Button(
                onClick = { onEditClick(statement) },
                modifier = Modifier.weight(1f)
            ) {
                Text("Uredi izpisek")
            }

            OutlinedButton(
                onClick = onBackClick,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = colors.secondary,
                    contentColor = colors.onSecondary
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
