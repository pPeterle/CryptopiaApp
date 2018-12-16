package com.cryptopia.android.pPeterle.presentation.mapper

import com.cryptopia.android.pPeterle.data.model.TradePair
import com.cryptopia.android.pPeterle.presentation.model.TradePairBinding
import com.cryptopia.android.pPeterle.utils.toFormattedString

class TradePairMapper: Mapper<TradePair, TradePairBinding> {
    override fun fromModel(model: TradePair): TradePairBinding = with(model) {
        TradePairBinding(
            id,
            label,
            label.split("/")[0],
            label.split("/")[1],
            lastPrice.toFormattedString(),
            low.toFormattedString(),
            high.toFormattedString(),
            change.toString(),
            volume.toFormattedString("2")
        )
    }

    override fun fromBinding(binding: TradePairBinding) = with(binding) {
        TradePair(
            id,
            label,
            lastPrice.toDouble(),
            low.toDouble(),
            high.toDouble(),
            change.toDouble(),
            volume.toDouble()
        )
    }
}