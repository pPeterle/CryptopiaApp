package com.example.pedro.myapplication.details

import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.pedro.myapplication.*
import com.example.pedro.myapplication.data.model.TradePairDetails
import com.example.pedro.myapplication.utils.onTextChanged
import kotlinx.android.synthetic.main.activity_details.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.math.BigDecimal

class DetailsActivity : AppCompatActivity() {
    private val mViewModel: DetailsViewModel by viewModel()

    private var tradePair: String? = null

    companion object {

        private const val CURRENCY_NAME = "currencyPair"
        fun newInstance(tradePair: String, context: Context): Intent {
            val intent = Intent(context, DetailsActivity::class.java)
            intent.putExtra(CURRENCY_NAME, tradePair)
            return intent
        }

    }

    val textChanged = fun(s: String) {
        if (details_price_et.isFocused || details_amount_et.isFocused) {
            val price = details_price_et.text.toString()
            val amount = details_amount_et.text.toString()
            val result = mViewModel.calculateTotal(price, amount)
            details_total_et.setText(result)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        tradePair = intent?.extras?.getString(CURRENCY_NAME)

        //details_coin_name.text = currencyPair.base.displayName
        setSupportActionBar(details_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupHint()

        setupViewModel()

        details_price_et.onTextChanged(textChanged)
        details_amount_et.onTextChanged(textChanged)
        details_total_et.onTextChanged {
            if (details_total_et.isFocused) {
                val price = details_price_et.text.toString()
                val total = details_total_et.text.toString()
                val result = mViewModel.calculateAmount(price, total)
                details_amount_et.setText(result)
            }
        }

        details_counter_amount_tv.setOnClickListener {
            details_amount_et.setText(details_counter_amount_tv.text)
        }

        details_btn_25.setOnClickListener {
            updateTotal(0.25)
        }

        details_btn_50.setOnClickListener {
            updateTotal(0.5)
        }

        details_btn_75.setOnClickListener {
            updateTotal(0.75)
        }

        details_btn_100.setOnClickListener {
            updateTotal(1.0)
        }

        details_buy_btn.setOnClickListener {
            mViewModel.setBuyOrder(
                details_amount_et.text.toString(),
                details_price_et.text.toString()
            )
        }

        details_sell_btn.setOnClickListener {
            mViewModel.setSellOrder(
                details_amount_et.text.toString(),
                details_price_et.text.toString()
            )
        }
    }

    private fun setupViewModel() {
        mViewModel.tradePair = this.tradePair!!
        mViewModel.getDetails()
        mViewModel.getState().observe(this, Observer {
            it?.let {
                handleState(it)
            }
        })
    }

    private fun setupHint() {
        val (symbol, baseSymbol) = tradePair!!.split("/")
        details_counter_tv.text = baseSymbol
        details_base_tv.text = symbol

        details_amount_et.hint = String.format(resources.getString(R.string.amount), symbol)
        details_total_et.hint = String.format(resources.getString(R.string.total), baseSymbol)
        details_price_et.hint = String.format(resources.getString(R.string.price), baseSymbol)
        details_currency_tv.text = tradePair
    }

    private fun handleState(viewState: ViewState<TradePairDetails>) {
        when (viewState) {
            is Success<TradePairDetails> -> handleSuccess(viewState.data)
            is Failure -> handleError(viewState.error)
            is Loading -> handleLoading()
        }
    }

    private fun handleSuccess(data: TradePairDetails) {
        details_loading.visibility = View.GONE

        val avaibleBase = String.format(
            "%.8f",
            data.baseAmount
        )
        val avaibleCounter = String.format(
            "%.8f",
            data.counterAmount
        )

        details_price_et.setText(String.format("%.8f", data.lastPrice))
        details_base_amount_tv.text = avaibleBase
        details_counter_amount_tv.text = avaibleCounter
    }

    private fun handleError(error: Throwable) {
        error.printStackTrace()
        details_loading.visibility = View.GONE
        Toast.makeText(this, "${error.message}", Toast.LENGTH_LONG).show()
    }

    private fun handleLoading() {
        details_loading.visibility = View.VISIBLE
        details_loading.speed = 1.5f
    }

    private fun updateTotal(percent: Double) {
        val balance = details_base_amount_tv.text.toString()
        val result = String.format("%.8f", BigDecimal(balance.toFloat() * percent).toFloat())
        details_total_et.requestFocus()
        details_total_et.setText(result)
    }
}
