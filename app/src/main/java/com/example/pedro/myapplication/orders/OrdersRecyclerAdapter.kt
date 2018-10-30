package com.example.pedro.myapplication.orders

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pedro.myapplication.R
import kotlinx.android.synthetic.main.orders_item.view.*
import org.knowm.xchange.dto.trade.LimitOrder

class OrdersRecyclerAdapter(var list: List<LimitOrder>, val onItemClick: (order: LimitOrder) -> Unit) :
    RecyclerView.Adapter<OrdersRecyclerAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.orders_item, parent, false)
        return MyViewHolder(view, onItemClick)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.update(list.get(position))
    }

    class MyViewHolder(val view: View, val onItemClick: (order: LimitOrder) -> Unit) : RecyclerView.ViewHolder(view) {
        fun update(order: LimitOrder) {
            view.run {
                order_currencyBase.text = order.currencyPair.base.toString()
                order_currencyCounter.text = order.currencyPair.counter.toString()
                order_amount.text = order.originalAmount.toPlainString()
                order_price.text = order.limitPrice.toPlainString()
                order_total.text = String.format("%.8f", order.originalAmount.times(order.limitPrice).toFloat())
                if (order.type.name == "BID") {
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