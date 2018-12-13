package com.example.pedro.myapplication.presentation.viewModel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.example.pedro.myapplication.data.CryptopiaRepository
import com.example.pedro.myapplication.data.model.OpenOrder
import com.example.pedro.myapplication.data.remote.exceptions.CryptopiaException
import com.example.pedro.myapplication.presentation.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.lang.RuntimeException

class OrdersViewModel(private val cryptopiaRepository: CryptopiaRepository) : ScopedViewModel<List<OpenOrder>>() {

    fun getOpenOrders() {
        tryCatch {
            launch(IO) {
                state.postValue(Loading())

                val orders = cryptopiaRepository.getOpenOrders()

                state.postValue(Success(orders))


            }
        }
    }

    fun removeOrder(id: Double) {
        tryCatch {
            launch(IO) {
                cryptopiaRepository.removeOrder(id)
                getOpenOrders()

            }
        }
    }

}

