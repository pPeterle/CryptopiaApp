package com.example.pedro.myapplication.domain

import android.graphics.Color
import android.util.Log
import com.example.pedro.myapplication.data.model.MarketOrders
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import java.util.ArrayList

object MarketOrdersMapper {

    fun map(marketOrders: MarketOrders): List<ILineDataSet> {

        for (order in marketOrders.sellOrders) {
            val position = marketOrders.sellOrders.indexOf(order)

            if (position > 0) {
                order.volume += marketOrders.sellOrders.get(position - 1).volume
            }

        }

        for (order in marketOrders.buyOrders) {
            val position = marketOrders.buyOrders.indexOf(order)

            if (position > 0) {
                order.volume += marketOrders.buyOrders.get(position - 1).volume
            }
        }

        //-----------------------//-----------------------//-----------------------


        val entryListBuy = mutableListOf<Entry>()
        val entryListSell = mutableListOf<Entry>()


        marketOrders.buyOrders.forEach {
            entryListBuy.add(Entry(it.price.toFloat(), it.volume.toFloat()))
        }

        marketOrders.sellOrders.forEach {
            entryListSell.add(Entry(it.price.toFloat(), it.volume.toFloat()))
        }

        //Conferir valores de buy
        Log.i("test", "buy: ${marketOrders.buyOrders}")
        Log.i("test", "sell: ${entryListSell}")
        //-----------------------//-----------------------//-----------------------
        val dataSets = ArrayList<ILineDataSet>()


        val lineDataSetSell = LineDataSet(entryListSell, "seldasdasdl").apply {
            color = Color.parseColor("#e64c1e")
            setDrawCircles(false)
            setDrawFilled(true)
            fillColor = Color.parseColor("#e64c1e")
        }

        val lineDataSetBuy = LineDataSet(entryListBuy.reversed(), "buyaa").apply {
            color = Color.GREEN
            setDrawCircles(false)
            setDrawFilled(true)
            fillColor = Color.GREEN
        }

        dataSets.add(lineDataSetBuy)
        dataSets.add(lineDataSetSell)


        return dataSets

    }
}