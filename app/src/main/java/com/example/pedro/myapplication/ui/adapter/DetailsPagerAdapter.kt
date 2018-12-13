package com.example.pedro.myapplication.ui.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.example.pedro.myapplication.ui.fragment.CandleStickChartFragment
import com.example.pedro.myapplication.ui.fragment.DepthChartFragment

class DetailsPagerAdapter(private val label: String,
                          fragmentManager: FragmentManager)
    : FragmentPagerAdapter(fragmentManager) {

    override fun getItem(position: Int) = when (position) {
        0 -> CandleStickChartFragment.newInstance(label)
        1 -> DepthChartFragment.newInstance(label)
        else -> null
    }

    override fun getCount() = 2
}