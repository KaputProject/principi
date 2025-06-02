package ui.pages

import User
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ui.AuthState
import ui.api.updateUser

@Composable
fun UserEditPage(initialUser: User = User()) {
    val coroutineScope = rememberCoroutineScope()

    var username by remember { mutableStateOf(initialUser.username ?: "") }
    var name by remember { mutableStateOf(initialUser.name ?: "") }
    var surname by remember { mutableStateOf(initialUser.surname ?: "") }
    var email by remember { mutableStateOf(initialUser.email ?: "") }
    var dateOfBirth by remember { mutableStateOf(initialUser.dateOfBirth ?: "") }
    var isAdmin by remember { mutableStateOf(initialUser.isAdmin ?: false) }
    var password by remember { mutableStateOf("") }
    var repeatPassword by remember { mutableStateOf("") }
    val userId = initialUser.id ?: ""

    var message by remember { mutableStateOf<String?>(null) }
    val initialUserJson = remember(initialUser) { Json.encodeToString(initialUser) }
    val currentUser = User(
        username = username,
        name = name,
        surname = surname,
        email = email,
        dateOfBirth = dateOfBirth,
        isAdmin = isAdmin
    )
    val currentUserJson = Json.encodeToString(currentUser)

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text("Edit User", style = MaterialTheme.typography.h5)
        Text("User ID: $userId", style = MaterialTheme.typography.body2)

        Spacer(Modifier.height(12.dp))

        Text("Original User JSON:", style = MaterialTheme.typography.subtitle2)
        Text(initialUserJson, style = MaterialTheme.typography.body2)

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = surname,
            onValueChange = { surname = it },
            label = { Text("Surname") },
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
            onValueChange = { input -> dateOfBirth = input },
            label = { Text("Date of Birth (d.M.yyyy)") },
            placeholder = { Text("7.10.2004") },
            modifier = Modifier.fillMaxWidth()
        )


        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true
        )

        OutlinedTextField(
            value = repeatPassword,
            onValueChange = { repeatPassword = it },
            label = { Text("Repeat Password") },
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
            Text("Is Admin")
        }

        Spacer(Modifier.height(20.dp))

        Button(
            onClick = {
                coroutineScope.launch {
                    message = null
                    if (password.isNotEmpty() && password != repeatPassword) {
                        message = "Passwords do not match!"
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
                            onSuccess = { "User updated successfully" },
                            onFailure = { "Update failed: ${it.message}" }
                        )
                    }

                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save")
        }

    }
}
