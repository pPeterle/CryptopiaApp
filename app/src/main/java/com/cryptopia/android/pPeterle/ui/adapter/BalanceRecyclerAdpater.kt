package com.cryptopia.android.pPeterle.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cryptopia.android.pPeterle.R
import com.cryptopia.android.pPeterle.data.model.Balance
import com.cryptopia.android.pPeterle.databinding.ItemBalanceBinding
import com.cryptopia.android.pPeterle.presentation.model.BalanceBinding
import com.cryptopia.android.pPeterle.utils.toFormattedString
import kotlinx.android.synthetic.main.item_balance.view.*

class BalanceRecyclerAdpater(var list: List<BalanceBinding>) : RecyclerView.Adapter<BalanceRecyclerAdpater.BalanceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): BalanceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_balance, parent, false)
        val binding = ItemBalanceBinding.bind(view)
        return BalanceViewHolder(binding)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: BalanceViewHolder, position: Int) {
        holder.holder(list[position])
    }

    class BalanceViewHolder(val binding: ItemBalanceBinding) : RecyclerView.ViewHolder(binding.root) {

        fun holder(balance: BalanceBinding) {
            binding.balance = balance
            binding.executePendingBindings()
        }
    }
}