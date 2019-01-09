package com.cryptopia.android.pPeterle.presentation.viewModel

import com.cryptopia.android.pPeterle.data.CryptopiaRepository
import com.cryptopia.android.pPeterle.presentation.BaseViewModel
import com.cryptopia.android.pPeterle.presentation.Loading
import com.cryptopia.android.pPeterle.presentation.Success
import com.cryptopia.android.pPeterle.presentation.ViewState
import com.cryptopia.android.pPeterle.presentation.mapper.TradeHistoryMapper
import com.cryptopia.android.pPeterle.presentation.model.TradeHistoryBinding
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class TradeHistoryViewModel(private val repository: CryptopiaRepository, private val mapper: TradeHistoryMapper) :
    BaseViewModel<List<TradeHistoryBinding>>() {

    var label: String? = null

    fun getTradeHistory() {
        tryCatch {
            launch(IO) {
                state.postValue(Loading())
                val tradeHistory = repository.getTradeHistory(label)
                val tradeHistoryBinding = tradeHistory.map { mapper.fromModel(it) }
                state.postValue(Success(tradeHistoryBinding))
            }
        }
    }
}