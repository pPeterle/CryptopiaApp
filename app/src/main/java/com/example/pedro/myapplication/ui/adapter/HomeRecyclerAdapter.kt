package com.example.pedro.myapplication.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pedro.myapplication.R
import com.example.pedro.myapplication.data.model.TradePair
import com.example.pedro.myapplication.utils.toFormattedString
import kotlinx.android.synthetic.main.item_currency.view.*

class HomeRecyclerAdapter(var list: List<TradePair>, val onClick: (TradePair) -> Unit): RecyclerView.Adapter<HomeRecyclerAdapter.HomeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): HomeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_currency, parent, false)
        return HomeViewHolder(view, onClick)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.update(list.get(position))
    }

    inner class HomeViewHolder(private val view: View, val onClick: (TradePair) -> Unit) : RecyclerView.ViewHolder(view) {

        fun update(tradePair: TradePair) {

            val (base, counter) = tradePair.label.split("/")
            view.item_currencyBase.text = base
            view.item_currencyCounter.text = counter
            view.item_lastPrice.text = tradePair.lastPrice.toFormattedString()
            view.item_volume.text = tradePair.volume.toFormattedString("2")

            val change = tradePair.change
            view.item_change.text = change.toString()
            when {
                change > 0 -> view.item_change.setBackgroundResource(R.drawable.btn_green)
                change < 0 -> view.item_change.setBackgroundResource(R.drawable.btn_red)
                else -> view.item_change.setBackgroundResource(R.drawable.btn_gray)
            }

            view.item_container.setOnClickListener {
                onClick.invoke(tradePair)
            }
        }
    }
}