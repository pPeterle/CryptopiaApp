package com.cryptopia.android.pPeterle.data.local

import android.content.Context

class AppPreferences(context: Context) {

    private val SECRET_API_KEY = "SECRET_API_KEY"
    private val API_KEY = "API_KEY"
    private val NAME = "MySharedPreferences"

    private val prefs = context.getSharedPreferences(NAME, Context.MODE_PRIVATE)

    var secretKey: String?
    get() = prefs.getString(SECRET_API_KEY, null)
    set(value) = prefs.edit().putString(SECRET_API_KEY, value).apply()

    var apiKey: String?
    get() = prefs.getString(API_KEY, null)
    set(value) = prefs.edit().putString(API_KEY, value).apply()

    /*private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }*/
}