package com.example.pedro.myapplication.home

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pedro.myapplication.R
import com.example.pedro.myapplication.data.model.ApiReturn
import kotlinx.android.synthetic.main.item_recycler.view.*

class RecyclerAdapter(var list: List<ApiReturn>, val onClick: (ApiReturn) -> Unit): RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recycler, parent, false)
        return MyViewHolder(view, onClick)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.update(list.get(position))
    }

    class MyViewHolder(private val view: View, val onClick: (ApiReturn) -> Unit) : RecyclerView.ViewHolder(view) {

        fun update(apiReturn: ApiReturn) {
            view.item_change.apply {
                /*if (currencyPair.second..change < 0) {
                    setTextColor(Color.RED)
                } else if (data.change > 0) {
                    setTextColor(Color.GREEN)
                } else {
                    setTextColor(Color.WHITE)
                }*/
            }
            val (base, counter) = apiReturn.label.split("/")
            view.item_currencyBase.text = base
            view.item_currencyCounter.text = counter
            view.item_lastPrice.text = String.format("%.8f",apiReturn.lastPrice)

            val change = apiReturn.change
            view.item_change.text = change.toString()
            if (change > 0) {
                view.item_change.setBackgroundResource(R.drawable.btn_green)
            } else if (change < 0) {
                view.item_change.setBackgroundResource(R.drawable.btn_red)
            } else {
                view.item_change.setBackgroundResource(R.drawable.btn_gray)
            }

            view.item_container.setOnClickListener {
                onClick.invoke(apiReturn)
            }
        }
    }
}