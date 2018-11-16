package com.example.pedro.myapplication.details

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.example.pedro.myapplication.*
import com.example.pedro.myapplication.data.CryptopiaRepositoty
import com.example.pedro.myapplication.data.model.TradePair
import com.example.pedro.myapplication.data.model.TradePairDetails
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class DetailsViewModel(private val cryptopiaRepositoty: CryptopiaRepositoty) : ScopedViewModel() {

    private val state = MutableLiveData<ViewState<TradePairDetails>>()
    lateinit var tradePair: String

    fun setBuyOrder(orginalAmount: String, price: String) {
        if (orginalAmount.isEmpty()) {
            state.postValue(Failure(Error("Amount is empty")))
            return
        }
        if (price.isEmpty()) {
            state.postValue(Failure(Error("Amount is empty")))
            return
        }
        launch(IO) {
            state.postValue(Loading())
            try {

                val trade = cryptopiaRepositoty.submitTrade(
                    tradePair,
                    "Buy",
                    price,
                    orginalAmount
                ).await()

                if (trade.success) {
                    getDetails()
                } else {
                    state.postValue(Failure(Error(trade.error)))
                }
            } catch (error: Throwable) {
                state.postValue(Failure(error))
            }
        }
    }

    fun setSellOrder(orginalAmount: String, price: String) {

        if (orginalAmount.isEmpty()) {
            state.postValue(Failure(Error("Amount is empty")))
            return
        }
        if (price.isEmpty()) {
            state.postValue(Failure(Error("Amount is empty")))
            return
        }

        launch(IO) {
            state.postValue(Loading())
            try {
                val trade = cryptopiaRepositoty.submitTrade(
                    tradePair,
                    "Sell",
                    price,
                    orginalAmount
                ).await()

                if (trade.success) {
                    getDetails()
                } else {
                    state.postValue(Failure(Error(trade.error)))
                }
            } catch (error: Throwable) {
                state.postValue(Failure(error))
            }
        }

    }

    fun getDetails() {
        launch(IO) {
            val (symbol, baseSymbol) = tradePair.split("/")

            try {

                val balance = cryptopiaRepositoty.getBalance(symbol).await()
                val balanceBase = cryptopiaRepositoty.getBalance(baseSymbol).await()
                val tradePair = cryptopiaRepositoty.getMarket(tradePair).await().data


                if (balance.success) {
                    state.postValue(
                        Success(
                            TradePairDetails(
                                tradePair.lastPrice,
                                balance.data[0].available,
                                balanceBase.data[0].available
                            )
                        )
                    )
                } else {
                    state.postValue(
                        Success(
                            TradePairDetails(
                                tradePair.lastPrice,
                                0.0,
                                balanceBase.data[0].available
                            )
                        )
                    )
                }

            } catch (error: Exception) {
                state.postValue(Failure(error))
            }


        }
    }

    fun getState() = state as LiveData<ViewState<TradePairDetails>>

    fun calculateTotal(price: String, amount: String): String {
        if (!amount.isEmpty() && !price.isEmpty()) {
            if (amount.toFloat() > 0 && price.toFloat() > 0)
                return String.format("%.8f", price.toFloat() * amount.toFloat())
        }

        return ""
    }

    fun calculateAmount(price: String, total: String): String {
        if (!total.isEmpty() && !price.isEmpty()) {
            if (total.toFloat() > 0 && price.toFloat() > 0)
                return String.format("%.8f", total.toFloat() / price.toFloat())
        }

        return ""
    }

}
