package com.example.pedro.myapplication.data.model

data class TradePairDetails(
    val marketDetails: TradePair,
    val counterAmount: Double,
    val baseAmount: Double,
    val marketHistory: List<MarketHistory>,
    val marketOrders: MarketOrders
)