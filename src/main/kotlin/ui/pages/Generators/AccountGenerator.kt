import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.serpro69.kfaker.Faker
import kotlinx.coroutines.launch
import ui.api.createAccount
import ui.components.cards.AccountCard
import ui.dataClasses.account.Account
import ui.enums.Currency
import kotlin.random.Random

@Composable
fun AccountGenerator(userId: String, onBackClick: () -> Unit) {
    val faker = remember { Faker() }
    val coroutineScope = rememberCoroutineScope()
    var accountCountInput by remember { mutableStateOf("") }
    var statusMessage by remember { mutableStateOf<String?>(null) }
    var isGenerating by remember { mutableStateOf(false) }
    var generatedAccounts by remember { mutableStateOf(listOf<Account>()) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp) // Dovolj prostora za gumb
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Text("Generiraj račune", style = MaterialTheme.typography.h4)
            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = accountCountInput,
                onValueChange = { if (it.all(Char::isDigit)) accountCountInput = it },
                label = { Text("Koliko računov želite generirati?") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    val count = accountCountInput.toIntOrNull() ?: 0
                    if (count <= 0) {
                        statusMessage = "Vnesite veljavno pozitivno število."
                        return@Button
                    }
                    isGenerating = true
                    statusMessage = null
                    generatedAccounts = emptyList()

                    coroutineScope.launch {
                        val newAccounts = mutableListOf<Account>()
                        var successCount = 0
                        var failedCount = 0

                        for (i in 1..count) {
                            val iban = "SI56 ${Random.nextInt(1000, 9999)} ${Random.nextInt(1000, 9999)} ${Random.nextInt(1000, 9999)} ${Random.nextInt(100, 999)}"
                            val currency1 = Currency.entries.random()
                            val balance = Random.nextDouble(0.0, 10000.0)
                            val statements: List<String> = listOf("")

                            val result = createAccount(
                                userId = userId,
                                iban = iban,
                                currency = currency1.toString(),
                                balance = balance
                            )

                            if (result.isSuccess) {
                                successCount++
                                newAccounts.add(
                                    Account(
                                        user = userId,
                                        statements = statements,
                                        _id = "",
                                        iban = iban,
                                        currency = currency1.toString(),
                                        balance = balance,
                                    )
                                )
                            } else {
                                failedCount++
                            }
                        }

                        generatedAccounts = newAccounts
                        statusMessage = "Generacija zaključena. Uspešno: $successCount, Neuspešno: $failedCount"
                        isGenerating = false
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

            if (generatedAccounts.isNotEmpty()) {
                Text("Generirani računi:", style = MaterialTheme.typography.h6)
                Spacer(modifier = Modifier.height(8.dp))

                generatedAccounts.forEach { account ->
                    AccountCard(account = account, onClick = {})
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // Fixed gumb na dnu zaslona
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Button(
                onClick = onBackClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Nazaj")
            }
        }
    }
}
