package hu.bme.aut.U5FT4N.easyshopping.ui.screen.money

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.U5FT4N.easyshopping.data.money.MoneyResult
import hu.bme.aut.U5FT4N.easyshopping.network.MoneyAPI
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class MoneyViewModel @Inject constructor(
    val moneyAPI: MoneyAPI
) : ViewModel() {
    var moneyUiState: MoneyUiState by mutableStateOf(MoneyUiState.Init)
    var conversionRates: MoneyResult? by mutableStateOf(null) // Holds the latest fetched rates

    fun getRates() {
        moneyUiState = MoneyUiState.Loading
        viewModelScope.launch {
            moneyUiState = try {
                val result = moneyAPI.getRates()
                conversionRates = result // Store rates
                MoneyUiState.Success(result)
            } catch (e: IOException) {
                MoneyUiState.Error
            } catch (e: HttpException) {
                MoneyUiState.Error
            } catch (e: Exception) {
                e.printStackTrace()
                MoneyUiState.Error
            }
        }
    }
    fun fetchLatestRates() {
        getRates() // Call the existing method to fetch the latest rates
    }
}

sealed interface MoneyUiState {
    object Init : MoneyUiState
    data class Success(val moneyRates: MoneyResult) : MoneyUiState
    object Error : MoneyUiState
    object Loading : MoneyUiState
}