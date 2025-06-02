import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ui.Header
import ui.components.SidebarButton
import ui.pages.*
import androidx.compose.ui.Alignment
import kotlinx.serialization.Serializable
import ui.Sidebar

@Serializable
data class UploadResponse(
    val message: String, val filename: String? = null, val metadata: String? = null, val ime: String? = null
)

@Composable
fun App() {
    var currentPage by remember { mutableStateOf(1) }

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
                        2 -> Users()
                        3 -> Scraper()
                        4 -> Generator()
                        5 -> Accounts()
                        6 -> AccountCreate()
                    }
                }
            }
        }
    }
}

