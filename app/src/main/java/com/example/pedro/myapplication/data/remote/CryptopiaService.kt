package com.example.pedro.myapplication.data.remote

import android.content.Context
import android.util.Log
import com.example.pedro.myapplication.data.model.*
import com.example.pedro.myapplication.data.remote.exceptions.ConnectivityInterceptor
import com.example.pedro.myapplication.data.remote.exceptions.NoConnectivityException
import com.example.pedro.myapplication.utils.ApiConstants
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

typealias DeferredApiList<T> = Deferred<ApiReturn<List<T>>>

typealias DeferredApi<T> = Deferred<ApiReturn<T>>

interface CryptopiaService {

    @GET("$Markets/BTC")
    fun getBtcMarkets(): DeferredApiList<TradePair>

    @GET("$Market/{id}")
    fun getMarket(@Path("id") tradePair: String): DeferredApi<TradePair>

    @GET("$MarketOrders/{id}/50")
    fun getMarketOrders(@Path("id") label: String): DeferredApi<MarketOrders>

    @GET("$MarketHistory/{market}/{hours}")
    fun getMarketHistory(@Path("market") market: String ,@Path("hours") hours: Int): DeferredApiList<MarketHistory>

    @POST(Balance)
    fun getBalance(@Header(ApiConstants.HEADER_AUTHORIZATION) authorization: String, @Body json: RequestBody): DeferredApiList<Balance>

    @POST(OpenOrders)
    fun getOpenOrders(@Header(ApiConstants.HEADER_AUTHORIZATION) authorization: String, @Body json: RequestBody): DeferredApiList<OpenOrder>

    @POST(CancelTrade)
    fun cancelTrade(@Header(ApiConstants.HEADER_AUTHORIZATION) authorization: String, @Body json: RequestBody): DeferredApiList<Double>

    @POST(SubmitTrade)
    fun submitTrade(@Header(ApiConstants.HEADER_AUTHORIZATION) authorization: String, @Body json: RequestBody): DeferredApi<Unit>

    companion object {

        const val Markets = "GetMarkets"
        const val Market = "GetMarket"
        const val MarketHistory = "GetMarketHistory"
        const val MarketOrders = "GetMarketOrders"
        const val Balance = "GetBalance"
        const val OpenOrders = "GetOpenOrders"
        const val CancelTrade = "CancelTrade"
        const val SubmitTrade = "SubmitTrade"

        fun getInstance(context: Context): CryptopiaService {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BASIC

            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .addInterceptor(ConnectivityInterceptor(context))
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(ApiConstants.API_BASE_URL)
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

            return retrofit.create(CryptopiaService::class.java)
        }
    }
}