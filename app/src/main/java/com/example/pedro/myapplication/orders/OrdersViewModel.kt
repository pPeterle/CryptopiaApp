package com.example.pedro.myapplication.orders

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.example.pedro.myapplication.*
import com.example.pedro.myapplication.data.CryptopiaRepositoty
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.knowm.xchange.dto.trade.OpenOrders

class OrdersViewModel(private val cryptopiaRepositoty: CryptopiaRepositoty) : ScopedViewModel() {

    private val state = MutableLiveData<ViewState<OpenOrders>>()

    fun getOpenOrders() {
        launch {
            state.postValue(Loading())
            val orders = async(IO) { cryptopiaRepositoty.getOpenOrders() }
            try {
                state.postValue(Success(orders.await()))
            } catch (e: Throwable) {
                state.postValue(Failure(e))
            }
        }
    }

    fun removeOrder(id: String) {
        launch(IO) {
            try {
                cryptopiaRepositoty.removeOrder(id)
                getOpenOrders()
            } catch (e: Throwable) {
                state.postValue(Failure(e))
            }
        }

    }

    fun getState() = state as LiveData<ViewState<OpenOrders>>
}
