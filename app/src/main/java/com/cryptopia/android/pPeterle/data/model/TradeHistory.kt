package com.cryptopia.android.pPeterle.data.model

import com.google.gson.annotations.SerializedName

data class TradeHistory(
    @SerializedName("Type")
    val type: String = "",
    @SerializedName("TradeId")
    val tradeId: Int = 0,
    @SerializedName("Rate")
    val rate: Double = 0.0,
    @SerializedName("Amount")
    val amount: Double = 0.0,
    @SerializedName("Total")
    val total: String = "",
    @SerializedName("TradePairId")
    val tradePairId: Int = 0,
    @SerializedName("Market")
    val market: String = "",
    @SerializedName("TimeStamp")
    val timeStamp: String = ""
)