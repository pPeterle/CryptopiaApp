package com.cryptopia.android.pPeterle.presentation.viewModel

import com.cryptopia.android.pPeterle.data.CryptopiaRepository
import com.cryptopia.android.pPeterle.presentation.Failure
import com.cryptopia.android.pPeterle.presentation.Loading
import com.cryptopia.android.pPeterle.presentation.BaseViewModel
import com.cryptopia.android.pPeterle.presentation.Success
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class KeysViewModel(private val repository: CryptopiaRepository): BaseViewModel<Unit>() {

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
                val success = repository.testKeys(apiKey, secretKey).await()

                if (success.success) {
                    state.postValue(Success(Unit))
                    repository.apiKey = apiKey
                    repository.secretKey = secretKey
                } else {
                    state.postValue(Failure(Error("Chaves Erradas")))
                }
            }
            catch (e: Throwable) {
                state.postValue(Failure(e))
            }

        }
    }
}