package com.example.pedro.myapplication.data.remote.exceptions

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response
import android.net.NetworkInfo
import android.net.ConnectivityManager

class ConnectivityInterceptor(private val context: Context): Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        if (!isOnline())
            throw RuntimeException("INTERNET")

        val builder = chain.request()
        return chain.proceed(builder)
    }

    private fun isOnline(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = connectivityManager.activeNetworkInfo
        return netInfo != null && netInfo.isConnected
    }
}