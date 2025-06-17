package ui.pages.userPages

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ui.AuthState
import ui.api.deleteUser
import ui.api.updateUser
import ui.dataClasses.user.User
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

@Composable
fun UserEdit(
    initialUser: User = User(),
    onBackClick: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val userId = initialUser.id ?: ""

    var username by remember { mutableStateOf(initialUser.username ?: "") }
    var name by remember { mutableStateOf(initialUser.name ?: "") }
    var surname by remember { mutableStateOf(initialUser.surname ?: "") }
    var email by remember { mutableStateOf(initialUser.email ?: "") }
    var dateOfBirth by remember { mutableStateOf(initialUser.dateOfBirth ?: "") }
    var identifier by remember { mutableStateOf(initialUser.identifier ?: "") }
    var isAdmin by remember { mutableStateOf(initialUser.isAdmin) }
    var password by remember { mutableStateOf("") }
    var repeatPassword by remember { mutableStateOf("") }
    var message by remember { mutableStateOf<String?>(null) }

    val passwordsMatch = password.isEmpty() || password == repeatPassword

    Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()), // Dodan scroll
            verticalArrangement = Arrangement.Top
        ) {
            // Ostala vsebina (ostane enaka) ...

            Text("Uredi uporabnika", style = MaterialTheme.typography.h5)

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Uporabniško ime") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Ime") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = surname,
                onValueChange = { surname = it },
                label = { Text("Priimek") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = dateOfBirth,
                onValueChange = { dateOfBirth = it },
                label = { Text("Datum rojstva (d.M.yyyy)") },
                placeholder = { Text("7.10.2004") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = identifier,
                onValueChange = { identifier = it },
                label = { Text("Identifikator") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Geslo") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true
            )
            OutlinedTextField(
                value = repeatPassword,
                onValueChange = { repeatPassword = it },
                label = { Text("Ponovi geslo") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true
            )

            Row(
                Modifier.padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(checked = isAdmin, onCheckedChange = { isAdmin = it })
                Spacer(Modifier.width(8.dp))
                Text("Administrator")
            }

            message?.let {
                Spacer(Modifier.height(16.dp))
                Text(
                    text = it,
                    color = if (it.contains(
                            "uspešno",
                            true
                        )
                    ) colors.primary else colors.error
                )
            }

            Spacer(modifier = Modifier.weight(1f)) // Pushes buttons to the bottom

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        coroutineScope.launch {
                            message = null
                            if (!passwordsMatch) {
                                message = "Gesli se ne ujemata!"
                                return@launch
                            }
                            val token = AuthState.token
                            val result = token?.let {
                                updateUser(
                                    username = username,
                                    password = password,
                                    name = name,
                                    surname = surname,
                                    email = email,
                                    dateOfBirth = dateOfBirth,
                                    isAdmin = isAdmin,
                                    userId = userId,
                                )
                            }
                            if (result != null) {
                                message = result.fold(
                                    onSuccess = { "Uporabnik uspešno posodobljen." },
                                    onFailure = { "Posodobitev ni uspela: ${it.message}" }
                                )
                            }
                        }
                    },
                    modifier = Modifier.weight(1f),
                    enabled = passwordsMatch
                ) {
                    Text("Shrani")
                }

                Button(
                    onClick = {
                        coroutineScope.launch {
                            message = null
                            val result = deleteUser(userId)
                            message = result.fold(
                                onSuccess = { "Uporabnik uspešno izbrisan." },
                                onFailure = { "Brisanje ni uspelo: ${it.message}" }
                            )
                        }
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(backgroundColor = colors.error)
                ) {
                    Text("Izbriši", color = colors.onError)
                }

                OutlinedButton(
                    onClick = onBackClick,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(backgroundColor = colors.secondary),
                ) {
                    Text("Nazaj")
                }
            }
        }
    }
}


