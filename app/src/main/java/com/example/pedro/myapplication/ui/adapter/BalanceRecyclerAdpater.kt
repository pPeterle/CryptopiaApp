package com.example.pedro.myapplication.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pedro.myapplication.R
import com.example.pedro.myapplication.data.model.Balance
import com.example.pedro.myapplication.utils.toFormattedString
import kotlinx.android.synthetic.main.item_balance.view.*
import java.text.DecimalFormat

class BalanceRecyclerAdpater(var list: List<Balance>) : RecyclerView.Adapter<BalanceRecyclerAdpater.BalanceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): BalanceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_balance, parent, false)
        return BalanceViewHolder(view)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: BalanceViewHolder, position: Int) {
        holder.holder(list[position])
    }

    class BalanceViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        fun holder(balance: Balance) {
            view.apply {
                textView_balance_symbol.text = balance.symbol
                textView_balance_total.text = balance.total.toFormattedString()
                textView_balance_btc.text = balance.btcValue.toFormattedString()
            }
        }
    }
}