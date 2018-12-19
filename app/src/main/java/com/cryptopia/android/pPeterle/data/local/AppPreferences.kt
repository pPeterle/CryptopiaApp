package com.cryptopia.android.pPeterle.data.local

import android.content.Context
import com.orhanobut.hawk.Hawk

class AppPreferences(context: Context) {

    private val SECRET_API_KEY = "SECRET_API_KEY"
    private val API_KEY = "API_KEY"
    private val NAME = "MySharedPreferences"

    private val prefs = context.getSharedPreferences(NAME, Context.MODE_PRIVATE)

    init {
        prefs.edit().clear().apply()
        Hawk.init(context).build()
    }

    var secretKey: String?
    get()  {
        return if (Hawk.contains(SECRET_API_KEY)) null
        else Hawk.get(SECRET_API_KEY)

    }
    set(value) { Hawk.put(SECRET_API_KEY, value) }

    var apiKey: String?
    get() {
        return if (Hawk.contains(API_KEY)) null
        else Hawk.get(API_KEY)
    }
    set(value) { Hawk.put(API_KEY, value) }

    /*private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }*/
}