package com.example.pedro.myapplication.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pedro.myapplication.R
import com.example.pedro.myapplication.data.model.MarketOrder
import com.example.pedro.myapplication.utils.toFormattedString
import kotlinx.android.synthetic.main.item_market_order_buy.view.*

class MarketOrderBuyAdapter(val list: List<MarketOrder>): RecyclerView.Adapter<MarketOrderBuyAdapter.MarketOrderBuyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarketOrderBuyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_market_order_buy, parent, false)
        return MarketOrderBuyViewHolder(view)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: MarketOrderBuyViewHolder, position: Int) {
        val order = list[position]

        with(holder.view) {
            textView_marketOrderBuy_value.text = order.volume.toFormattedString("5")
            textView_marketOrderBuy_price.text = order.price.toFormattedString()
        }
    }

    class MarketOrderBuyViewHolder(val view: View): RecyclerView.ViewHolder(view)
}