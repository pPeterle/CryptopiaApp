package com.example.pedro.myapplication.balance

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.example.pedro.myapplication.Failure
import com.example.pedro.myapplication.ScopedViewModel
import com.example.pedro.myapplication.Success
import com.example.pedro.myapplication.ViewStateList
import com.example.pedro.myapplication.data.CryptopiaRepositoty
import com.example.pedro.myapplication.data.model.Balance
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class BalanceViewModel(private val repository: CryptopiaRepositoty) : ScopedViewModel() {

    private val state = MutableLiveData<ViewStateList<Balance>>()

    fun getState() = state as LiveData<ViewStateList<Balance>>

    fun getAllBalances() {
        launch(IO) {
            try {
                val balances = repository.getAllBalances().await()
                if (balances.success) {
                    val list = balances.data.sortedByDescending { it.total }
                    getEquivalentBTC(list).await()
                    state.postValue(Success(list))
                } else {
                    throw Error(balances.error)
                }
            } catch (error: Exception) {
                state.postValue(Failure(error))
            }
        }
    }

    private suspend fun getEquivalentBTC(list: List<Balance>) = async(IO) {
        val result = list.filter { it.available > 0 || it.heldForTrades > 0 }

        result.forEach {
            launch {
                if (it.currencyId != 469.0 && it.currencyId != 1.0) {
                    val market = repository.getMarket("${it.symbol}/BTC").await()
                    it.btcValue = it.total * market.data.lastPrice
                }

                if (it.currencyId == 1.0) {
                    it.btcValue = it.total
                }
            }
        }

    }
}
