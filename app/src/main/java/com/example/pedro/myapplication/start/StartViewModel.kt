package com.example.pedro.myapplication.start

import android.arch.lifecycle.*
import android.util.Log
import com.example.pedro.myapplication.*
import com.example.pedro.myapplication.data.CryptopiaRepositoty
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import retrofit2.HttpException

class StartViewModel(private val cryptopiaRepositoty: CryptopiaRepositoty) : ScopedViewModel(), LifecycleObserver {

    private val state = MutableLiveData<ViewState<Unit>>()

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun verifyKeys() {
        val apiKey = cryptopiaRepositoty.apiKey
        val secretKey = cryptopiaRepositoty.secretKey
        if (apiKey == null || apiKey.isEmpty() || secretKey == null || secretKey.isEmpty()) {
            state.value = Failure(Error("YOU NEED TO SET THE KEYS"))
        } else {
            state.value = Success(Unit)
        }
    }

    fun setKeys(apiKey: String, secretKey: String) {
        if (!apiKey.isEmpty() && !secretKey.isEmpty()) {
            testKeys(apiKey, secretKey)
        } else {
            state.value = Failure(Error("ApiReturn Key or SecretKey EMPTY"))
        }
    }

    private fun testKeys(apiKey: String, secretKey: String) {
        launch(IO) {
            state.postValue(Loading())
            try {
                val success = cryptopiaRepositoty.testKeys(apiKey, secretKey).await()
                Log.i("test", "erro: ${success}")

                if (success.success) {
                    Log.i("test", "erro: ${success.data}")
                    state.postValue(Success(Unit))
                    cryptopiaRepositoty.apiKey = apiKey
                    cryptopiaRepositoty.secretKey = secretKey
                } else {

                    state.postValue(Failure(Error("Chaves Erradas")))
                }
            } catch (e: HttpException) {
                state.postValue(Failure(Error("Chaves Erradas")))
            } catch (e: Throwable) {
                state.postValue(Failure(Error("Algum error inesperado: $e")))
            }

        }
    }

    fun getState() = state as LiveData<ViewState<Unit>>
}