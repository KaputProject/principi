package GUI
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState

fun main() = application {
    val windowState = rememberWindowState(
        position = WindowPosition.Aligned(Alignment.Center),
        width = 800.dp,
        height = 600.dp
    )

    Window(
        onCloseRequest = ::exitApplication,
        title = "Preprost GUI",
        state = windowState
    ) {
        MaterialTheme {
            Row(modifier = Modifier.fillMaxSize()) {
                // Levi del z vsebino (seznam)
                val listState = rememberLazyListState()
                val itemsList = remember { mutableStateListOf("Element 1", "Element 2", "Element 3") }

                Box(modifier = Modifier.weight(1f).fillMaxHeight().padding(16.dp)) {
                    LazyColumn(state = listState) {
                        items(itemsList) { item ->
                            Text(
                                text = item,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Normal,
                                modifier = Modifier
                                    .padding(8.dp)
                                    .fillMaxWidth()
                                    .background(Color.LightGray)
                                    .padding(8.dp)
                            )
                        }
                    }
                    VerticalScrollbar(
                        adapter = rememberScrollbarAdapter(listState),
                        modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight()
                    )
                }

                // Desna orodna vrstica
                Column(
                    modifier = Modifier
                        .width(150.dp)
                        .fillMaxHeight()
                        .background(Color(0xFFEEEEEE))
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text("Orodja", fontWeight = FontWeight.Bold, fontSize = 16.sp)

                    Button(onClick = {
                        itemsList.add("Element ${itemsList.size + 1}")
                    }) {
                        Text("Dodaj")
                    }

                    Button(onClick = {
                        if (itemsList.isNotEmpty()) itemsList.removeLast()
                    }) {
                        Text("Izbriši")
                    }

                    Button(onClick = {
                        itemsList.shuffle()
                    }) {
                        Text("Premešaj")
                    }
                }
            }
        }
    }
}
