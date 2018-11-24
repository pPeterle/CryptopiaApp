package com.example.pedro.myapplication.orders

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pedro.myapplication.R
import com.example.pedro.myapplication.data.model.OpenOrder
import kotlinx.android.synthetic.main.item_order.view.*

class OrdersRecyclerAdapter(var list: List<OpenOrder>, val onItemClick: (order: OpenOrder) -> Unit) :
    RecyclerView.Adapter<OrdersRecyclerAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_order, parent, false)
        return MyViewHolder(view, onItemClick)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.update(list.get(position))
    }

    class MyViewHolder(val view: View, val onItemClick: (order: OpenOrder) -> Unit) : RecyclerView.ViewHolder(view) {
        fun update(order: OpenOrder) {
            view.run {
                val (symbol, baseSymbol ) = order.market.split("/")
                order_currencyBase.text = symbol
                order_currencyCounter.text = baseSymbol
                order_amount.text = order.amount.toString()
                order_price.text = String.format("%.8f", order.rate)
                order_total.text = String.format("%.8f", order.total)
                if (order.type == "Buy") {
                    order_container.setBackgroundResource(R.drawable.order_bid)
                } else {
                    order_container.setBackgroundResource(R.drawable.order_ask)
                }

                order_animation.frame = order_animation.maxFrame.toInt() - 30
                order_animation.setOnClickListener {
                    order_animation.speed = -1f
                    order_animation.playAnimation()
                    onItemClick.invoke(order)
                }


            }
        }
    }
}