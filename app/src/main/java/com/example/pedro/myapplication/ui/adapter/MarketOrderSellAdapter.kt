package com.example.pedro.myapplication.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pedro.myapplication.R
import com.example.pedro.myapplication.data.model.MarketOrder
import com.example.pedro.myapplication.utils.toFormattedString
import kotlinx.android.synthetic.main.item_market_order_sell.view.*

class MarketOrderSellAdapter(val list: List<MarketOrder>): RecyclerView.Adapter<MarketOrderSellAdapter.MarketOrderSellViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarketOrderSellViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_market_order_sell, parent, false)
        return MarketOrderSellViewHolder(view)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: MarketOrderSellViewHolder, position: Int) {
        val order = list[position]

        with(holder.view) {
           textView_marketOrderSell_value.text = order.volume.toFormattedString("5")
            textView_marketOrderSell_price.text = order.price.toFormattedString()
        }
    }

    class MarketOrderSellViewHolder(val view: View): RecyclerView.ViewHolder(view)
}