package ui.pages


import User
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


import ui.api.users
import ui.components.UserCard
@Composable
fun Users(onNavigate: (User) -> Unit) {
    val usersState = produceState<List<User>>(initialValue = emptyList()) {
        value = users()
    }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Users", style = MaterialTheme.typography.h5, modifier = Modifier.padding(bottom = 12.dp))

        if (usersState.value.isEmpty()) {
            Text("No users found or loading...")
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(8.dp)
            ) {
                items(usersState.value.size) { index ->
                    val user = usersState.value[index]
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





