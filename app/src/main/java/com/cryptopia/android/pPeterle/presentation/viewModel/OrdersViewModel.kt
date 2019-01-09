package com.cryptopia.android.pPeterle.presentation.viewModel

import com.cryptopia.android.pPeterle.data.CryptopiaRepository
import com.cryptopia.android.pPeterle.data.model.OpenOrder
import com.cryptopia.android.pPeterle.presentation.*
import com.cryptopia.android.pPeterle.presentation.mapper.OpenOrdersMapper
import com.cryptopia.android.pPeterle.presentation.model.OpenOrderBinding
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class OrdersViewModel(private val cryptopiaRepository: CryptopiaRepository, private val mapper: OpenOrdersMapper) :
    BaseViewModel<List<OpenOrderBinding>>() {

    fun getOpenOrders() {
        tryCatch {
            launch(IO) {
                state.postValue(Loading())

                val orders = cryptopiaRepository.getOpenOrders()
                val ordersBinding = orders.map { mapper.fromModel(it) }
                state.postValue(Success(ordersBinding))


            }
        }
    }

    fun getHistoryOrders() {

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

