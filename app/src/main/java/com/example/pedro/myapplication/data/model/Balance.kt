package com.example.pedro.myapplication.data.model

import com.google.gson.annotations.SerializedName

data class Balance(
    @SerializedName("CurrencyId") val currencyId: Double,
    @SerializedName("Symbol") val symbol: String,
    @SerializedName("Available") val available: Double,
    @SerializedName("Total") val total: Double,
    @SerializedName("HeldForTrades") val heldForTrades: Double,
    var btcValue: Double = 0.0
)