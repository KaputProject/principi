package ui.pages.userPages

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ui.dataClasses.user.User

@Composable
fun UserMenuPage(
    user: User,
    onEditClick: (User) -> Unit,
    onAccountClick: (User) -> Unit,
    onLocationClick: (User) -> Unit,
    onBackClick: () -> Unit
) {
    println("UserMenuPage opened with user: ${user.name}, ${user.email}")

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(">>> User Menu Page <<<", style = MaterialTheme.typography.h5)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Name: ${user.name ?: "Unknown"}")
        Text("Email: ${user.email ?: "Unknown"}")
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = { onEditClick(user) }) {
            Text("Edit User")
        }
        Button(onClick = { onAccountClick(user) }) {
            Text("See Accounts")
        }
        Button(onClick = { onLocationClick(user) }) {
            Text("See Locations")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onBackClick) {
            Text("Nazaj")
        }
    }
}

