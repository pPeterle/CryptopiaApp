package com.cryptopia.android.pPeterle.utils

import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Toast
import com.cryptopia.android.pPeterle.R

fun EditText.onTextChanged(block: (s: String) -> Unit) {
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            s?.let {
                block(s.toString())
            }
        }
    })
}

fun Double.toFormattedString(decimal: String = "8"): String {
    val result = String.format("%.${decimal}f", this)

    val string = result.replace(",", ".")

    return string.take(10)
}

fun Double.toPercent() = String.format("%.2f %%", this)

fun AppCompatActivity.showToast(text: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, text, duration).show()
}

fun Fragment.getDrawable(@DrawableRes drawableRes: Int) = ContextCompat.getDrawable(context!!, drawableRes)

fun Fragment.getColor(@ColorRes colorRes: Int) = ContextCompat.getColor(context!!, colorRes)