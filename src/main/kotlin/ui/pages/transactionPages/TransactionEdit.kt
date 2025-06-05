package ui.pages.transactionPages

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ui.api.updateTransaction
import ui.api.deleteTransaction
import ui.dataClasses.transaction.Transaction

@Composable
fun TransactionEdit(
    initialTransaction: Transaction,
    onBackClick: () -> Unit,
    onTransactionUpdated: (Transaction) -> Unit,
    onTransactionDeleted: () -> Unit
) {
    var description by remember { mutableStateOf(initialTransaction.description) }
    var change by remember { mutableStateOf(initialTransaction.change.toString()) }
    var datetime by remember { mutableStateOf(initialTransaction.datetime) }
    var reference by remember { mutableStateOf(initialTransaction.reference ?: "") }

    var message by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()
    var showDeleteDialog by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Uredi transakcijo", style = MaterialTheme.typography.h5)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Opis") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = change,
            onValueChange = { change = it },
            label = { Text("Znesek") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = datetime,
            onValueChange = { datetime = it },
            label = { Text("Datum in čas") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = reference,
            onValueChange = { reference = it },
            label = { Text("Referenca") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                coroutineScope.launch {
                    val result = updateTransaction(
                        transactionId = initialTransaction.id,
                        description = description,
                        change = change.toDoubleOrNull() ?: 0.0,
                        datetime = datetime,
                        reference = reference,
                        userId = initialTransaction.user,
                    )

                    result.onSuccess {
                        onTransactionUpdated(
                            initialTransaction.copy(
                                description = description,
                                change = change.toDoubleOrNull() ?: initialTransaction.change,
                                datetime = datetime,
                                reference = if (reference.isBlank()) null else reference
                            )
                        )
                    }

                    result.onFailure {
                        message = "Napaka: ${it.message}"
                    }

                    onBackClick()
                }
            },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        ) {
            Text("Shrani")
        }

        Button(
            onClick = { showDeleteDialog = true },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.error)
        ) {
            Text("Izbriši transakcijo", color = MaterialTheme.colors.onError)
        }

        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("Potrditev brisanja") },
                text = { Text("Ste prepričani, da želite izbrisati to transakcijo?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showDeleteDialog = false
                            coroutineScope.launch {
                                val result = deleteTransaction(
                                    transactionId = initialTransaction.id,
                                    userId = initialTransaction.user,
                                )

                                message = result?.fold(
                                    onSuccess = { "Transakcija uspešno izbrisana." },
                                    onFailure = { "Napaka pri brisanju: ${it.message}" }
                                )

                                if (result?.isSuccess == true) {
                                    onTransactionDeleted()
                                }
                            }
                        }
                    ) {
                        Text("Da, izbriši")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = false }) {
                        Text("Prekliči")
                    }
                }
            )
        }

        Button(
            onClick = onBackClick, modifier = Modifier.fillMaxWidth()
        ) {
            Text("Nazaj")
        }

        message?.let {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                it, color = if (it.startsWith("Napaka")) MaterialTheme.colors.error else MaterialTheme.colors.primary
            )
        }
    }
}
