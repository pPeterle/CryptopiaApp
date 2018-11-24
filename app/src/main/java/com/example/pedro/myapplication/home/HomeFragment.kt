package com.example.pedro.myapplication.home

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.*
import android.widget.Toast
import com.example.pedro.myapplication.*
import com.example.pedro.myapplication.data.model.TradePair
import com.example.pedro.myapplication.details.DetailsActivity
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import org.koin.android.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private lateinit var mAdapter: RecyclerAdapter
    private var list = mutableListOf<TradePair>()

    companion object {
        fun newInstance() = HomeFragment()
    }

    private val mViewModel: HomeViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        (activity as MainActivity).toolbarTitle.text = "MARKET"
        setHasOptionsMenu(true)

        val recyclerView = view.home_recycler
        mAdapter = RecyclerAdapter(list) { apiReturn ->
            startActivity(DetailsActivity.newInstance(apiReturn.label, context!!))
        }
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
        }

        view.home_chng_tv.setOnClickListener {
            if (view.home_chng_tv.isChecked) {
                list.sortBy { it.change }
                mAdapter.notifyDataSetChanged()
                view.home_chng_tv.setCompoundDrawablesWithIntrinsicBounds(null, null, resources.getDrawable(R.drawable.ic_keyboard_arrow_down), null)
            } else {
                list.sortByDescending { it.change }
                mAdapter.notifyDataSetChanged()
                view.home_chng_tv.setCompoundDrawablesWithIntrinsicBounds(null, null, resources.getDrawable(R.drawable.ic_keyboard_arrow_up), null)
            }
        }

        return view
    }

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
                    mAdapter.list = newList
                    mAdapter.notifyDataSetChanged()
                }
                return true
            }


        })
        super.onCreateOptionsMenu(menu, inflater);

    }

    private fun handleState(viewState: ViewState<List<TradePair>>) {
        when (viewState) {
            is Success<List<TradePair>> -> handleSuccess(viewState.data)
            is Failure -> handleError(viewState.error)
            is Loading -> handleLoading()
        }
    }

    private fun handleSuccess(data: List<TradePair>) {
        home_loading.visibility = View.GONE
        home_recycler.visibility = View.VISIBLE
        list.clear()
        list.addAll(data)
        mAdapter.notifyDataSetChanged()
    }

    private fun handleError(error: Throwable) {
        home_loading.visibility = View.GONE
        error.printStackTrace()
        Toast.makeText(activity, "Algum erro aconteceu", Toast.LENGTH_LONG).show()
    }

    private fun handleLoading() {
        home_loading.visibility = View.VISIBLE
        home_loading.speed = 1.5f
        home_recycler.visibility = View.GONE
    }


}
