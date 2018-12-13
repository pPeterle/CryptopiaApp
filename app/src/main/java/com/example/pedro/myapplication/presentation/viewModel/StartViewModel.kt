package com.example.pedro.myapplication.presentation.viewModel

import android.arch.lifecycle.*
import android.util.Log
import com.example.pedro.myapplication.data.CryptopiaRepository
import com.example.pedro.myapplication.presentation.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import retrofit2.HttpException

class StartViewModel(private val cryptopiaRepository: CryptopiaRepository) : ScopedViewModel<Unit>(), LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun verifyKeys() {
        val apiKey = cryptopiaRepository.apiKey
        val secretKey = cryptopiaRepository.secretKey
        if (apiKey == null || apiKey.isEmpty() || secretKey == null || secretKey.isEmpty()) {

        } else {
            state.value = Success(Unit)
        }
    }

    fun setKeys(apiKey: String, secretKey: String) {
        if (!apiKey.isEmpty() && !secretKey.isEmpty()) {
            testKeys(apiKey, secretKey)
        } else {
            state.value =
                    Failure(Error("ApiReturn Key or SecretKey EMPTY"))
        }
    }

    private fun testKeys(apiKey: String, secretKey: String) {
        launch(IO) {
            state.postValue(Loading())
            try {
                val success = cryptopiaRepository.testKeys(apiKey, secretKey).await()

                if (success.success) {
                    state.postValue(Success(Unit))
                    cryptopiaRepository.apiKey = apiKey
                    cryptopiaRepository.secretKey = secretKey
                } else {

                    state.postValue(Failure(Error("Chaves Erradas")))
                }
            } /*catch (e: HttpException) {
                state.postValue(Failure(Error("Chaves Erradas")))
            }*/ catch (e: Throwable) {
                state.postValue(Failure(e))
            }

        }
    }

}