package com.cryptopia.android.pPeterle.ui.fragment

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.cryptopia.android.pPeterle.R
import com.cryptopia.android.pPeterle.presentation.Failure
import com.cryptopia.android.pPeterle.presentation.Loading
import com.cryptopia.android.pPeterle.presentation.Success
import com.cryptopia.android.pPeterle.presentation.ViewStateList
import com.cryptopia.android.pPeterle.presentation.model.BalanceBinding
import com.cryptopia.android.pPeterle.presentation.viewModel.BalanceViewModel
import com.cryptopia.android.pPeterle.ui.FragmentToolbar
import com.cryptopia.android.pPeterle.ui.ToolbarManager
import com.cryptopia.android.pPeterle.ui.adapter.BalanceRecyclerAdpater
import com.cryptopia.android.pPeterle.utils.toFormattedString
import kotlinx.android.synthetic.main.fragment_balance.*
import kotlinx.android.synthetic.main.fragment_balance.view.*
import org.koin.android.viewmodel.ext.android.viewModel

class BalanceFragment : Fragment() {

    private val mAdapter = BalanceRecyclerAdpater(emptyList())

    companion object {
        fun newInstance() = BalanceFragment()
    }

    private val mViewModel: BalanceViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_balance, container, false)

        ToolbarManager(fragmentToolbarBuilder(), view).build()

        view.recyclerView_balance.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        }
        return view
    }

    private fun fragmentToolbarBuilder() = FragmentToolbar.Builder()
        .withId(R.id.toolbar_balance)
        .withTitle(R.string.balances)
        .build()


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mViewModel.getAllBalances()
        mViewModel.getState().observe(this, Observer {
            it?.let { handleState(it) }
        })
    }

    private fun handleState(state: ViewStateList<BalanceBinding>) {
        when (state) {
            is Success -> handleSuccess(state.data)
            is Failure -> handleFailure(state.error)
            is Loading -> handleLoading()
        }
    }

    private fun handleLoading() {
        loading_balance.speed = 1.5f
        loading_balance.visibility = View.VISIBLE
        recyclerView_balance.visibility = View.GONE
    }

    private fun handleFailure(error: Throwable) {
        loading_balance.visibility = View.GONE
        Toast.makeText(activity, error.message, Toast.LENGTH_SHORT).show()
    }

    private fun handleSuccess(data: List<BalanceBinding>) {
        loading_balance.visibility = View.GONE
        recyclerView_balance.visibility = View.VISIBLE

        mAdapter.list = data
        mAdapter.notifyDataSetChanged()
        val sum = data.sumByDouble { it.btcValue.toDouble() }
        textView_balance_btcTotal.text = String.format(resources.getString(R.string.bitcoinPrice), sum.toFormattedString() )
    }

    override fun onDestroyView() {
        recyclerView_balance.adapter = null
        super.onDestroyView()
    }

}
