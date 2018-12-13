package com.example.pedro.myapplication.ui.fragment

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.pedro.myapplication.*
import com.example.pedro.myapplication.data.model.OpenOrder
import com.example.pedro.myapplication.presentation.*
import com.example.pedro.myapplication.presentation.viewModel.OrdersViewModel
import com.example.pedro.myapplication.ui.FragmentToolbar
import com.example.pedro.myapplication.ui.ToolbarManager
import com.example.pedro.myapplication.ui.adapter.OrdersRecyclerAdapter
import com.example.pedro.myapplication.ui.activity.MainActivity
import kotlinx.android.synthetic.main.fragment_orders.*
import kotlinx.android.synthetic.main.fragment_orders.view.*
import org.koin.android.viewmodel.ext.android.viewModel

class OrdersFragment : Fragment() {

    private val mViewModel : OrdersViewModel by viewModel()
    private lateinit var mAdapter: OrdersRecyclerAdapter
    private val list = mutableListOf<OpenOrder>()

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    companion object {
        fun newInstance() = OrdersFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_orders, container, false)

        ToolbarManager(fragmentToolbarBuilder(), view).prepareToolbar()

        mAdapter = OrdersRecyclerAdapter(list) { order ->
            mViewModel.removeOrder(order.orderId)
        }
        view.orders_recycler.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context)
        }

        view.orders_refresh.apply {
            setOnRefreshListener {
                isRefreshing = false
                mViewModel.getOpenOrders()
            }
            swipeRefreshLayout = this

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

    private fun fragmentToolbarBuilder() = FragmentToolbar.Builder()
        .withId(R.id.toolbar_orders)
        .withTitle(R.string.orders)
        .build()

    private fun handleState(viewState: ViewStateList<OpenOrder>) {
        when (viewState) {
            is Success<List<OpenOrder>> -> handleSuccess(viewState.data)
            is Failure -> handleError(viewState.error)
            is Loading -> handleLoading()
        }
    }

    private fun handleSuccess(data: List<OpenOrder>) {
        order_loading.visibility = View.GONE
        orders_recycler.visibility = View.VISIBLE

        list.clear()
        list.addAll(data)
        mAdapter.notifyDataSetChanged()
    }

    private fun handleError(error: Throwable) {
        error.printStackTrace()
        Toast.makeText(activity, error.message, Toast.LENGTH_LONG).show()
        order_loading.visibility = View.GONE
    }

    private fun handleLoading() {
        order_loading.visibility = View.VISIBLE
        order_loading.speed = 1.5f
        orders_recycler.visibility = View.GONE
    }

}
