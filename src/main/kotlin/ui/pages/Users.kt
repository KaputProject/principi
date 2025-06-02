package ui.pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ui.components.PersonCard

@Composable
fun Users() {
    val people = listOf(
        "Daysi" to "ZBONCAK", "Brady" to "BERGNAUM", "Simon" to "WEST", "Wendell" to "SCHILLER"
    )

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("People", style = MaterialTheme.typography.h5, modifier = Modifier.padding(bottom = 12.dp))
        LazyVerticalGrid(columns = GridCells.Fixed(2), modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(8.dp)) {
            items(people.size) { index ->
                val (name, surname) = people[index]
                PersonCard(name, surname)
            }
        }
    }
}
