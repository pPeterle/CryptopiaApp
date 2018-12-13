package com.example.pedro.myapplication.data.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "Balances")
data class Balance(
    @PrimaryKey
    @SerializedName("CurrencyId") val currencyId: Double,
    @SerializedName("Symbol") val symbol: String,
    @SerializedName("Available") val available: Double,
    @SerializedName("Total") val total: Double,
    @SerializedName("HeldForTrades") val heldForTrades: Double,
    var btcValue: Double = 0.0
)