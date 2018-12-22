package com.cryptopia.android.pPeterle.presentation.model

data class TradeHistoryBinding(
    val type: String,
    val tradeId: Int,
    val rate: String,
    val amount: String,
    val total: String,
    val tradePairId: Int,
    val market: String,
    val symbol: String,
    val baseSymbol: String,
    val timeStamp: String
)