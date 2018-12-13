package com.example.pedro.myapplication.presentation.viewModel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.example.pedro.myapplication.data.CryptopiaRepository
import com.example.pedro.myapplication.data.remote.exceptions.CryptopiaException
import com.example.pedro.myapplication.domain.MarketHistoryMapper
import com.example.pedro.myapplication.domain.MarketOrdersMapper
import com.example.pedro.myapplication.presentation.Failure
import com.example.pedro.myapplication.presentation.ScopedViewModel
import com.example.pedro.myapplication.presentation.Success
import com.example.pedro.myapplication.presentation.ViewStateList
import com.github.mikephil.charting.data.CandleEntry
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DepthViewModel(private val repository: CryptopiaRepository) : ScopedViewModel<List<ILineDataSet>>() {


    lateinit var label: String

    fun getMarketOrders() {
        tryCatch {
            launch(Dispatchers.IO) {
                val (first, second) = label.split("/")


                val marketHistory = repository.getMarketOrders("${first}_$second")


                val candleList = MarketOrdersMapper.map(marketHistory)
                state.postValue(Success(candleList))


            }
        }
    }

}