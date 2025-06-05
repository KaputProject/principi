import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ui.components.cards.TransactionCard
import ui.dataClasses.transaction.Transaction
import ui.dataClasses.user.User

@Composable
fun Transactions(
    transactions: List<Transaction>,
    onTransactionSelected: (Transaction ) -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()

    Box(modifier = modifier.padding(start = 8.dp).background(MaterialTheme.colors.surface)) {
        if (transactions.isEmpty()) {
            Text("Ni transakcij.", modifier = Modifier.padding(8.dp))
        } else {
            Box(Modifier.fillMaxSize()) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(end = 12.dp),
                    state = listState
                ) {
                    items(transactions) { transaction ->
                        TransactionCard(
                            transaction = transaction,
                            onClick = { onTransactionSelected(transaction) }
                        )
                    }
                }

                VerticalScrollbar(
                    adapter = rememberScrollbarAdapter(listState),
                    modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight()
                )
            }
        }
    }
}