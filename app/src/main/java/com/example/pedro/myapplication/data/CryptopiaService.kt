package com.example.pedro.myapplication.data

import com.example.pedro.myapplication.data.model.ApiReturn
import com.example.pedro.myapplication.data.model.Balance
import com.example.pedro.myapplication.data.model.OpenOrder
import com.example.pedro.myapplication.data.model.TradePair
import com.example.pedro.myapplication.data.remote.constants.ApiConstants
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

    @GET("GetMarkets/BTC")
    fun getBtcMarkets(): DeferredApiList<TradePair>

    @GET("GetMarket/{id}")
    fun getMarket(@Path("id") tradePair: String): DeferredApi<TradePair>

    @POST("GetBalance")
    fun getBalance(@Header(ApiConstants.HEADER_AUTHORIZATION) authorization: String, @Body json: RequestBody): DeferredApiList<Balance>

    @POST("GetOpenOrders")
    fun getOpenOrders(@Header(ApiConstants.HEADER_AUTHORIZATION) authorization: String, @Body json: RequestBody): DeferredApiList<OpenOrder>

    @POST("CancelTrade")
    fun cancelTrade(@Header(ApiConstants.HEADER_AUTHORIZATION) authorization: String, @Body json: RequestBody): DeferredApiList<Unit>

    @POST("SubmitTrade")
    fun submitTrade(@Header(ApiConstants.HEADER_AUTHORIZATION) authorization: String, @Body json: RequestBody): DeferredApi<Unit>

    companion object {
        fun getInstance(): CryptopiaService {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BASIC

            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
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