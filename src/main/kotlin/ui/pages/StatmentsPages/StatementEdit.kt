package ui.pages.statementPages

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ui.dataClasses.statemant.Statement
import ui.api.updateStatement

@Composable
fun StatementEdit(
    initialStatement: Statement,
    onBackClick: () -> Unit,
    onStatementUpdated: (Statement) -> Unit
) {
    var startDate by remember { mutableStateOf(initialStatement.startDate ?: "") }
    var endDate by remember { mutableStateOf(initialStatement.endDate ?: "") }
    var inflow by remember { mutableStateOf(initialStatement.inflow.toString()) }
    var outflow by remember { mutableStateOf(initialStatement.outflow.toString()) }
    var startBalance by remember { mutableStateOf(initialStatement.startBalance.toString()) }
    var endBalance by remember { mutableStateOf(initialStatement.endBalance.toString()) }
    var month by remember { mutableStateOf(initialStatement.month.toString()) }
    var year by remember { mutableStateOf(initialStatement.year.toString()) }

    var message by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        println("Initial Statement ID: ${initialStatement.id}")
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Uredi izpisek", style = MaterialTheme.typography.h5)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = startDate,
            onValueChange = { startDate = it },
            label = { Text("Začetni datum") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = endDate,
            onValueChange = { endDate = it },
            label = { Text("Končni datum") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = inflow,
            onValueChange = { inflow = it },
            label = { Text("Prejemki") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = outflow,
            onValueChange = { outflow = it },
            label = { Text("Odhodki") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = startBalance,
            onValueChange = { startBalance = it },
            label = { Text("Začetno stanje") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = endBalance,
            onValueChange = { endBalance = it },
            label = { Text("Končno stanje") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = month,
            onValueChange = { month = it },
            label = { Text("Mesec") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = year,
            onValueChange = { year = it },
            label = { Text("Leto") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = {
                // Izpiši podatke za debug
                println("Posiljam updateStatement z naslednjimi podatki:")
                println("statementId = ${initialStatement.id ?: ""}")
                println("startDate = $startDate")
                println("endDate = $endDate")
                println("inflow = ${inflow.toDoubleOrNull() ?: 0.0}")
                println("outflow = ${outflow.toDoubleOrNull() ?: 0.0}")
                println("startBalance = ${startBalance.toDoubleOrNull() ?: 0.0}")
                println("endBalance = ${endBalance.toDoubleOrNull() ?: 0.0}")
                println("month = ${month.toIntOrNull() ?: 0}")
                println("year = ${year.toIntOrNull() ?: 0}")
                println("userId = ${initialStatement.user.toString()}")

                coroutineScope.launch {
                    val result = updateStatement(
                        statementId = initialStatement.id ?: "",
                        startDate = startDate,
                        endDate = endDate,
                        inflow = inflow.toDoubleOrNull() ?: 0.0,
                        outflow = outflow.toDoubleOrNull() ?: 0.0,
                        startBalance = startBalance.toDoubleOrNull() ?: 0.0,
                        endBalance = endBalance.toDoubleOrNull() ?: 0.0,
                        month = month.toIntOrNull() ?: 0,
                        year = year.toIntOrNull() ?: 0,
                        userId = initialStatement.user?.id.toString()
                    )

                    result.onSuccess {
                        onStatementUpdated(
                            initialStatement.copy(
                                startDate = startDate,
                                endDate = endDate,
                                inflow = inflow.toDoubleOrNull() ?: 0.0,
                                outflow = outflow.toDoubleOrNull() ?: 0.0,
                                startBalance = startBalance.toDoubleOrNull() ?: 0.0,
                                endBalance = endBalance.toDoubleOrNull() ?: 0.0,
                                month = month.toIntOrNull() ?: 0,
                                year = year.toIntOrNull() ?: 0
                            )
                        )
                        message = "Uspešno posodobljeno."
                    }.onFailure {
                        message = "Napaka: ${it.message}"
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Shrani spremembe")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = onBackClick, modifier = Modifier.fillMaxWidth()) {
            Text("Nazaj")
        }

        message?.let {
            Spacer(modifier = Modifier.height(16.dp))
            Text(it, color = if (it.startsWith("Napaka")) MaterialTheme.colors.error else MaterialTheme.colors.primary)
        }
    }
}
