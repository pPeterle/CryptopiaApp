package com.cryptopia.android.pPeterle.ui.fragment

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.*
import android.widget.Toast
import com.cryptopia.android.pPeterle.*
import com.cryptopia.android.pPeterle.presentation.*
import com.cryptopia.android.pPeterle.presentation.model.TradePairBinding
import com.cryptopia.android.pPeterle.presentation.viewModel.MarketViewModel
import com.cryptopia.android.pPeterle.ui.FragmentToolbar
import com.cryptopia.android.pPeterle.ui.ToolbarManager
import com.cryptopia.android.pPeterle.ui.activity.DetailsActivity
import com.cryptopia.android.pPeterle.ui.adapter.MarketRecyclerAdapter
import kotlinx.android.synthetic.main.fragment_market.*
import kotlinx.android.synthetic.main.fragment_market.view.*
import org.koin.android.viewmodel.ext.android.viewModel

class MarketFragment : Fragment() {

    private val mViewModel: MarketViewModel by viewModel()

    private lateinit var mAdapterMarket: MarketRecyclerAdapter

    enum class Market {BTC, USDT, NZDT, LTC, DOGE}

    companion object {

        private const val MARKET_KEY = "Market_Key"

        fun newInstance(market: Market) = MarketFragment().apply {
            val args = Bundle().apply { putSerializable(MARKET_KEY, market) }
            arguments = args
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_market, container, false)

        mAdapterMarket = MarketRecyclerAdapter(emptyList()) { tradePair ->
            startActivity(DetailsActivity.newInstance(tradePair.label, context!!))
        }

        view.market_recycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapterMarket
        }

        mViewModel.market = arguments?.getSerializable(MARKET_KEY) as Market

        mViewModel.getCurrencies()
        mViewModel.getState().observe(this, Observer {
            it?.let { handleState(it) }
        })

        //TODO melhorar aqui
        /*view.market_chng_tv.apply {
            setOnClickListener {
                if (isChecked) {
                    mViewModel.orderList(MarketViewModel.Sort.CHANGE, false)
                    setCompoundDrawablesWithIntrinsicBounds(null, null, getDrawable(R.drawable.ic_arrow_down), null)
                } else {
                    mViewModel.orderList(MarketViewModel.Sort.CHANGE, true)
                    setCompoundDrawablesWithIntrinsicBounds(null, null, getDrawable(R.drawable.ic_arrow_up), null)
                }

                setTextColor(getColor(R.color.colorTextPrimary))
            }
        }*/

        return view
    }

   override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_home, menu)
        val menuItem = menu?.findItem(R.id.action_search)
        val search = menuItem?.actionView as SearchView
        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String?): Boolean {
                search.clearFocus()
                return true
            }

            override fun onQueryTextChange(s: String?): Boolean {
                if (s != null) {
                    val newList = mAdapterMarket.list.filter {
                        it.label.toUpperCase().contains(s.toString().toUpperCase())
                    }
                    mAdapterMarket.list = newList
                    mAdapterMarket.notifyDataSetChanged()
                }
                return true
            }


        })
        super.onCreateOptionsMenu(menu, inflater);

    }

    private fun handleState(viewState: ViewState<List<TradePairBinding>>) {
        when (viewState) {
            is Success -> handleSuccess(viewState.data)
            is Failure -> handleError(viewState.error)
            is Loading -> handleLoading()
        }
    }

    private fun handleSuccess(data: List<TradePairBinding>) {
        market_loading.visibility = View.GONE
        market_recycler.visibility = View.VISIBLE
        mAdapterMarket.list = data
        mAdapterMarket.notifyDataSetChanged()
    }

    private fun handleError(error: Throwable) {
        market_loading.visibility = View.GONE
        market_loading.progress
        error.printStackTrace()
        Toast.makeText(activity, "Algum erro aconteceu", Toast.LENGTH_LONG).show()
    }

    private fun handleLoading() {
        market_loading.visibility = View.VISIBLE
        market_recycler.visibility = View.GONE
        market_loading.speed = 1.5f
    }


}
