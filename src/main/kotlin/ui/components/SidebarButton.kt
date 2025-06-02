package ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape

@Composable
fun SidebarButton(label: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 6.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFE6F2F3)),
        shape = RoundedCornerShape(12.dp),
        elevation = ButtonDefaults.elevation(4.dp)
    ) {
        Text(label, color = Color(0xFF004D40))
    }
}