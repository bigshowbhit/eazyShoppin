package hu.bme.aut.U5FT4N.easyshopping.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import hu.bme.aut.U5FT4N.easyshopping.network.MoneyAPI
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MoneyExchangeAPIHost
//"https://api.exchangerate-api.com/"


@Module
@InstallIn(SingletonComponent::class)
object APIModule {
    @Provides
    @MoneyExchangeAPIHost
    @Singleton
    fun provideMoneyExchangeAPIRetrofit(): Retrofit {
        val client = OkHttpClient.Builder()
            .build()

        return Retrofit.Builder()
            .baseUrl("https://api.exchangerate-api.com/")
            .addConverterFactory(
                Json{ ignoreUnknownKeys = true }.asConverterFactory("application/json".toMediaType()) )
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    fun provideMoneyAPI(@MoneyExchangeAPIHost retrofit: Retrofit): MoneyAPI {
        return retrofit.create(MoneyAPI::class.java)
    }

}