package com.cryptopia.android.pPeterle.ui.activity

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.cryptopia.android.pPeterle.presentation.Failure
import com.cryptopia.android.pPeterle.presentation.Success
import com.cryptopia.android.pPeterle.presentation.viewModel.StartViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class SplashActivity : AppCompatActivity() {

    private val mViewModel: StartViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycle.addObserver(mViewModel)



        mViewModel.getState().observe(this, Observer { state ->
            state?.let {
                when (it) {
                    is Success -> {
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
                    is Failure -> {
                        startActivity(Intent(this, IntroActivity::class.java))
                        finish()
                    }
                }
            }
        })
    }
}
