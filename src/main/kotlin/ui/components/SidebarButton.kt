package ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SidebarButton(label: String, onClick: () -> Unit) {
    val colors = MaterialTheme.colors
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = colors.surface,  // uporabi surface barvo teme
            contentColor = colors.onSurface    // barvo teksta glede na surface
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = ButtonDefaults.elevation(4.dp)
    ) {
        Text(label)
    }
}
