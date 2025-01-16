package hu.bme.aut.U5FT4N.easyshopping.ui.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class SummaryViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {
    var totalItems by mutableStateOf(0)
    var foodItemsCount by mutableStateOf(0)
    var electronicsItemsCount by mutableStateOf(0)

    init {
        val tmpTotalItems = savedStateHandle.get<String>("totalItems") ?: "0"
        val tmpFoodItemsCount = savedStateHandle.get<String>("foodItemsCount") ?: "0"
        val tmpElectronicsItemsCount = savedStateHandle.get<String>("electronicsItemsCount") ?: "0"

        totalItems = tmpTotalItems.toInt()
        foodItemsCount = tmpFoodItemsCount.toInt()
        electronicsItemsCount = tmpElectronicsItemsCount.toInt()
    }
}
