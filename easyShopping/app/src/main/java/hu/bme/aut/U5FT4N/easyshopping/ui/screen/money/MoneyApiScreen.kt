package hu.bme.aut.U5FT4N.easyshopping.ui.screen.money

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.easyshopping.R
import hu.bme.aut.U5FT4N.easyshopping.data.money.MoneyResult

@Composable
fun MoneyApiScreen(
    itemName: String,
    isInternetAvailable: Boolean,
    moneyViewModel: MoneyViewModel = hiltViewModel()
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Button(onClick = {
            moneyViewModel.getRates()
        },
            enabled = isInternetAvailable) {
            Text(text = stringResource(R.string.refresh))
        }
        when (moneyViewModel.moneyUiState) {
            is MoneyUiState.Init -> Text(stringResource(R.string.press_refresh_to_do_something))
            is MoneyUiState.Loading -> CircularProgressIndicator()
            is MoneyUiState.Success -> MoneyResultScreen(
                (moneyViewModel.moneyUiState as MoneyUiState.Success).moneyRates)
            is MoneyUiState.Error -> Text(text = stringResource(R.string.error))
        }
    }

}

@Composable
fun MoneyResultScreen(moneyRates: MoneyResult) {
    Column() {
        Text(text = "Base: USD")
        Text(text = "USD: ${moneyRates.rates?.uSD}")
        Text(text = "EUR: ${moneyRates.rates?.eUR}")
        Text(text = "HUF: ${moneyRates.rates?.hUF}")
        Text(text = "HUF: ${moneyRates.rates?.gBP}")
    }
}

