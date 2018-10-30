package com.example.pedro.myapplication.data

import com.example.pedro.myapplication.data.model.Api
import com.example.pedro.myapplication.data.model.ApiReturn
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET


interface CryptopiaService {

    @GET("GetMarkets/BTC")
    fun getBtcMarkets(): Deferred<Api>

    companion object {
        fun getInstance(): CryptopiaService {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BASIC
            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl("https://www.cryptopia.co.nz/api/")
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit.create(CryptopiaService::class.java)
        }
    }
}