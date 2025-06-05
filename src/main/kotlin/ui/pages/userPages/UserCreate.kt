package ui.pages.userPages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.*
import androidx.compose.material.OutlinedTextField
import ui.api.createUser

val client = HttpClient(CIO) {
    install(ContentNegotiation) {
        json(
            kotlinx.serialization.json.Json {
                ignoreUnknownKeys = true
            }
        )
    }
    install(Logging) {
        logger = Logger.DEFAULT
        level = LogLevel.ALL
    }
}


@Composable
fun UserCreate() {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var dateOfBirth by remember { mutableStateOf("2025.05.27") }
    var password by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }

    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            elevation = 8.dp,
            modifier = Modifier.fillMaxWidth().padding(16.dp).width(400.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp).fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Vnesi podatke uporabnika", style = MaterialTheme.typography.h5)
                Spacer(Modifier.height(16.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = dateOfBirth,
                    onValueChange = { dateOfBirth = it },
                    label = { Text("Date of Birth (yyyy-MM-dd)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = surname,
                    onValueChange = { surname = it },
                    label = { Text("Surname") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Username") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))

                Button(
                    onClick = {
                        coroutineScope.launch {
                            val resultMessage = withContext(Dispatchers.IO) {
                                createUser(username, password, name, surname, email, dateOfBirth)
                            }
                            message = resultMessage
                        }
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFB2DFDB)),
                    shape = RoundedCornerShape(16.dp),
                    elevation = ButtonDefaults.elevation(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Shrani uporabnika", color = Color(0xFF004D40))
                }

                if (message.isNotEmpty()) {
                    Spacer(Modifier.height(16.dp))
                    Text(message, color = if (message.startsWith("Napaka")) Color.Red else Color.Green)
                }
            }
        }
    }
}
