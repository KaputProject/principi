package ui.pages.Generators

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.serpro69.kfaker.Faker
import kotlinx.coroutines.launch
import ui.api.createUser
import java.time.LocalDate
import kotlin.random.Random
import ui.dataClasses.user.User
import java.util.*
import androidx.compose.foundation.lazy.LazyColumn
import ui.components.cards.UserCard

@Composable
fun UserGenerator() {
    val faker = remember { Faker() }
    val coroutineScope = rememberCoroutineScope()
    var userCountInput by remember { mutableStateOf("") }
    var statusMessage by remember { mutableStateOf<String?>(null) }
    var isGenerating by remember { mutableStateOf(false) }
    var generatedUsers by remember { mutableStateOf(listOf<User>()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Generiraj uporabnike", style = MaterialTheme.typography.h4)
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = userCountInput,
            onValueChange = { input ->
                if (input.all { it.isDigit() }) userCountInput = input
            },
            label = { Text("Koliko uporabnikov želite generirati?") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                val count = userCountInput.toIntOrNull() ?: 0
                if (count <= 0) {
                    statusMessage = "Vnesite veljavno pozitivno število."
                    return@Button
                }
                isGenerating = true
                statusMessage = null
                generatedUsers = emptyList()

                coroutineScope.launch {
                    var successCount = 0
                    var failedCount = 0
                    val newUsers = mutableListOf<User>()

                    for (i in 1..count) {
                        try {
                            val username = faker.name.name().lowercase() + Random.nextInt(1, 99)
                            val password = "Password"
                            val name = faker.name.firstName()
                            val surname = faker.name.lastName()
                            val email = faker.internet.email()
                            val dateOfBirth = randomBirthday().toString()
                            val identifier = (surname + name).uppercase(Locale.getDefault())

                            val result = createUser(
                                username = username,
                                password = password,
                                name = name,
                                surname = surname,
                                email = email,
                                dateOfBirth = dateOfBirth
                            )

                            if (result.contains("uspešno")) {
                                successCount++
                                newUsers.add(
                                    User(
                                        username = username,
                                        name = name,
                                        surname = surname,
                                        email = email,
                                        dateOfBirth = dateOfBirth,
                                        identifier = identifier,
                                        accounts = emptyList(),
                                        isAdmin = false,
                                        locations = emptyList()
                                    )
                                )
                            } else {
                                failedCount++
                            }
                        } catch (e: Exception) {
                            failedCount++
                        }
                    }

                    generatedUsers = newUsers
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

        if (generatedUsers.isNotEmpty()) {
            Text("Generirani uporabniki:", style = MaterialTheme.typography.h6)
            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 8.dp),
                contentPadding = PaddingValues(bottom = 64.dp)
            ) {
                items(generatedUsers.size) { index ->
                    val user = generatedUsers[index]
                    UserCard(
                        name = user.name ?: "",
                        surname = user.surname ?: "",
                        email = user.email,
                        dateOfBirth = user.dateOfBirth,
                        onClick = {}
                    )
                }
            }
        }
    }
}


fun randomBirthday(): LocalDate {
    val startEpochDay = LocalDate.of(1950, 1, 1).toEpochDay()
    val endEpochDay = LocalDate.of(2005, 12, 31).toEpochDay()
    val randomDay = Random.nextLong(startEpochDay, endEpochDay)
    return LocalDate.ofEpochDay(randomDay)
}

