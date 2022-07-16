import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabTimeline() {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(24.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp)
    ) {
        item {
            Text("Timeline not done")
        }
    }
}