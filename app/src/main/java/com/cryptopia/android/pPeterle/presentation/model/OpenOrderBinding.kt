package com.cryptopia.android.pPeterle.presentation.model

data class OpenOrderBinding(
    val orderId: Double,
    val tradePairId: Double,
    val currencyBase: String,
    val currencyCount: String,
    val market: String,
    val type: String,
    val price: String,
    val amount: String,
    val total: String,
    val timeStamp: String
)