package ui.pages.transactionPages

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ui.api.deleteTransaction
import ui.api.updateTransaction
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

    Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Column(modifier = Modifier.fillMaxSize()) {
            Text(
                "Uredi transakcijo",
                style = MaterialTheme.typography.h5,
                color = colors.onSurface
            )
            Spacer(modifier = Modifier.height(16.dp))

            description?.let {
                OutlinedTextField(
                    value = it,
                    onValueChange = { description = it },
                    label = { Text("Opis") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
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

            message?.let {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    it,
                    color = if (it.startsWith("Napaka")) colors.error else colors.primary,
                    style = MaterialTheme.typography.body1
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        coroutineScope.launch {
                            val result = description?.let {
                                updateTransaction(
                                    transactionId = initialTransaction.id,
                                    description = it,
                                    change = change.toDoubleOrNull() ?: 0.0,
                                    datetime = datetime,
                                    reference = reference,
                                    userId = initialTransaction.user.toString(),
                                )
                            }

                            if (result != null) {
                                result.onSuccess {
                                    onTransactionUpdated(
                                        initialTransaction.copy(
                                            description = description,
                                            change = change.toDoubleOrNull() ?: initialTransaction.change,
                                            datetime = datetime,
                                            reference = if (reference.isBlank()) null else reference
                                        )
                                    )
                                    message = "Transakcija uspešno posodobljena."
                                }
                            }

                            if (result != null) {
                                result.onFailure {
                                    message = "Napaka: ${it.message}"
                                }
                            }

                            onBackClick()
                        }
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(backgroundColor = colors.primary)
                ) {
                    Text("Shrani", color = colors.onPrimary)
                }

                Button(
                    onClick = { showDeleteDialog = true },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(backgroundColor = colors.error)
                ) {
                    Text("Izbriši", color = colors.onError)
                }

                OutlinedButton(
                    onClick = onBackClick,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(backgroundColor = colors.secondary)
                ) {
                    Text("Nazaj")
                }
            }
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

                                message = result.fold(
                                    onSuccess = { "Transakcija uspešno izbrisana." },
                                    onFailure = { "Napaka pri brisanju: ${it.message}" }
                                )

                                if (result.isSuccess == true) {
                                    onTransactionDeleted()
                                }
                            }
                        }
                    ) {
                        Text("Da, izbriši", color = colors.primary)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = false }) {
                        Text("Prekliči", color = colors.primary)
                    }
                },
                backgroundColor = colors.surface,
                contentColor = colors.onSurface
            )
        }
    }
}

