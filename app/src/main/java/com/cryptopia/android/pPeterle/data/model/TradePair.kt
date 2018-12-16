package com.cryptopia.android.pPeterle.data.model

import com.google.gson.annotations.SerializedName

data class TradePair(
    @SerializedName("TradePairId") val id: Int,
    @SerializedName("Label") val label: String,
    @SerializedName("LastPrice") val lastPrice: Double,
    @SerializedName("Low") val low: Double,
    @SerializedName("High") val high: Double,
    @SerializedName("Change") val change: Double,
    @SerializedName("BaseVolume") val volume: Double
)