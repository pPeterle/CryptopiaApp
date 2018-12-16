package com.cryptopia.android.pPeterle.ui.activity

import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast
import com.cryptopia.android.pPeterle.R
import com.cryptopia.android.pPeterle.presentation.model.TradePairView
import com.cryptopia.android.pPeterle.presentation.Failure
import com.cryptopia.android.pPeterle.presentation.Loading
import com.cryptopia.android.pPeterle.presentation.Success
import com.cryptopia.android.pPeterle.presentation.ViewState
import com.cryptopia.android.pPeterle.presentation.viewModel.DetailsViewModel
import com.cryptopia.android.pPeterle.ui.adapter.DetailsPagerAdapter
import com.cryptopia.android.pPeterle.ui.adapter.MarketOrderBuyAdapter
import com.cryptopia.android.pPeterle.ui.adapter.MarketOrderSellAdapter
import com.cryptopia.android.pPeterle.utils.onTextChanged
import com.cryptopia.android.pPeterle.utils.showToast
import com.cryptopia.android.pPeterle.utils.toFormattedString
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.android.synthetic.main.view_market_oders.*
import kotlinx.android.synthetic.main.view_trade.*
import org.koin.android.viewmodel.ext.android.viewModel


class DetailsActivity : AppCompatActivity() {
    private val mViewModel: DetailsViewModel by viewModel()

    private lateinit var tradePair: String

    companion object {

        private const val CURRENCY_NAME = "currencyPair"

        fun newInstance(tradePair: String, context: Context) = Intent(context, DetailsActivity::class.java).apply {
            putExtra(CURRENCY_NAME, tradePair)
        }
    }

    private val textChanged = fun(_: String) {
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
        tradePair = intent?.extras?.getString(CURRENCY_NAME)!!

        val bottomSheet = BottomSheetBehavior.from(nestedScroll_details)

        setSupportActionBar(toolbar_details)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        text_details_toolbarTitle.text = tradePair

        setupViewPager()

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
            details_amount_et.requestFocus()
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
            ).observe(this, Observer { state ->
                state?.let {
                    when (it) {
                        is Loading -> trade_loading.visibility = View.VISIBLE
                        is Success -> {
                            trade_loading.visibility = View.GONE
                            showToast("Order Submitted")
                        }
                        is Error -> {
                            trade_loading.visibility = View.GONE
                            showToast(it.message!!)
                        }
                    }
                }
            })
        }

        details_sell_btn.setOnClickListener {
            mViewModel.setSellOrder(
                details_amount_et.text.toString(),
                details_price_et.text.toString()
            ).observe(this, Observer {
                it?.let {
                    when (it) {
                        is Loading -> trade_loading.visibility = View.VISIBLE
                        is Success -> {
                            trade_loading.visibility = View.GONE
                            showToast("Order Submitted")
                        }
                        is Error -> {
                            trade_loading.visibility = View.GONE
                            showToast(it.message!!)
                        }
                    }
                }
            })
        }

        btn_details_chart.setOnClickListener {
            viewPager_details.currentItem = 0
        }

        btn_details_orderBook.setOnClickListener {
            viewPager_details.currentItem = 1
        }

        textView_trade_transaction.setOnClickListener {
            if (bottomSheet.state == BottomSheetBehavior.STATE_COLLAPSED)
                bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED
            else
                bottomSheet.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        bottomSheet.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                imageView_trade.rotation = slideOffset * 180
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
            }

        })
    }

    private fun setupViewPager() {
        viewPager_details.adapter = DetailsPagerAdapter(tradePair, supportFragmentManager)

    }

    private fun setupViewModel() {
        mViewModel.tradePair = this.tradePair
        mViewModel.getDetails()
        mViewModel.getState().observe(this, Observer {
            it?.let {
                handleState(it)
            }
        })
    }

    private fun setupHint() {
        val (symbol, baseSymbol) = tradePair.split("/")
        details_counter_tv.text = baseSymbol
        details_base_tv.text = symbol

        details_amount_et.hint = String.format(resources.getString(R.string.amount), symbol)
        details_total_et.hint = String.format(resources.getString(R.string.total), baseSymbol)
        details_price_et.hint = String.format(resources.getString(R.string.price), baseSymbol)
    }

    private fun handleState(viewState: ViewState<TradePairView>) {
        when (viewState) {
            is Success<TradePairView> -> handleSuccess(viewState.data)
            is Failure -> handleError(viewState.error)
            is Loading -> handleLoading()
        }
    }

    private fun handleSuccess(data: TradePairView) {
        details_loading.visibility = View.GONE

        with(data.marketDetails) {

            textView_details_low.text = high.toFormattedString()
            textView_details_higt.text = low.toFormattedString()
            textView_details_volume.text = volume.toFormattedString()
            textview_details_lastPrice.text = resources.getString(R.string.bitcoinPrice, lastPrice.toFormattedString())
            details_price_et.setText(lastPrice.toFormattedString())
        }

        details_base_amount_tv.text = data.baseAmount.toFormattedString()
        details_counter_amount_tv.text = data.counterAmount.toFormattedString()

        val marketOrdersSellAdapter = MarketOrderSellAdapter(data.marketOrders.sellOrders.take(15))
        val marketOrdersBuyAdapter = MarketOrderBuyAdapter(data.marketOrders.buyOrders.take(15))

        recycler_details_sellOrders.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = marketOrdersSellAdapter
        }

        recycler_details_buyOrders.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = marketOrdersBuyAdapter
        }

        marketOrdersSellAdapter.notifyDataSetChanged()
        marketOrdersBuyAdapter.notifyDataSetChanged()

    }


    private fun handleError(error: Throwable) {
        details_loading.visibility = View.GONE
        error.printStackTrace()
        Toast.makeText(this, "${error.message}", Toast.LENGTH_LONG).show()
    }

    private fun handleLoading() {
        details_loading.visibility = View.VISIBLE
        details_loading.speed = 1.5f
    }

    private fun updateTotal(percent: Double) {

        val balance = details_base_amount_tv.text.toString()
        val result = (balance.toDouble() * percent).toFormattedString()
        details_total_et.requestFocus()
        details_total_et.setText(result)

    }
}
