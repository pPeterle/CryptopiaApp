package com.cryptopia.android.pPeterle.ui.fragment

import android.arch.lifecycle.Observer
import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cryptopia.android.pPeterle.R
import com.cryptopia.android.pPeterle.presentation.Failure
import com.cryptopia.android.pPeterle.presentation.Loading
import com.cryptopia.android.pPeterle.presentation.Success
import com.cryptopia.android.pPeterle.presentation.viewModel.KeysViewModel
import com.cryptopia.android.pPeterle.ui.activity.MainActivity
import kotlinx.android.synthetic.main.fragment_keys.*
import kotlinx.android.synthetic.main.fragment_keys.view.*
import org.koin.android.viewmodel.ext.android.viewModel

class KeysFragment: Fragment() {

    private val mViewModel: KeysViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_keys, container, false)

        view.text_keys_link.apply {
            paintFlags = Paint.UNDERLINE_TEXT_FLAG
            setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.cryptopia.co.nz/Security"))
                startActivity(intent)
            }
        }

        view.button_keys.setOnClickListener {
            mViewModel.setKeys(edit_keys_apiKey.text.toString(), edit_keys_secretKey.text.toString())
        }

        mViewModel.getState().observe(this, Observer {
            it?.let {
                when (it) {
                    is Success -> {
                        startActivity(Intent(activity, MainActivity::class.java))
                    }
                    is Failure -> {
                        AlertDialog.Builder(context!!).setTitle("Error").setMessage(it.error.message).create().show()
                        Log.i("test", "onCreateView: ${it.error}")
                    }
                    is Loading -> {}
                }
            }
        })
        return view
    }
}