import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ui.Header
import ui.pages.*
import ui.Sidebar

@Composable
fun App() {
    var currentPage by remember { mutableStateOf(1) }
    var userToEdit by remember { mutableStateOf<User?>(null) }

    MaterialTheme {
        Column(Modifier.fillMaxSize().background(Color(0xFFF5F5F5))) {
            Header()

            Row(Modifier.weight(1f)) {
                Sidebar(currentPage = currentPage, onNavigate = { page ->
                    currentPage = page
                })

                Box(Modifier.fillMaxSize().background(Color.White).padding(8.dp)) {
                    when (currentPage) {
                        1 -> UserCreate()
                        2 -> Users(onNavigate = { selectedUser ->
                            userToEdit = selectedUser
                            currentPage = 7  // page 7 je UserEditPage
                        })
                        3 -> Scraper()
                        4 -> Generator()
                        5 -> Accounts()
                        6 -> AccountCreate()
                        7 -> userToEdit?.let { user ->
                            UserEditPage(initialUser = user)
                        } ?: Text("No user selected")
                        else -> Text("Page not found")
                    }
                }
            }
        }
    }
}


