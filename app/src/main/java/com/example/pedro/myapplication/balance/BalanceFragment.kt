package com.example.pedro.myapplication.balance

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.pedro.myapplication.*
import com.example.pedro.myapplication.data.model.Balance
import kotlinx.android.synthetic.main.fragment_balance.*
import kotlinx.android.synthetic.main.fragment_balance.view.*
import org.koin.android.viewmodel.ext.android.viewModel

class BalanceFragment : Fragment() {

    private val mAdapter = BalanceAdpater(emptyList())

    companion object {
        fun newInstance() = BalanceFragment()
    }

    private val mViewModel: BalanceViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_balance, container, false)

        (activity as MainActivity).toolbarTitle.text = "BALANCES"

        view.recyclerView_balance.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        }

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mViewModel.getAllBalances()
        mViewModel.getState().observe(this, Observer {
            it?.let { handleState(it) }
        })
    }

    private fun handleState(state: ViewStateList<Balance>) {
        when (state) {
            is Success -> handleSuccess(state.data)
            is Failure -> handleFailure(state.error)
        }
    }

    private fun handleFailure(error: Throwable) {
        Toast.makeText(activity, error.message, Toast.LENGTH_SHORT).show()
    }

    private fun handleSuccess(data: List<Balance>) {
        mAdapter.list = data
        mAdapter.notifyDataSetChanged()
        val sum = data.sumByDouble { it.btcValue }
        Log.i("test", "handleSuccess: $sum")
        textView_balance_btcTotal.text = String.format("%.8f",sum )
    }

}
