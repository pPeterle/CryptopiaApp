package com.example.pedro.myapplication.home

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.example.pedro.myapplication.*
import com.example.pedro.myapplication.data.CryptopiaRepositoty
import com.example.pedro.myapplication.data.model.ApiReturn
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO

class HomeViewModel(private val cryptopiaRepositoty: CryptopiaRepositoty) : ScopedViewModel() {

    private val state = MutableLiveData<ViewState<List<ApiReturn>>>()

    fun getMarkets() {
        launch(IO) {
            state.postValue(Loading())

            try {
                val apiReturn = cryptopiaRepositoty.getBtcMarket().await()
                state.postValue(Success(apiReturn.data))
            } catch (e: Exception) {
                state.postValue(Failure(e))
            }
        }
    }

    fun getState() = state as LiveData<ViewState<List<ApiReturn>>>

}
