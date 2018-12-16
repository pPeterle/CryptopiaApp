package com.cryptopia.android.pPeterle.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cryptopia.android.pPeterle.R
import com.cryptopia.android.pPeterle.data.model.TradePair
import com.cryptopia.android.pPeterle.databinding.ItemCurrencyBinding
import com.cryptopia.android.pPeterle.presentation.model.TradePairBinding
import com.cryptopia.android.pPeterle.utils.toFormattedString
import kotlinx.android.synthetic.main.item_currency.view.*

class HomeRecyclerAdapter(var list: List<TradePairBinding>, val onClick: (TradePairBinding) -> Unit) :
    RecyclerView.Adapter<HomeRecyclerAdapter.HomeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): HomeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_currency, parent, false)
        val binding = ItemCurrencyBinding.bind(view)
        return HomeViewHolder(binding, onClick)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.update(list.get(position))
    }

    inner class HomeViewHolder(private val binding: ItemCurrencyBinding, val onClick: (TradePairBinding) -> Unit) :
        RecyclerView.ViewHolder(binding.root) {

        fun update(tradePair: TradePairBinding) {
            binding.tradePair = tradePair

            when {
                tradePair.change.toDouble() > 0 -> binding.itemChange.setBackgroundResource(R.drawable.btn_green)
                tradePair.change.toDouble() < 0 -> binding.itemChange.setBackgroundResource(R.drawable.btn_red)
                else -> binding.itemChange.setBackgroundResource(R.drawable.btn_gray)
            }

            binding.itemContainer.setOnClickListener {
                onClick.invoke(tradePair)
            }
            binding.executePendingBindings()
        }
    }
}