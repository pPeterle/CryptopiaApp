package com.cryptopia.android.pPeterle.data.model

import com.google.gson.annotations.SerializedName

data class ApiReturn<T>(
    @SerializedName("Data") val data: T,
    @SerializedName("Success") val success: Boolean,
    @SerializedName("Error") val error: String
)