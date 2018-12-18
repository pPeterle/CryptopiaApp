package com.cryptopia.android.pPeterle.presentation.viewModel

import com.cryptopia.android.pPeterle.data.CryptopiaRepository
import com.cryptopia.android.pPeterle.data.model.Balance
import com.cryptopia.android.pPeterle.presentation.BaseViewModel
import com.cryptopia.android.pPeterle.presentation.Success
import com.cryptopia.android.pPeterle.presentation.mapper.BalanceMapper
import com.cryptopia.android.pPeterle.presentation.mapper.Mapper
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

    //TODO tornar o metodo generico nao somente para BTC
    private suspend fun getEquivalentBTC(list: List<Balance>) {

        val tradePair = repository.getCurrencies("BTC")

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
