package com.example.pedro.myapplication.details

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.example.pedro.myapplication.Failure
import com.example.pedro.myapplication.ScopedViewModel
import com.example.pedro.myapplication.Success
import com.example.pedro.myapplication.ViewState
import com.example.pedro.myapplication.data.CryptopiaRepositoty
import com.example.pedro.myapplication.data.model.TradePairDetails
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.knowm.xchange.currency.CurrencyPair
import org.knowm.xchange.dto.Order
import org.knowm.xchange.dto.account.Balance
import org.knowm.xchange.dto.trade.LimitOrder
import java.math.BigDecimal
import java.util.*

class DetailsViewModel(private val cryptopiaRepositoty: CryptopiaRepositoty) : ScopedViewModel() {

    private val state = MutableLiveData<ViewState<TradePairDetails>>()
    lateinit var currencyPair: CurrencyPair

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
            try {

                cryptopiaRepositoty.placeLimitOrder(
                    LimitOrder(
                        Order.OrderType.BID,
                        BigDecimal(orginalAmount),
                        currencyPair,
                        currencyPair.counter.currencyCode,
                        Date(),
                        BigDecimal(price)
                    )
                )
            } catch (error: Throwable) {
                state.postValue(Failure(error))
            }

            getDetails()
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
            try {
                cryptopiaRepositoty.placeLimitOrder(
                    LimitOrder(
                        Order.OrderType.ASK,
                        BigDecimal(orginalAmount),
                        currencyPair,
                        currencyPair.counter.currencyCode,
                        Date(),
                        BigDecimal(price)
                    )
                )
                getDetails()
            } catch (error: Throwable) {
                state.postValue(Failure(error))
            }

        }
    }

    fun getDetails() {
        launch {
            withContext(IO) {
                val balancePair = async { cryptopiaRepositoty.getBalancePair(currencyPair) }
                val ticker = async { cryptopiaRepositoty.getCurrencyDetails(currencyPair) }
                try {

                    state.postValue(
                        Success(
                            TradePairDetails(
                                ticker.await().last.toPlainString(),
                                balancePair.await().first.available.toFloat(),
                                balancePair.await().second.available.toFloat()
                            )
                        )
                    )
                } catch (error: Throwable) {
                    state.postValue(Failure(error))
                }
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