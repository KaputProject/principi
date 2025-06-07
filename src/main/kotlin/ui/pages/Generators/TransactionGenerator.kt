package ui.pages.Generators

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.serpro69.kfaker.Faker
import kotlinx.coroutines.launch
import ui.api.createTransaction
import ui.api.getAccounts
import ui.components.cards.TransactionCard
import ui.dataClasses.account.Account
import ui.dataClasses.account.toAccountInfo
import ui.dataClasses.transaction.Transaction
import ui.dataClasses.transaction.TransactionCreate
import ui.dataClasses.transaction.TransactionResponse
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.random.Random

@Composable
fun TransactionGenerator(userId: String, onBackClick : () -> Unit) {
    val faker = remember { Faker() }
    val coroutineScope = rememberCoroutineScope()

    var transactionCountInput by remember { mutableStateOf("") }
    var statusMessage by remember { mutableStateOf<String?>(null) }
    var isGenerating by remember { mutableStateOf(false) }
    var generatedTransactions by remember { mutableStateOf(listOf<TransactionResponse>()) }

    var accounts by remember { mutableStateOf(listOf<Account>()) }
    var selectedAccount by remember { mutableStateOf<Account?>(null) }

    LaunchedEffect(Unit) {
        accounts = getAccounts(userId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Generiraj transakcije", style = MaterialTheme.typography.h4)
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = transactionCountInput,
            onValueChange = { if (it.all(Char::isDigit)) transactionCountInput = it },
            label = { Text("Koliko transakcij želite generirati?") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (accounts.isNotEmpty()) {
            var expanded by remember { mutableStateOf(false) }

            Box(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
                OutlinedButton(
                    onClick = { expanded = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(selectedAccount?.iban ?: "Izberite račun")
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    accounts.forEach { account ->
                        DropdownMenuItem(onClick = {
                            selectedAccount = account
                            expanded = false
                        }) {
                            Text(text = account.iban)
                        }
                    }
                }
            }
        }

        Button(
            onClick = {
                val count = transactionCountInput.toIntOrNull() ?: 0
                if (count <= 0) {
                    statusMessage = "Vnesite veljavno število."
                    return@Button
                }
                if (selectedAccount == null) {
                    statusMessage = "Izberite račun za generacijo transakcij."
                    return@Button
                }

                isGenerating = true
                statusMessage = null
                generatedTransactions = emptyList()

                coroutineScope.launch {
                    val account = selectedAccount!!
                    val transactions = mutableListOf<TransactionResponse>()
                    var successCount = 0
                    var failedCount = 0

                    repeat(count) {
                        val amount = Random.nextDouble(1.0, 500.0)
                        val outgoing = Random.nextBoolean()
                        val now = LocalDateTime.now().minusDays(Random.nextLong(0, 30))
                        val formattedDate = now.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)

                        val request = TransactionCreate(
                            userId = userId,
                            account = account.toAccountInfo(),
                            location = null,
                            datetime = formattedDate,
                            description = faker.name.name() + " generiana transakcija",
                            change = amount,
                            outgoing = outgoing,
                            reference = null
                        )

                        try {
                            val response = createTransaction(request)
                            transactions.add(response)
                            successCount++
                        } catch (e: Exception) {
                            failedCount++
                        }
                    }

                    generatedTransactions = transactions
                    statusMessage = "Generacija končana. Uspešno: $successCount, Neuspešno: $failedCount"
                    isGenerating = false
                }
            },
            enabled = !isGenerating,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (isGenerating) "Generiram..." else "Generiraj transakcije")
        }

        Spacer(modifier = Modifier.height(16.dp))

        statusMessage?.let {
            Text(it, style = MaterialTheme.typography.body1)
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (generatedTransactions.isNotEmpty()) {
            Text("Generirane transakcije:", style = MaterialTheme.typography.h6)
            Spacer(modifier = Modifier.height(8.dp))

            fun TransactionResponse.toTransaction(): Transaction = this.transaction

            LazyColumn {
                items(generatedTransactions) { transactionResponse ->
                    TransactionCard(
                        transaction = transactionResponse.toTransaction(),
                        onClick = {}
                    )
                }
            }
        }
        Button(
            onClick = onBackClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Nazaj")
        }
    }
}
