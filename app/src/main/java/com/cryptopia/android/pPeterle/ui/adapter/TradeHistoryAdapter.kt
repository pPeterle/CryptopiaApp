package com.cryptopia.android.pPeterle.ui.adapter

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.cryptopia.android.pPeterle.R
import com.cryptopia.android.pPeterle.databinding.ItemTradeHistoryBinding
import com.cryptopia.android.pPeterle.presentation.model.TradeHistoryBinding

class TradeHistoryAdapter(var list: List<TradeHistoryBinding>) :

    RecyclerView.Adapter<TradeHistoryAdapter.TradeHistoryViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TradeHistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_trade_history, parent, false)
        val binding = ItemTradeHistoryBinding.bind(view)
        return TradeHistoryViewHolder(binding)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: TradeHistoryViewHolder, position: Int) {
        holder.bind(list[position])
    }

    class TradeHistoryViewHolder(private val binding: ItemTradeHistoryBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(tradeHistory: TradeHistoryBinding) {
            binding.tradeHistory = tradeHistory
            binding.executePendingBindings()
        }
    }
}