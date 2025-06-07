package ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ui.AuthState
import ui.api.login
import ui.components.SidebarButton

@Composable
fun Sidebar(currentPage: Int, onNavigate: (Int) -> Unit) {
    var expandedSection by remember { mutableStateOf<String?>(null) }
    var usernameInput by remember { mutableStateOf("") }
    var passwordInput by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    val colors = MaterialTheme.colors
    val typography = MaterialTheme.typography

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(200.dp)
            .background(colors.surface)
            .padding(8.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.Start
    ) {
        Column {
            Spacer(Modifier.height(16.dp))

            SidebarButton("Users ${if (expandedSection == "users") "▲" else "▼"}") {
                expandedSection = if (expandedSection == "users") null else "users"
            }
            if (expandedSection == "users") {
                Column(Modifier.padding(start = 16.dp)) {
                    SidebarButton("All Users") { onNavigate(2) }
                    SidebarButton("Create User") { onNavigate(1) }
                }
            }

            Spacer(Modifier.height(12.dp))

            SidebarButton("Scraper") { onNavigate(3) }
            //SidebarButton("Generator") { onNavigate(4) }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            if (AuthState.token == null) {
                Column {
                    OutlinedTextField(
                        value = usernameInput,
                        onValueChange = { usernameInput = it },
                        label = { Text("Username", style = typography.subtitle1) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(6.dp))
                    OutlinedTextField(
                        value = passwordInput,
                        onValueChange = { passwordInput = it },
                        label = { Text("Password", style = typography.subtitle1) },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(10.dp))
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                val token = login(usernameInput, passwordInput)
                                if (token != null) {
                                    AuthState.token = token
                                    AuthState.username = usernameInput
                                    usernameInput = ""
                                    passwordInput = ""
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = colors.primary,
                            contentColor = colors.onPrimary
                        ),
                        shape = MaterialTheme.shapes.medium,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Login")
                    }
                }
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "Hello, ${AuthState.username ?: "Admin"}",
                        style = typography.body1,
                        modifier = Modifier.weight(1f)
                    )
                    Button(
                        onClick = {
                            AuthState.token = null
                            AuthState.username = null
                        },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = colors.secondary,
                            contentColor = colors.onSecondary
                        ),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Text("Logout")
                    }
                }
            }
        }
    }
}
