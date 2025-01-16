package hu.bme.aut.U5FT4N.easyshopping.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import hu.bme.aut.U5FT4N.easyshopping.data.AppDatabase
import hu.bme.aut.U5FT4N.easyshopping.data.ShoppingItemDAO
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {
    @Provides
    fun provideTodoDao(appDatabase: AppDatabase): ShoppingItemDAO {
        return appDatabase.shoppingItemDao()
    }

    @Provides
    @Singleton
    fun provideTodoAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return AppDatabase.getDatabase(appContext)
    }
}