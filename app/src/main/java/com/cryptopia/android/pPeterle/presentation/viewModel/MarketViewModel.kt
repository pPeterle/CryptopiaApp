package com.cryptopia.android.pPeterle.presentation.viewModel

import com.cryptopia.android.pPeterle.data.CryptopiaRepository
import com.cryptopia.android.pPeterle.presentation.BaseViewModel
import com.cryptopia.android.pPeterle.presentation.Loading
import com.cryptopia.android.pPeterle.presentation.Success
import com.cryptopia.android.pPeterle.presentation.mapper.TradePairMapper
import com.cryptopia.android.pPeterle.presentation.model.TradePairBinding
import com.cryptopia.android.pPeterle.ui.fragment.MarketFragment
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class MarketViewModel(private val cryptopiaRepository: CryptopiaRepository, private val mapper: TradePairMapper) :
    BaseViewModel<List<TradePairBinding>>() {

    enum class Sort { NAME, PRICE, VOLUME, CHANGE }

    private val tradePairList = mutableListOf<TradePairBinding>()

    lateinit var market: MarketFragment.Market

    fun getCurrencies() {
        tryCatch {
            launch(IO) {
                state.postValue(Loading())


                val list = cryptopiaRepository.getCurrencies(market.name)
                val tradePairBinding = list.map { mapper.fromModel(it) }

                tradePairList.clear()
                tradePairList.addAll(tradePairBinding)
                state.postValue(Success(tradePairBinding))

            }
        }
    }

    fun filterListString(s: String) {
        val newList = tradePairList.filter { it.label.toUpperCase().contains(s.toUpperCase()) }
        state.postValue(Success(newList))
    }

    fun orderList(filter: Sort, descending: Boolean) {
        if (descending) {
            when (filter) {
                Sort.NAME -> tradePairList.sortBy { it.label }
                Sort.CHANGE -> tradePairList.sortBy { it.change }
                Sort.PRICE -> tradePairList.sortBy { it.lastPrice }
                Sort.VOLUME -> tradePairList.sortBy { it.volume }
            }
        } else {
            when (filter) {
                Sort.NAME -> tradePairList.sortByDescending { it.label }
                Sort.CHANGE -> tradePairList.sortByDescending { it.change }
                Sort.PRICE -> tradePairList.sortByDescending { it.lastPrice }
                Sort.VOLUME -> tradePairList.sortByDescending { it.volume }
            }
        }

        state.value = Success(tradePairList)
    }
}
