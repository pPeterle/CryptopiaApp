package com.example.pedro.myapplication.data.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "OpenOrders")
data class OpenOrder(
    @PrimaryKey
    @SerializedName("OrderId") val orderId: Double,
    @SerializedName("TradePairId") val tradePairId: Double,
    @SerializedName("Market") val market: String,
    @SerializedName("Type") val type: String,
    @SerializedName("Rate") val rate: Double,
    @SerializedName("Amount") val amount: Double,
    @SerializedName("Total") val total: Double,
    @SerializedName("TimeStamp") val timeStamp: String
)