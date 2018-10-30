package com.example.pedro.myapplication.data.model

import com.google.gson.annotations.SerializedName

data class ApiReturn(
    @SerializedName("TradePairId") val id: Int,
    @SerializedName("Label") val label: String,
    @SerializedName("LastPrice") val lastPrice: Double,
    @SerializedName("Change") val change: Double,
    @SerializedName("BaseVolume") val volume: Double
)

data class Api(
    @SerializedName("Data") val data: List<ApiReturn>
)