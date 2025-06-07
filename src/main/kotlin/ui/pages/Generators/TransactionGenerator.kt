import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.serpro69.kfaker.Faker
import kotlinx.coroutines.launch
import kotlin.random.Random
import ui.api.createTransaction
import ui.api.showAccount
import ui.api.showLocation
import ui.components.cards.TransactionCard
import ui.dataClasses.account.Account
import ui.dataClasses.account.AccountInfo
import ui.dataClasses.locations.Location
import ui.dataClasses.transaction.Transaction
import ui.dataClasses.transaction.TransactionCreate
import java.util.*
@Composable
fun TransactionGenerator(
    userId: String,
    accounts: List<String>,  // so ID-ji
    locations: List<String>, // so ID-ji
    onBackClick: () -> Unit,
    onGenerated: () -> Unit
) {
    var fullAccounts by remember { mutableStateOf<List<Account>>(emptyList()) }
    var fullLocations by remember { mutableStateOf<List<Location>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    var selectedAccount by remember { mutableStateOf<Account?>(null) }
    var selectedLocation by remember { mutableStateOf<Location?>(null) }

    var transactionCountInput by remember { mutableStateOf("") }
    var statusMessage by remember { mutableStateOf<String?>(null) }
    var isGenerating by remember { mutableStateOf(false) }
    var generatedTransactions by remember { mutableStateOf(listOf<Transaction>()) }

    val faker = remember { Faker() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(accounts, locations) {
        isLoading = true
        errorMessage = null
        try {
            val loadedAccounts = mutableListOf<Account>()
            val loadedLocations = mutableListOf<Location>()

            for (accountId in accounts) {
                val result = showAccount(userId, accountId)
                if (result.isSuccess) {
                    loadedAccounts.add(result.getOrThrow())
                } else {
                    errorMessage = "Napaka pri nalaganju računa ID=$accountId: ${result.exceptionOrNull()?.message}"
                }
            }

            for (locationId in locations) {
                val result = showLocation(userId, locationId)
                if (result.isSuccess) {
                    loadedLocations.add(result.getOrThrow())
                } else {
                    errorMessage = "Napaka pri nalaganju lokacije ID=$locationId: ${result.exceptionOrNull()?.message}"
                }
            }

            fullAccounts = loadedAccounts
            fullLocations = loadedLocations
        } catch (e: Exception) {
            errorMessage = "Napaka pri nalaganju podatkov: ${e.message}"
        } finally {
            isLoading = false
        }
    }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    if (errorMessage != null) {
        Text("Napaka: $errorMessage", color = MaterialTheme.colors.error)
        return
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Generiraj transakcije", style = MaterialTheme.typography.h4)
        Spacer(modifier = Modifier.height(24.dp))

        Text("Izberi račun za transakcije:")
        Spacer(modifier = Modifier.height(8.dp))
        DropdownMenuSelector(
            items = fullAccounts,
            selectedItem = selectedAccount,
            onItemSelected = { selectedAccount = it },
            itemLabel = { "${it.iban} (${it.currency}, ${it.balance})" }
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text("Izberi lokacijo (opcijsko):")
        Spacer(modifier = Modifier.height(8.dp))
        DropdownMenuSelector(
            items = listOf<Location?>(null) + fullLocations,
            selectedItem = selectedLocation,
            onItemSelected = { selectedLocation = it },
            itemLabel = { it?.name ?: "Ni izbrane" }
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = transactionCountInput,
            onValueChange = { if (it.all(Char::isDigit)) transactionCountInput = it },
            label = { Text("Koliko transakcij želite generirati?") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                val count = transactionCountInput.toIntOrNull() ?: 0
                if (count <= 0) {
                    statusMessage = "Vnesite veljavno pozitivno število."
                    return@Button
                }
                if (selectedAccount == null) {
                    statusMessage = "Izberite račun."
                    return@Button
                }

                isGenerating = true
                statusMessage = null
                generatedTransactions = emptyList()

                coroutineScope.launch {
                    var successCount = 0
                    var failedCount = 0
                    val newTransactions = mutableListOf<Transaction>()

                    for (i in 1..count) {
                        val description = faker.string.toString()
                        val amount = Random.nextDouble(10.0, 1000.0)
                        val outgoing = Random.nextBoolean()
                        val now = System.currentTimeMillis()
                        val randomDate = Date(now - Random.nextLong(0, 10_000_000_000))
                        val formattedDate = java.time.format.DateTimeFormatter.ofPattern("dd.MM.yyyy")
                            .format(randomDate.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate())

                        val transactionRequest = TransactionCreate(
                            userId = userId,
                            account = AccountInfo(
                                iban = selectedAccount!!.iban,
                                _id = selectedAccount!!._id
                            ),
                            location = selectedLocation?._id,
                            datetime = formattedDate,
                            description = description,
                            change = if (outgoing) -amount else amount,
                            outgoing = outgoing,
                            reference = null,
                        )

                        val result = try {
                            val response = createTransaction(transactionRequest)
                            Result.success(response)
                        } catch (e: Exception) {
                            Result.failure<Transaction>(e)
                        }

                        if (result.isSuccess) {
                            successCount++
                            newTransactions.add(result.getOrNull()!! as Transaction)
                        } else {
                            failedCount++
                        }
                    }

                    generatedTransactions = newTransactions
                    statusMessage = "Generacija zaključena. Uspešno: $successCount, Neuspešno: $failedCount"
                    isGenerating = false
                    onGenerated()
                }
            },
            enabled = !isGenerating,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (isGenerating) "Generiram..." else "Generiraj")
        }

        Spacer(modifier = Modifier.height(16.dp))

        statusMessage?.let {
            Text(text = it, style = MaterialTheme.typography.body1)
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (generatedTransactions.isNotEmpty()) {
            Text("Generirane transakcije:", style = MaterialTheme.typography.h6)
            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn {
                items(generatedTransactions) { transaction ->
                    TransactionCard(transaction = transaction, onClick = {})
                }
            }
        }
    }
}

@Composable
fun <T> DropdownMenuSelector(
    items: List<T>,
    selectedItem: T?,
    onItemSelected: (T) -> Unit,
    itemLabel: (T) -> String
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        OutlinedTextField(
            value = selectedItem?.let { itemLabel(it) } ?: "",
            onValueChange = {},
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            label = { Text("Izberi") },
            trailingIcon = {
                IconButton(onClick = { expanded = true }) {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = "Izberi")
                }
            }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEach { item ->
                DropdownMenuItem(onClick = {
                    onItemSelected(item)
                    expanded = false
                }) {
                    Text(itemLabel(item))
                }
            }
        }
    }
}
