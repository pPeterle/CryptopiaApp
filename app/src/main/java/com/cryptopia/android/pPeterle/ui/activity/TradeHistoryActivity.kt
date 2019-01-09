package com.cryptopia.android.pPeterle.ui.activity

import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.Toast
import com.cryptopia.android.pPeterle.R
import com.cryptopia.android.pPeterle.presentation.Failure
import com.cryptopia.android.pPeterle.presentation.Loading
import com.cryptopia.android.pPeterle.presentation.Success
import com.cryptopia.android.pPeterle.presentation.ViewState
import com.cryptopia.android.pPeterle.presentation.model.TradeHistoryBinding
import com.cryptopia.android.pPeterle.presentation.viewModel.TradeHistoryViewModel
import com.cryptopia.android.pPeterle.ui.adapter.TradeHistoryAdapter
import kotlinx.android.synthetic.main.activity_trade_history.*
import org.koin.android.viewmodel.ext.android.viewModel

class TradeHistoryActivity : AppCompatActivity() {

    private val mViewModel: TradeHistoryViewModel by viewModel()
    private val mAdapter = TradeHistoryAdapter(emptyList())

    companion object {

        private const val LABEL_KEY = "Label Key"

        fun newInstace(context: Context, label: String? = null) =
            Intent(context, TradeHistoryActivity::class.java).apply {
                putExtra(LABEL_KEY, label)
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trade_history)

        setSupportActionBar(toolbar_trade_history)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val label = intent.extras.getString(LABEL_KEY)
        mViewModel.label = label

        if (label != null) {
            text_trader_history_title.text = "Trade History - $label"
        }

        mViewModel.getTradeHistory()

        mViewModel.getState().observe(this, Observer {
            it?.let { handleState(it) }
        })

        recycler_trade_history.apply {
            layoutManager = LinearLayoutManager(this@TradeHistoryActivity)
            adapter = mAdapter
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item?.itemId) {
            android.R.id.home -> onBackPressed()
        }

        return true
    }

    private fun handleState(state: ViewState<List<TradeHistoryBinding>>) {
        when (state) {
            is Success -> handleSuccess(state.data)
            is Failure -> handleError(state.error)
            is Loading -> showLoading()
        }
    }

    private fun showLoading() {
        Toast.makeText(this, "Loading", Toast.LENGTH_SHORT).show()
    }

    private fun handleError(error: Throwable) {
        Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
    }

    private fun handleSuccess(data: List<TradeHistoryBinding>) {
        mAdapter.list = data
        mAdapter.notifyDataSetChanged()
    }
}
