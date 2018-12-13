package com.example.pedro.myapplication.presentation.viewModel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.example.pedro.myapplication.data.CryptopiaRepository
import com.example.pedro.myapplication.data.model.TradePair
import com.example.pedro.myapplication.presentation.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO

class HomeViewModel(private val cryptopiaRepository: CryptopiaRepository) : ScopedViewModel<List<TradePair>>() {

    private val tradePairList = mutableListOf<TradePair>()

    fun getMarkets() {
        tryCatch {
            launch(IO) {
                state.postValue(Loading())

                val apiReturn = cryptopiaRepository.getBtcMarket()

                tradePairList.clear()
                tradePairList.addAll(apiReturn)
                state.postValue(Success(apiReturn))

            }
        }
    }

    fun filterListString(s: String) {
        val newList = tradePairList.filter { it.label.toUpperCase().contains(s.toUpperCase()) }
        state.postValue(Success(newList))
    }

}
