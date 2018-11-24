package com.example.pedro.myapplication.data.model

import com.google.gson.annotations.SerializedName

data class MarketHistory(
    @SerializedName("TradePairId") val tradePairId: Double,
    @SerializedName("Label") val label: String,
    @SerializedName("Type") val type: String,
    @SerializedName("Price") val price: Double,
    @SerializedName("Amount") val amount: Double,
    @SerializedName("Timestamp") val timestamp: Double
)