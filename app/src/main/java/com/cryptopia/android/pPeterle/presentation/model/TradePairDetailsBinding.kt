package com.cryptopia.android.pPeterle.presentation.model

import com.cryptopia.android.pPeterle.data.model.MarketHistory
import com.cryptopia.android.pPeterle.data.model.MarketOrders
import com.cryptopia.android.pPeterle.data.model.TradePair

data class TradePairDetailsBinding(
    val marketDetails: TradePairBinding,
    val counterAmount: String,
    val baseAmount: String,
    val marketOrders: MarketOrders
)