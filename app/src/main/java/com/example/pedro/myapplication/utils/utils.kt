package com.example.pedro.myapplication.utils

import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Toast
import com.airbnb.lottie.LottieAnimationView

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