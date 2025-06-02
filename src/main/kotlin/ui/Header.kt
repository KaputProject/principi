package ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun Header() {
    Box(
        modifier = Modifier.fillMaxWidth().height(60.dp).background(Color(0xFFE6F2F3)),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = "Kaput admin panel",
            color = Color(0xFF00796B),
            modifier = Modifier.padding(start = 16.dp),
            style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold)
        )
    }
}
