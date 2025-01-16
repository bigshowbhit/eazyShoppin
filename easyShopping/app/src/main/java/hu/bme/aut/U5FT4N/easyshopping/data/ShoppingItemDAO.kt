package hu.bme.aut.U5FT4N.easyshopping.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ShoppingItemDAO {
    @Query("SELECT * FROM shoppingtable")
    fun getAllItems(): Flow<List<ShoppingItem>>

    @Query("SELECT * from shoppingtable WHERE id = :id")
    fun getItem(id: Int): Flow<ShoppingItem>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: ShoppingItem)

    @Update
    suspend fun update(item: ShoppingItem)

    @Delete
    suspend fun delete(item: ShoppingItem)

    @Query("DELETE from shoppingtable")
    suspend fun deleteAllItems()
}
