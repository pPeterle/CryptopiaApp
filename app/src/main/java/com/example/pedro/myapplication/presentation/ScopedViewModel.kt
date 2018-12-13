package com.example.pedro.myapplication.presentation

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.pedro.myapplication.data.remote.exceptions.NoConnectivityException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

abstract class ScopedViewModel<T>: ViewModel(), CoroutineScope {

    protected val state = MutableLiveData<ViewState<T>>()

    val job = Job()

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main


    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    fun getState() = state as LiveData<ViewState<T>>

    fun tryCatch(block: () -> Unit) {
        try {
            block()

        } catch (error: NoConnectivityException) {
            state.postValue(Failure(Error("SEM CONEXAO")))
        } catch (error: Throwable) {
            state.postValue(Failure(error))
        }
    }
}