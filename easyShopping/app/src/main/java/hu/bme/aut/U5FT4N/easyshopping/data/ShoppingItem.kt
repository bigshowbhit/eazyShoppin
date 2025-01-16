package hu.bme.aut.U5FT4N.easyshopping.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.easyshopping.R
import java.io.Serializable

@Entity(tableName = "shoppingtable")
data class ShoppingItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "estimatedPrice") val estimatedPrice: Double,
    @ColumnInfo(name = "category") val category: ItemCategory,
    @ColumnInfo(name = "isBought") var isBought: Boolean,
    @ColumnInfo(name = "currency") val currency: String = "USD"
) : Serializable

enum class ItemCategory {
    FOOD, ELECTRONIC;

    fun getIcon(): Int {
        return when (this) {
            FOOD -> R.drawable.food_icon
            ELECTRONIC -> R.drawable.electronic_icon
        }
    }
}