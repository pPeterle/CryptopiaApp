package com.example.pedro.myapplication.ui.activity

import android.arch.lifecycle.Observer
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.widget.Toast
import com.example.pedro.myapplication.*
import com.example.pedro.myapplication.presentation.Failure
import com.example.pedro.myapplication.presentation.Loading
import com.example.pedro.myapplication.presentation.viewModel.StartViewModel
import com.example.pedro.myapplication.presentation.Success
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

        start_link.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.cryptopia.co.nz/Security"))
            startActivity(intent)
        }

        mViewModel.getState().observe(this, Observer { state ->
            state?.let {
                when (it) {
                    is Success -> {
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
                    is Failure -> AlertDialog.Builder(this).setTitle("ERROR").setMessage(it.error.message).create().show()
                    is Loading -> Toast.makeText(this, "Loading", Toast.LENGTH_LONG).show()
                }
            }
        })
    }
}
