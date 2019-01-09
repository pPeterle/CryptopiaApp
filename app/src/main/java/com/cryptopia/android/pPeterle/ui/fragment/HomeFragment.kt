package com.cryptopia.android.pPeterle.ui.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cryptopia.android.pPeterle.R
import com.cryptopia.android.pPeterle.presentation.viewModel.MarketViewModel
import com.cryptopia.android.pPeterle.ui.FragmentToolbar
import com.cryptopia.android.pPeterle.ui.ToolbarManager
import com.cryptopia.android.pPeterle.ui.adapter.HomePageApdater
import com.cryptopia.android.pPeterle.ui.adapter.TradeHistoryAdapter
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*

class HomeFragment : Fragment() {

    lateinit var mAdapter: HomePageApdater

    companion object {
        fun newInstance() = HomeFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        ToolbarManager(fragmentToolbarBuilder(), view).build()

        mAdapter = HomePageApdater(childFragmentManager)
        view.viewPager_home.adapter = mAdapter

        view.tabLayout_home.setupWithViewPager(view.viewPager_home)

        view.text_home_change.apply {
            setOnClickListener {

                view.text_home_tradePair.setTextColor(resources.getColor(R.color.colorTextSecondary))
                view.text_home_tradePair.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)

                view.text_home_volume.setTextColor(resources.getColor(R.color.colorTextSecondary))
                view.text_home_volume.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)

                view.text_home_price.setTextColor(resources.getColor(R.color.colorTextSecondary))
                view.text_home_price.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)

                if (isChecked) {
                    mAdapter.currentFragment?.sortList(MarketViewModel.Sort.CHANGE)
                    setCompoundDrawablesWithIntrinsicBounds(
                        null,
                        null,
                        resources.getDrawable(R.drawable.ic_arrow_up),
                        null
                    )
                } else {
                    mAdapter.currentFragment?.sortListDescending(MarketViewModel.Sort.CHANGE)
                    setCompoundDrawablesWithIntrinsicBounds(
                        null,
                        null,
                        resources.getDrawable(R.drawable.ic_arrow_down),
                        null
                    )
                }
                setTextColor(resources.getColor(R.color.colorTextPrimary))
            }

        }

        view.text_home_price.apply {

            setOnClickListener {

                view.text_home_tradePair.setTextColor(resources.getColor(R.color.colorTextSecondary))
                view.text_home_tradePair.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)

                view.text_home_volume.setTextColor(resources.getColor(R.color.colorTextSecondary))
                view.text_home_volume.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)

                view.text_home_change.setTextColor(resources.getColor(R.color.colorTextSecondary))
                view.text_home_change.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)

                if (isChecked) {
                    mAdapter.currentFragment?.sortList(MarketViewModel.Sort.PRICE)
                    setCompoundDrawablesWithIntrinsicBounds(
                        resources.getDrawable(R.drawable.ic_arrow_up),
                        null,
                        null,
                        null
                    )
                } else {
                    mAdapter.currentFragment?.sortListDescending(MarketViewModel.Sort.PRICE)
                    setCompoundDrawablesWithIntrinsicBounds(
                        resources.getDrawable(R.drawable.ic_arrow_down),
                        null,
                        null,
                        null
                    )
                }
                setTextColor(resources.getColor(R.color.colorTextPrimary))
            }
        }

        view.text_home_tradePair.apply {

            setOnClickListener {

                view.text_home_price.setTextColor(resources.getColor(R.color.colorTextSecondary))
                view.text_home_price.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)

                view.text_home_volume.setTextColor(resources.getColor(R.color.colorTextSecondary))
                view.text_home_volume.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)

                view.text_home_change.setTextColor(resources.getColor(R.color.colorTextSecondary))
                view.text_home_change.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)

                if (isChecked) {
                    mAdapter.currentFragment?.sortList(MarketViewModel.Sort.NAME)
                    setCompoundDrawablesWithIntrinsicBounds(
                        null,
                        null,
                        resources.getDrawable(R.drawable.ic_arrow_up),
                        null
                    )
                } else {
                    mAdapter.currentFragment?.sortListDescending(MarketViewModel.Sort.NAME)
                    setCompoundDrawablesWithIntrinsicBounds(
                        null,
                        null,
                        resources.getDrawable(R.drawable.ic_arrow_down),
                        null
                    )
                }
                setTextColor(resources.getColor(R.color.colorTextPrimary))
            }

        }
        view.text_home_volume.apply {

            setOnClickListener {

                view.text_home_price.setTextColor(resources.getColor(R.color.colorTextSecondary))
                view.text_home_price.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)

                view.text_home_tradePair.setTextColor(resources.getColor(R.color.colorTextSecondary))
                view.text_home_tradePair.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)

                view.text_home_change.setTextColor(resources.getColor(R.color.colorTextSecondary))
                view.text_home_change.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)

                if (isChecked) {
                    mAdapter.currentFragment?.sortList(MarketViewModel.Sort.VOLUME)
                    setCompoundDrawablesWithIntrinsicBounds(
                        null,
                        null,
                        resources.getDrawable(R.drawable.ic_arrow_up),
                        null
                    )
                } else {
                    mAdapter.currentFragment?.sortListDescending(MarketViewModel.Sort.VOLUME)
                    setCompoundDrawablesWithIntrinsicBounds(
                        null,
                        null,
                        resources.getDrawable(R.drawable.ic_arrow_down),
                        null
                    )
                }
                setTextColor(resources.getColor(R.color.colorTextPrimary))
            }
        }



        return view
    }

    override fun onDestroyView() {
        viewPager_home.adapter = null
        super.onDestroyView()
    }

    private fun fragmentToolbarBuilder() = FragmentToolbar.Builder()
        .withId(R.id.toolbar_home)
        .withMenu(R.menu.menu_home)
        .withSearchView(
            R.id.action_search,
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(s: String?) = true

                override fun onQueryTextChange(s: String?): Boolean {
                    s?.let {
                        mAdapter.currentFragment?.filterListByText(s)
                        Log.i("test", "onQueryTextChange: $s")
                    }
                    return true
                }


            })
        .build()
}