package hu.bme.aut.U5FT4N.easyshopping.network

import hu.bme.aut.U5FT4N.easyshopping.data.money.MoneyResult
import retrofit2.http.GET

// https://api.exchangerate-api.com/v4/latest/USD

//Host: https://api.exchangerate-api.com/
//Path: v4/latest/USD
//Query params: we do not have now...
interface MoneyAPI {

    @GET("v4/latest/USD")
    suspend fun getRates(): MoneyResult

}