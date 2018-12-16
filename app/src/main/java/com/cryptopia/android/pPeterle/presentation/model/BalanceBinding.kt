package com.cryptopia.android.pPeterle.presentation.model

data class BalanceBinding(
    val currencyId: Double,
    val symbol: String,
    val available: String,
    val total: String,
    val heldForTrades: String,
    val btcValue: String
)