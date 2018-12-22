package com.cryptopia.android.pPeterle.presentation.mapper

import com.cryptopia.android.pPeterle.data.model.TradeHistory
import com.cryptopia.android.pPeterle.presentation.model.TradeHistoryBinding
import com.cryptopia.android.pPeterle.utils.toFormattedString

class TradeHistoryMapper: Mapper<TradeHistory, TradeHistoryBinding> {
    override fun fromModel(model: TradeHistory): TradeHistoryBinding = with(model) {
        TradeHistoryBinding(
            type,
            tradeId,
            rate.toFormattedString(),
            amount.toFormattedString(),
            total,
            tradePairId,
            market,
            market.split("/")[0],
            market.split("/")[1],
            timeStamp
        )
    }

    override fun fromBinding(binding: TradeHistoryBinding) = with(binding) {
        TradeHistory(
            type,
            tradeId,
            rate.toDouble(),
            amount.toDouble(),
            total,
            tradePairId,
            market,
            timeStamp
        )
    }
}