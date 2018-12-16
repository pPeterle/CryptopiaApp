package com.cryptopia.android.pPeterle.presentation.viewModel

import android.arch.lifecycle.*
import com.cryptopia.android.pPeterle.data.CryptopiaRepository
import com.cryptopia.android.pPeterle.presentation.*

class StartViewModel(private val cryptopiaRepository: CryptopiaRepository) : BaseViewModel<Unit>(), LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun verifyKeys() {
        val apiKey = cryptopiaRepository.apiKey
        val secretKey = cryptopiaRepository.secretKey
        if (apiKey == null || apiKey.isEmpty() || secretKey == null || secretKey.isEmpty()) {
            state.value = Failure(Error())
        } else {
            state.value = Success(Unit)
        }
    }

}