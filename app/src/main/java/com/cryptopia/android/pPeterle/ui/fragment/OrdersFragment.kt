package com.cryptopia.android.pPeterle.ui.fragment

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.cryptopia.android.pPeterle.*
import com.cryptopia.android.pPeterle.presentation.*
import com.cryptopia.android.pPeterle.presentation.model.OpenOrderBinding
import com.cryptopia.android.pPeterle.presentation.viewModel.OrdersViewModel
import com.cryptopia.android.pPeterle.ui.FragmentToolbar
import com.cryptopia.android.pPeterle.ui.ToolbarManager
import com.cryptopia.android.pPeterle.ui.activity.TradeHistoryActivity
import com.cryptopia.android.pPeterle.ui.adapter.OrdersRecyclerAdapter
import kotlinx.android.synthetic.main.fragment_orders.*
import kotlinx.android.synthetic.main.fragment_orders.view.*
import org.koin.android.viewmodel.ext.android.viewModel

class OrdersFragment : Fragment() {

    private val mViewModel: OrdersViewModel by viewModel()
    private lateinit var mAdapter: OrdersRecyclerAdapter

    companion object {
        fun newInstance() = OrdersFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_orders, container, false)

        ToolbarManager(fragmentToolbarBuilder(), view).build()

        mAdapter = OrdersRecyclerAdapter(emptyList()) { order ->
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

    override fun onDestroyView() {
        orders_recycler.adapter = null
        super.onDestroyView()
    }

    private fun fragmentToolbarBuilder() = FragmentToolbar.Builder()
        .withId(R.id.toolbar_orders)
        .withTitle(R.string.orders)
        .withMenu(R.menu.menu_open_orders)
        .withMenuItems(listOf(R.id.action_order_history), listOf(MenuItem.OnMenuItemClickListener {
            startActivity(TradeHistoryActivity.newInstace(context!!, null))
            true
        }))
        .build()

    private fun handleState(viewState: ViewStateList<OpenOrderBinding>) {
        when (viewState) {
            is Success<List<OpenOrderBinding>> -> handleSuccess(viewState.data)
            is Failure -> handleError(viewState.error)
            is Loading -> handleLoading()
        }
    }

    private fun handleSuccess(data: List<OpenOrderBinding>) {
        order_loading.visibility = View.GONE
        orders_recycler.visibility = View.VISIBLE

        mAdapter.run {
            list = data
            notifyDataSetChanged()
        }
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
