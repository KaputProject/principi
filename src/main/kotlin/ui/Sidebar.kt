package ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ui.components.SidebarButton

@Composable
fun Sidebar(currentPage: Int, onNavigate: (Int) -> Unit) {
    var expandedSection by remember { mutableStateOf<String?>(null) }

    Column(
        Modifier
            .fillMaxHeight()
            .width(180.dp)
            .background(Color(0xFFF2F2F2)),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
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

        SidebarButton("Accounts ${if (expandedSection == "accounts") "▲" else "▼"}") {
            expandedSection = if (expandedSection == "accounts") null else "accounts"
        }
        if (expandedSection == "accounts") {
            Column(Modifier.padding(start = 16.dp)) {
                SidebarButton("All Accounts") { onNavigate(5) }
                SidebarButton("Create Account") { onNavigate(6) }
            }
        }

        Spacer(Modifier.height(8.dp))

        SidebarButton("Scraper") { onNavigate(3) }
        SidebarButton("Generator") { onNavigate(4) }
    }
}
