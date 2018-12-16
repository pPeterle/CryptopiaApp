package com.cryptopia.android.pPeterle.presentation.model

data class TradePairBinding(
    val id: Int,
    val label: String,
    val symbol: String,
    val baseSymbol: String,
    val lastPrice: String,
    val low: String,
    val high: String,
    val change: String,
    val volume: String
)