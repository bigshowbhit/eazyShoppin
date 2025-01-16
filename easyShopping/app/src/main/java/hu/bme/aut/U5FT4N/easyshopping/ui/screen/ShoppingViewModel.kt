package hu.bme.aut.U5FT4N.easyshopping.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.U5FT4N.easyshopping.data.ShoppingItem
import hu.bme.aut.U5FT4N.easyshopping.data.ShoppingItemDAO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoppingViewModel @Inject constructor(
    private val shoppingItemDAO: ShoppingItemDAO
) : ViewModel() {

    fun getAllItems(): Flow<List<ShoppingItem>> {
        return shoppingItemDAO.getAllItems()
    }

    fun insertItem(shoppingItem: ShoppingItem) {
        viewModelScope.launch(Dispatchers.IO) {
            shoppingItemDAO.insert(shoppingItem)
        }
    }

    fun removeItem(shoppingItem: ShoppingItem) {
        viewModelScope.launch(Dispatchers.IO) {
            shoppingItemDAO.delete(shoppingItem)
        }
    }

    fun updateItem(updatedItem: ShoppingItem) {
        viewModelScope.launch(Dispatchers.IO) {
            shoppingItemDAO.update(updatedItem)
        }
    }

    fun toggleItemState(shoppingItem: ShoppingItem, isBought: Boolean) {
        val updatedItem = shoppingItem.copy(isBought = isBought)
        viewModelScope.launch(Dispatchers.IO) {
            shoppingItemDAO.update(updatedItem)
        }
    }

    fun clearAllItems() {
        viewModelScope.launch(Dispatchers.IO) {
            shoppingItemDAO.deleteAllItems()
        }
    }
}
