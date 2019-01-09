package com.cryptopia.android.pPeterle.data.local

import android.content.Context
import android.util.Log
import com.orhanobut.hawk.Hawk

class AppPreferences(context: Context) {

    private val SECRET_API_KEY = "SECRET_API_KEY"
    private val API_KEY = "API_KEY"

    init {
        Hawk.init(context).build()
        Log.i("test", "hawk: ${Hawk.count()}")
    }

    var secretKey: String?
    get() = Hawk.get(SECRET_API_KEY, null)
    set(value) { Hawk.put(SECRET_API_KEY, value) }

    var apiKey: String?
    get() = Hawk.get(API_KEY, null)
    set(value) { Hawk.put(API_KEY, value) }

}