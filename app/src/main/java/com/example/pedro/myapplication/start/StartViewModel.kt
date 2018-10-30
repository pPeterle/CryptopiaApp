package com.example.pedro.myapplication.start

import android.arch.lifecycle.*
import android.util.Log
import com.example.pedro.myapplication.*
import com.example.pedro.myapplication.data.CryptopiaRepositoty
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class StartViewModel(private val cryptopiaRepositoty: CryptopiaRepositoty): ScopedViewModel(), LifecycleObserver {

    private val state = MutableLiveData<ViewState<Unit>>()

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun verifyKeys() {
        val apiKey = cryptopiaRepositoty.mApiKey
        val secretKey = cryptopiaRepositoty.mSecretKey
        if (apiKey == null || apiKey.isEmpty() || secretKey == null || secretKey.isEmpty()) {
            state.value = Failure(Error("YOU NEED TO SET THE KEYS"))
        } else {
            state.value = Success(Unit)
        }
    }

    fun setKeys(apiKey: String, secretKey: String) {
        if (!apiKey.isEmpty() && !secretKey.isEmpty()) {
            Log.i("test", "api key: $apiKey")
            Log.i("test", "secretKey: $secretKey")
            cryptopiaRepositoty.mApiKey = apiKey
            cryptopiaRepositoty.mSecretKey = secretKey
            testKeys()
        } else {
            state.value = Failure(Error("Api Key or SecretKey EMPTY"))
        }
    }

    private fun testKeys() {
        launch(IO) {
            try {
                state.postValue(Loading())
                cryptopiaRepositoty.testKeys()
                state.postValue(Success(Unit))
            } catch (error: Throwable) {
                state.postValue(Failure(Error("Keys dont exist")))
            }
        }
    }

    fun getState() = state as LiveData<ViewState<Unit>>
}