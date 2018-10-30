package com.example.pedro.myapplication.orders

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.pedro.myapplication.*
import kotlinx.android.synthetic.main.orders_fragment.*
import org.knowm.xchange.dto.trade.LimitOrder
import org.knowm.xchange.dto.trade.OpenOrders
import org.koin.android.viewmodel.ext.android.viewModel

class OrdersFragment : Fragment() {

    private val mViewModel : OrdersViewModel by viewModel()
    private lateinit var mAdapter: OrdersRecyclerAdapter
    private val list = mutableListOf<LimitOrder>()

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    companion object {
        fun newInstance() = OrdersFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.orders_fragment, container, false)

        (activity as MainActivity).toolbarTitle.text = "ORDERS"

        mAdapter = OrdersRecyclerAdapter(list) {limitOrder ->

                mViewModel.removeOrder(limitOrder.id)
        }
        view.findViewById<RecyclerView>(R.id.orders_recycler).apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context)
        }

        swipeRefreshLayout =  view.findViewById<SwipeRefreshLayout>(R.id.orders_refresh).apply {
            setOnRefreshListener {
                swipeRefreshLayout.isRefreshing = false
                mViewModel.getOpenOrders()
            }
        }

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mViewModel.getOpenOrders()
        mViewModel.getState().observe(this, Observer {
            it?.let {
                handleState(it)
            }
        })
    }

    private fun handleState(viewState: ViewState<OpenOrders>) {
        when (viewState) {
            is Success<OpenOrders> -> handleSuccess(viewState.data)
            is Failure -> handleError(viewState.error)
            is Loading -> handleLoading()
        }
    }

    private fun handleSuccess(data: OpenOrders) {
        order_loading.visibility = View.GONE
        orders_recycler.visibility = View.VISIBLE

        list.clear()
        list.addAll(data.openOrders)
        mAdapter.notifyDataSetChanged()
    }

    private fun handleError(error: Throwable) {
        error.printStackTrace()
        Toast.makeText(activity, "Algum erro aconteceu", Toast.LENGTH_LONG).show()
    }

    private fun handleLoading() {
        order_loading.visibility = View.VISIBLE
        order_loading.speed = 1.5f
        orders_recycler.visibility = View.GONE
    }

}
