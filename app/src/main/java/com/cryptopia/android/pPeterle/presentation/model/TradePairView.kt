package com.cryptopia.android.pPeterle.presentation.model

import com.cryptopia.android.pPeterle.data.model.MarketHistory
import com.cryptopia.android.pPeterle.data.model.MarketOrders
import com.cryptopia.android.pPeterle.data.model.TradePair

data class TradePairView(
    val marketDetails: TradePair,
    val counterAmount: Double,
    val baseAmount: Double,
    val marketOrders: MarketOrders
)