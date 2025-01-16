package hu.bme.aut.U5FT4N.easyshopping.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.easyshopping.R

@Composable
fun SummaryScreen(
    totalItems: Int,
    foodItemsCount: Int,
    electronicsItemsCount: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.summary_of_items),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge // Material 3 style
        )

        // Display total number of items
        Text(
            text = "Total Items: $totalItems",
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge
        )

        // Display count of Food items
        Text(
            text = "Food Items: $foodItemsCount",
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge
        )

        // Display count of Electronics items
        Text(
            text = "Electronics Items: $electronicsItemsCount",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
