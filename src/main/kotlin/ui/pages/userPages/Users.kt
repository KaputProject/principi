package ui.pages.userPages

import ui.dataClasses.user.User
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ui.api.users
import ui.components.cards.UserCard
import androidx.compose.material.*
import androidx.compose.ui.Alignment


@Composable
fun Users(
    onNavigate: (User) -> Unit,
    onGoToUserGenerator: () -> Unit
) {
    var usersState by remember { mutableStateOf<List<User>>(emptyList()) }
    var filterUsername by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            val fetchedUsers = users()
            println("Fetched users: $fetchedUsers")
            usersState = fetchedUsers
        }
    }

    val filteredUsers = if (filterUsername.isBlank()) {
        usersState
    } else {
        usersState.filter { user ->
            val username = user.name ?: ""
            username.contains(filterUsername, ignoreCase = true)
        }
    }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Users", style = MaterialTheme.typography.h5)
            Button(onClick = onGoToUserGenerator) {
                Text("Generiraj uporabnika")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = filterUsername,
            onValueChange = { filterUsername = it },
            label = { Text("Filter po uporabniškem imenu") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        )

        if (filteredUsers.isEmpty()) {
            Text("Ni najdenih uporabnikov za prikaz.")
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(8.dp)
            ) {
                items(filteredUsers.size) { index ->
                    val user = filteredUsers[index]
                    UserCard(
                        name = user.name ?: "No Name",
                        surname = user.surname ?: "",
                        email = user.email,
                        dateOfBirth = user.dateOfBirth,
                        onClick = { onNavigate(user) }
                    )
                }
            }
        }
    }
}




