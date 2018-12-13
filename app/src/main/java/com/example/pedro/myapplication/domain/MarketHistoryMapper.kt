package com.example.pedro.myapplication.domain

import android.util.Log
import com.example.pedro.myapplication.data.model.MarketHistory
import com.github.mikephil.charting.data.CandleEntry
import java.math.BigDecimal
import java.util.*

object MarketHistoryMapper {
    fun map(list: MutableList<MarketHistory>, hours: Int = 24): List<CandleEntry> {
        val candleEntryList = mutableListOf<CandleEntry>()

        val finalTimestamp = list.first().timestamp

        for (i in 1 until hours) {
            val hour = finalTimestamp - (3600 * i)
            val hourList = list.filter { it.timestamp >= hour }
            list.removeAll(hourList)
            
            Log.i("test", "map: $hourList")

            if (!hourList.isEmpty()) {

                val open = hourList.last().price
                val hight = hourList.maxBy { it.price }?.price?.toFloat()
                val min = hourList.minBy { it.price }?.price?.toFloat()
                val close = hourList.first().price
                candleEntryList.add(CandleEntry((hours - i).toFloat(), hight!!, min!!, open.toFloat(), close.toFloat()))

            }
        }

        Log.i("test", "candleList: $candleEntryList")

        return candleEntryList.reversed()
    }
}