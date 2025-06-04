package ui.pages.userPages

import ui.dataClasses.user.User
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ui.api.users
import ui.components.UserCard

@Composable
fun Users(onNavigate: (User) -> Unit) {
    var usersState by remember { mutableStateOf<List<User>>(emptyList()) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            val fetchedUsers = users()
            println("Fetched users: $fetchedUsers")
            usersState = fetchedUsers
        }
    }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Users", style = MaterialTheme.typography.h5, modifier = Modifier.padding(bottom = 12.dp))

        if (usersState.isEmpty()) {
            Text("No users found or loading...")
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(8.dp)
            ) {
                items(usersState.size) { index ->
                    val user = usersState[index]
                    UserCard(
                        name = user.name ?: "No Name",
                        surname = user.surname ?: "",
                        email = user.email,
                        dateOfBirth = user.dateOfBirth,
                        onClick = {
                            onNavigate(user)
                        }
                    )
                }
            }
        }
    }
}






