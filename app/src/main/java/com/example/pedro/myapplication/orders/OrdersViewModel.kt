package com.example.pedro.myapplication.orders

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.example.pedro.myapplication.*
import com.example.pedro.myapplication.data.CryptopiaRepositoty
import com.example.pedro.myapplication.data.model.OpenOrder
import com.example.pedro.myapplication.data.remote.exceptions.CryptopiaException
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.lang.RuntimeException

class OrdersViewModel(private val cryptopiaRepositoty: CryptopiaRepositoty) : ScopedViewModel() {

    private val state = MutableLiveData<ViewStateList<OpenOrder>>()

    fun getOpenOrders() {
        launch(IO) {
            state.postValue(Loading())

            try {
                val orders = cryptopiaRepositoty.getOpenOrders().await()
                with(orders) {
                    if (success){
                        state.postValue(Success(data))
                        cryptopiaRepositoty.saveOrders(data)
                    }
                    else
                        throw CryptopiaException(error)
                }

            } catch (e: CryptopiaException) {
                state.postValue(Failure(e))
            } catch (e: Throwable) {
                state.postValue(Failure(e))
            }

        }
    }

    fun removeOrder(id: Double) {
        launch(IO) {
            try {
                val r = cryptopiaRepositoty.removeOrder(id).await()
                Log.i("test", "o resultao e ${r.success} e o error: ${r.error}")
                if (r.success) {
                    getOpenOrders()
                } else {
                    state.postValue(Failure(Error(r.error)))
                }

            } catch (e: RuntimeException) {
            } catch (e: Throwable) {
                state.postValue(Failure(e))
            }
        }

    }

    fun getState() = state as LiveData<ViewStateList<OpenOrder>>
}
