package com.example.pedro.myapplication.presentation.viewModel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.example.pedro.myapplication.data.CryptopiaRepository
import com.example.pedro.myapplication.data.remote.exceptions.CryptopiaException
import com.example.pedro.myapplication.domain.MarketHistoryMapper
import com.example.pedro.myapplication.domain.MarketOrdersMapper
import com.example.pedro.myapplication.presentation.*
import com.github.mikephil.charting.data.CandleEntry
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class CandleStickViewModel(private val repository: CryptopiaRepository) : ScopedViewModel<List<CandleEntry>>() {


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