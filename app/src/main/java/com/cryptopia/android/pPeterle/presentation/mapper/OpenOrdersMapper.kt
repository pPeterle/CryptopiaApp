package com.cryptopia.android.pPeterle.presentation.mapper

import com.cryptopia.android.pPeterle.data.model.OpenOrder
import com.cryptopia.android.pPeterle.presentation.model.OpenOrderBinding
import com.cryptopia.android.pPeterle.utils.toFormattedString

class OpenOrdersMapper : Mapper<OpenOrder, OpenOrderBinding> {

    override fun fromModel(model: OpenOrder) = with(model) {
            OpenOrderBinding(
                orderId,
                tradePairId,
                market.split("/")[0],
                market.split("/")[1],
                market,
                type,
                price.toFormattedString(),
                amount.toFormattedString(),
                total.toFormattedString(),
                timeStamp
            )
        }

    override fun fromBinding(binding: OpenOrderBinding) = with(binding) {
        OpenOrder(
            orderId,
            tradePairId,
            market,
            type,
            price.toDouble(),
            amount.toDouble(),
            total.toDouble(),
            timeStamp
        )
    }
}