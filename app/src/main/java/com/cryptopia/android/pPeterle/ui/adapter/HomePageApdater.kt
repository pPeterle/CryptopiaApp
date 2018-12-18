package com.cryptopia.android.pPeterle.ui.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.cryptopia.android.pPeterle.ui.fragment.MarketFragment
import java.lang.RuntimeException

class HomePageApdater(fragmentManager: FragmentManager): FragmentPagerAdapter(fragmentManager) {

    private val titleList = listOf("BTC", "USDT", "NZDT", "LTC", "DOGE")

    override fun getItem(position: Int) = when (position) {
        0 -> MarketFragment.newInstance(MarketFragment.Market.BTC)
        1 -> MarketFragment.newInstance(MarketFragment.Market.USDT)
        2 -> MarketFragment.newInstance(MarketFragment.Market.NZDT)
        3 -> MarketFragment.newInstance(MarketFragment.Market.LTC)
        4 -> MarketFragment.newInstance(MarketFragment.Market.DOGE)
        else -> throw RuntimeException("No Title Added")
    }

    override fun getCount() = titleList.size

    override fun getPageTitle(position: Int) = titleList[position]
}