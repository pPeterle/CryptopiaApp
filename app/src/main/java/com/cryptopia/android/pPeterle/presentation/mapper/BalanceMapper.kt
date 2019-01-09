package com.cryptopia.android.pPeterle.presentation.mapper

import com.cryptopia.android.pPeterle.data.model.Balance
import com.cryptopia.android.pPeterle.presentation.model.BalanceBinding
import com.cryptopia.android.pPeterle.utils.toFormattedString

class BalanceMapper:Mapper<Balance, BalanceBinding> {
    override fun fromModel(model: Balance) = with(model) {
        BalanceBinding(
            currencyId,
            symbol,
            available.toFormattedString(),
            total.toFormattedString(),
            heldForTrades.toFormattedString(),
            btcValue.toFormattedString()
        )
    }

    override fun fromBinding(binding: BalanceBinding) = with(binding) {
        Balance(
            currencyId,
            symbol,
            available.toDouble(),
            total.toDouble(),
            heldForTrades.toDouble(),
            btcValue.toDouble()
        )
    }
}