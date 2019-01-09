package com.cryptopia.android.pPeterle.presentation.viewModel

import android.util.Log
import com.cryptopia.android.pPeterle.data.CryptopiaRepository
import com.cryptopia.android.pPeterle.data.model.Balance
import com.cryptopia.android.pPeterle.presentation.BaseViewModel
import com.cryptopia.android.pPeterle.presentation.Success
import com.cryptopia.android.pPeterle.presentation.mapper.BalanceMapper
import com.cryptopia.android.pPeterle.presentation.model.BalanceBinding
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class BalanceViewModel(private val repository: CryptopiaRepository, private val mapper: BalanceMapper) :
    BaseViewModel<List<BalanceBinding>>() {

    fun getAllBalances() {
        tryCatch {
            launch(IO) {

                val balances = repository.getAllBalances()
                repository.insertBalancesLocal(balances)

                val filteredList = balances.filter { it.total > 0 }
                getEquivalentBTC(filteredList)

                val orderedList = filteredList.sortedByDescending { it.btcValue }

                val balanceBindingList = orderedList.map { mapper.fromModel(it) }
                state.postValue(Success(balanceBindingList))

            }
        }
    }

    private suspend fun getEquivalentBTC(list: List<Balance>) {

        val btcMarket = repository.getCurrencies("BTC")
        val usdMarket = repository.getCurrencies("USDT")

        for (balance in list) {
            when (balance.currencyId) {
                1.0 -> balance.btcValue = balance.total
                469.0 -> {
                    val usdTradePair = usdMarket.first { it.label.contains(balance.symbol) }
                    balance.btcValue = balance.total * usdTradePair.lastPrice
                }
                else -> {
                    val market = btcMarket.first { it.label.contains(balance.symbol) }
                    balance.btcValue = balance.total * market.lastPrice
                }

            }
        }
    }


}
