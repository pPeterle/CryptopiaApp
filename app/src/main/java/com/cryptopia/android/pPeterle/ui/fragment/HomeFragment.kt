package com.cryptopia.android.pPeterle.ui.fragment

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.*
import android.widget.Toast
import com.cryptopia.android.pPeterle.*
import com.cryptopia.android.pPeterle.data.model.TradePair
import com.cryptopia.android.pPeterle.presentation.*
import com.cryptopia.android.pPeterle.presentation.model.TradePairBinding
import com.cryptopia.android.pPeterle.presentation.viewModel.HomeViewModel
import com.cryptopia.android.pPeterle.ui.FragmentToolbar
import com.cryptopia.android.pPeterle.ui.ToolbarManager
import com.cryptopia.android.pPeterle.ui.activity.DetailsActivity
import com.cryptopia.android.pPeterle.ui.adapter.HomeRecyclerAdapter
import com.cryptopia.android.pPeterle.utils.getColor
import com.cryptopia.android.pPeterle.utils.getDrawable
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import org.koin.android.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private lateinit var mAdapterHome: HomeRecyclerAdapter
    private var list = mutableListOf<TradePairBinding>()

    companion object {
        fun newInstance() = HomeFragment()
    }

    private val mViewModel: HomeViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        ToolbarManager(fragmentToolbarBuilder(), view).prepareToolbar()

        mAdapterHome = HomeRecyclerAdapter(list) { tradePair ->
            startActivity(DetailsActivity.newInstance(tradePair.label, context!!))
        }

        view.home_recycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapterHome
        }

        //TODO melhorar aqui
        view.home_chng_tv.apply {
            setOnClickListener {
                if (isChecked) {
                    mViewModel.orderList(HomeViewModel.Sort.CHANGE, false)
                    setCompoundDrawablesWithIntrinsicBounds(null, null, getDrawable(R.drawable.ic_arrow_down), null)
                } else {
                    mViewModel.orderList(HomeViewModel.Sort.CHANGE, true)
                    setCompoundDrawablesWithIntrinsicBounds(null, null, getDrawable(R.drawable.ic_arrow_up), null)
                }

                setTextColor(getColor(R.color.colorTextPrimary))
            }
        }

        return view
    }

    private fun fragmentToolbarBuilder() = FragmentToolbar.Builder()
        .withId(R.id.toolbar_home)
        .withMenu(R.menu.menu_home)
        .withSearchView(R.id.action_search,
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(s: String?) = true

                override fun onQueryTextChange(s: String?): Boolean {
                    s?.let {
                        mViewModel.filterListString(s)
                    }
                    return true
                }


            })
        .build()


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mViewModel.getMarkets()
        mViewModel.getState().observe(this, Observer {
            it?.let { handleState(it) }
        })
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
                    val newList = list.filter {
                        it.label.toUpperCase().contains(s.toString().toUpperCase())
                    }
                    mAdapterHome.list = newList
                    mAdapterHome.notifyDataSetChanged()
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
        home_loading.visibility = View.GONE
        home_recycler.visibility = View.VISIBLE
        list.clear()
        list.addAll(data)
        mAdapterHome.notifyDataSetChanged()
    }

    private fun handleError(error: Throwable) {
        home_loading.visibility = View.GONE
        home_loading.progress
        error.printStackTrace()
        Toast.makeText(activity, "Algum erro aconteceu", Toast.LENGTH_LONG).show()
    }

    private fun handleLoading() {
        home_loading.visibility = View.VISIBLE
        home_loading.speed = 1.5f
        home_recycler.visibility = View.GONE
    }


}
