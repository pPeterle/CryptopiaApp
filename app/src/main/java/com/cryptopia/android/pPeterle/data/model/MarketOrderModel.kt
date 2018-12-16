package com.cryptopia.android.pPeterle.data.model

import com.google.gson.annotations.SerializedName

data class MarketOrder(
    @SerializedName("Label") val label: String,
    @SerializedName("Price") val price: Double,
    @SerializedName("Volume") var volume: Double,
    @SerializedName("Total") val total: Double
)

data class MarketOrders(
    @SerializedName("Buy") val buyOrders: List<MarketOrder>,
    @SerializedName("Sell")val sellOrders: List<MarketOrder>
)