package com.cryptopia.android.pPeterle.ui.adapter

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.cryptopia.android.pPeterle.ui.fragment.CandleStickChartFragment
import com.cryptopia.android.pPeterle.ui.fragment.DepthChartFragment

class DetailsPageAdapter(private val label: String,
                         fragmentManager: FragmentManager)
    : FragmentPagerAdapter(fragmentManager) {

    override fun getItem(position: Int) = when (position) {
        0 -> CandleStickChartFragment.newInstance(label)
        1 -> DepthChartFragment.newInstance(label)
        else -> null
    }

    override fun getCount() = 2
}