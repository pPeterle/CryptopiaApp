package com.cryptopia.android.pPeterle.presentation.viewModel

import com.cryptopia.android.pPeterle.data.CryptopiaRepository
import com.cryptopia.android.pPeterle.domain.MarketHistoryMapper
import com.cryptopia.android.pPeterle.presentation.*
import com.github.mikephil.charting.data.CandleEntry
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class CandleStickViewModel(private val repository: CryptopiaRepository) : BaseViewModel<List<CandleEntry>>() {


    lateinit var label: String

    fun getMarketHistory() {
        launch(IO) {
            try {
                val marketHistory = repository.getMarketHistory(label)


                val candleList = MarketHistoryMapper.map(marketHistory.toMutableList())
                state.postValue(Success(candleList))

            } catch (e: Throwable) {
                state.postValue(Failure(e))
            }
        }
    }


}