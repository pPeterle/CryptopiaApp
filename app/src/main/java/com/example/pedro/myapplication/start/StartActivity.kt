package com.example.pedro.myapplication.start

import android.arch.lifecycle.Observer
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.pedro.myapplication.*
import kotlinx.android.synthetic.main.activity_start.*
import org.koin.android.viewmodel.ext.android.viewModel

class StartActivity : AppCompatActivity() {

    private val mViewModel: StartViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        lifecycle.addObserver(mViewModel)

        button.setOnClickListener {
            mViewModel.setKeys(start_apikey_et.text.toString(), start_secretkey_et.text.toString())
        }

        mViewModel.getState().observe(this, Observer {
            it?.let {
                when (it) {
                    is Success -> {
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
                    is Failure -> Toast.makeText(this, it.error.message, Toast.LENGTH_LONG).show()
                    is Loading -> Toast.makeText(this, "Loading", Toast.LENGTH_LONG).show()
                }
            }
        })
    }
}
