package com.cryptopia.android.pPeterle.ui.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cryptopia.android.pPeterle.R
import com.cryptopia.android.pPeterle.ui.FragmentToolbar
import com.cryptopia.android.pPeterle.ui.ToolbarManager
import com.cryptopia.android.pPeterle.ui.adapter.HomePageApdater
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*

class HomeFragment: Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        ToolbarManager(fragmentToolbarBuilder(), view).prepareToolbar()

        view.viewPager_home.adapter = HomePageApdater(childFragmentManager)

        view.tabLayout_home.setupWithViewPager(view.viewPager_home)

        return view
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
                        //mViewModel.filterListString(s)
                        Log.i("test", "onQueryTextChange: $s")
                    }
                    return true
                }


            })
        .build()
}