package ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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

    Column(
        Modifier
            .fillMaxHeight()
            .width(180.dp)
            .background(Color(0xFFF2F2F2)),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.Start
    ) {
        Column {
            Spacer(Modifier.height(24.dp))

            SidebarButton("Users ${if (expandedSection == "users") "▲" else "▼"}") {
                expandedSection = if (expandedSection == "users") null else "users"
            }
            if (expandedSection == "users") {
                Column(Modifier.padding(start = 16.dp)) {
                    SidebarButton("All Users") { onNavigate(2) }
                    SidebarButton("Create User") { onNavigate(1) }
                }
            }


            Spacer(Modifier.height(8.dp))

            SidebarButton("Scraper") { onNavigate(3) }
            SidebarButton("Generator") { onNavigate(4) }
        }

        Box(Modifier.padding(12.dp).fillMaxWidth()) {
            if (AuthState.token == null) {
                Column {
                    OutlinedTextField(
                        value = usernameInput,
                        onValueChange = { usernameInput = it },
                        label = { Text("Username") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(4.dp))
                    OutlinedTextField(
                        value = passwordInput,
                        onValueChange = { passwordInput = it },
                        label = { Text("Password") },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(8.dp))
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
                    Text("Hello, ${AuthState.username ?: "Admin"}", modifier = Modifier.weight(1f))
                    Button(
                        onClick = {
                            AuthState.token = null
                            AuthState.username = null
                        }
                    ) {
                        Text("Logout")
                    }
                }
            }
        }
    }
}