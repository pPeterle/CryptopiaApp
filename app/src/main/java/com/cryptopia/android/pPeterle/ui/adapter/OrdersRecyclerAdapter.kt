package com.cryptopia.android.pPeterle.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cryptopia.android.pPeterle.R
import com.cryptopia.android.pPeterle.data.model.OpenOrder
import com.cryptopia.android.pPeterle.databinding.ItemOrderBinding
import com.cryptopia.android.pPeterle.presentation.model.OpenOrderBinding
import com.cryptopia.android.pPeterle.utils.toFormattedString

class OrdersRecyclerAdapter(var list: List<OpenOrderBinding>, val onItemClick: (order: OpenOrderBinding) -> Unit) :
    RecyclerView.Adapter<OrdersRecyclerAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_order, parent, false)
        val binding = ItemOrderBinding.bind(view)

        return MyViewHolder(binding, onItemClick)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.update(list.get(position))
    }

    class MyViewHolder(val binding: ItemOrderBinding, private val onItemClick: (order: OpenOrderBinding) -> Unit) :
        RecyclerView.ViewHolder(binding.root) {

        fun update(order: OpenOrderBinding) {

            with(binding) {

                openOrder = order

                orderAnimation.frame = orderAnimation.maxFrame.toInt() - 30
                orderAnimation.setOnClickListener {
                    orderAnimation.speed = -1f
                    orderAnimation.playAnimation()
                    onItemClick.invoke(order)
                }

                executePendingBindings()
            }
        }
    }
}