package com.example.pedro.myapplication.data.model

import com.google.gson.annotations.SerializedName

data class TradePair(
    @SerializedName("TradePairId") val id: Int,
    @SerializedName("Label") val label: String,
    @SerializedName("Symbol") val symbol: String,
    @SerializedName("BaseSymbol") val baseSymbol: String,
    @SerializedName("LastPrice") val lastPrice: Double,
    @SerializedName("Change") val change: Double,
    @SerializedName("BaseVolume") val volume: Double
)