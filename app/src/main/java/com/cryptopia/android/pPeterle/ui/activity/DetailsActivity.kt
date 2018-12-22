package com.cryptopia.android.pPeterle.ui.activity

import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.cryptopia.android.pPeterle.R
import com.cryptopia.android.pPeterle.databinding.ActivityDetailsBinding
import com.cryptopia.android.pPeterle.presentation.model.TradePairDetailsBinding
import com.cryptopia.android.pPeterle.presentation.Failure
import com.cryptopia.android.pPeterle.presentation.Loading
import com.cryptopia.android.pPeterle.presentation.Success
import com.cryptopia.android.pPeterle.presentation.ViewState
import com.cryptopia.android.pPeterle.presentation.viewModel.DetailsViewModel
import com.cryptopia.android.pPeterle.ui.adapter.DetailsPageAdapter
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

    private lateinit var binding: ActivityDetailsBinding

    lateinit var tradePair: String

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

    private val orderSubmitedObserver =  Observer<ViewState<Unit>?> { state ->
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
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_details)

        tradePair = intent?.extras?.getString(CURRENCY_NAME)!!

        val bottomSheet = BottomSheetBehavior.from(nestedScroll_details)

        setSupportActionBar(toolbar_details)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val (symbol, baseSymbol) = tradePair.split("/")

        binding.symbol = symbol
        binding.baseSymbol = baseSymbol
        binding.executePendingBindings()

        setupViewPager(tradePair)

        setupViewModel(tradePair)

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

        details_btn_25.setOnClickListener { updateTotal(0.25) }

        details_btn_50.setOnClickListener { updateTotal(0.5) }

        details_btn_75.setOnClickListener { updateTotal(0.75) }

        details_btn_100.setOnClickListener { updateTotal(1.0) }

        details_buy_btn.setOnClickListener {
            mViewModel.setBuyOrder(
                details_amount_et.text.toString(),
                details_price_et.text.toString()
            ).observe(this, orderSubmitedObserver)
        }

        details_sell_btn.setOnClickListener {
            mViewModel.setSellOrder(
                details_amount_et.text.toString(),
                details_price_et.text.toString()
            ).observe(this, orderSubmitedObserver)
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

            override fun onStateChanged(bottomSheet: View, newState: Int) {}
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_trade_history, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> onBackPressed()
            R.id.action_trade_history -> startActivity(TradeHistoryActivity.newInstace(this, tradePair))
        }
        return true
    }

    private fun setupViewPager(tradePair: String) {
        viewPager_details.adapter = DetailsPageAdapter(tradePair, supportFragmentManager)

    }

    private fun setupViewModel(tradePair: String) {
        mViewModel.tradePair = tradePair
        mViewModel.getDetails()
        mViewModel.getState().observe(this, Observer {
            it?.let {
                handleState(it)
            }
        })
    }

    private fun handleState(viewState: ViewState<TradePairDetailsBinding>) {
        when (viewState) {
            is Success<TradePairDetailsBinding> -> handleSuccess(viewState.data)
            is Failure -> handleError(viewState.error)
            is Loading -> handleLoading()
        }
    }

    private fun handleSuccess(data: TradePairDetailsBinding) {
        details_loading.visibility = View.GONE
        binding.tradePairDetails = data


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
        binding.executePendingBindings()
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
