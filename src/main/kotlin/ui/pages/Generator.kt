package ui.pages

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Generator(
    onGoToUserGenerator: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Številka strani: 4", style = MaterialTheme.typography.h4)
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = onGoToUserGenerator) {
                Text("Generiraj uporabnika")
            }
        }
    }
}


