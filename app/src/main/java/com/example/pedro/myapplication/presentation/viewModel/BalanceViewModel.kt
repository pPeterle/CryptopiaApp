package com.example.pedro.myapplication.presentation.viewModel

import com.example.pedro.myapplication.data.CryptopiaRepository
import com.example.pedro.myapplication.data.model.Balance
import com.example.pedro.myapplication.presentation.ScopedViewModel
import com.example.pedro.myapplication.presentation.Success
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class BalanceViewModel(private val repository: CryptopiaRepository) : ScopedViewModel<List<Balance>>() {

    fun getAllBalances() {
        tryCatch {
            launch(IO) {

                val balances = repository.getAllBalances()

                val list = balances
                repository.insertBalancesLocal(list)
                val filteredList = list.filter { it.total > 0 }
                getEquivalentBTC(filteredList)

                val orderedList = filteredList.sortedByDescending { it.btcValue }
                state.postValue(Success(orderedList))

            }
        }
    }

    private suspend fun getEquivalentBTC(list: List<Balance>) {
        val tradePair = repository.getBtcMarket()

        list.forEach { balance ->
            if (balance.currencyId != 469.0 && balance.currencyId != 1.0) {
                val market = tradePair.filter { it.label.contains(balance.symbol) }
                balance.btcValue = balance.total * market.get(0).lastPrice
            }

            if (balance.currencyId == 1.0) {
                balance.btcValue = balance.total
            }
        }

    }


}
