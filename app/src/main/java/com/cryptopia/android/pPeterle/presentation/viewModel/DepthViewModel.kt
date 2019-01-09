package com.cryptopia.android.pPeterle.presentation.viewModel

import com.cryptopia.android.pPeterle.data.CryptopiaRepository
import com.cryptopia.android.pPeterle.domain.MarketOrdersMapper
import com.cryptopia.android.pPeterle.presentation.BaseViewModel
import com.cryptopia.android.pPeterle.presentation.Success
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DepthViewModel(private val repository: CryptopiaRepository) : BaseViewModel<List<ILineDataSet>>() {


    lateinit var label: String

    fun getMarketOrders() {
        tryCatch {
            launch(Dispatchers.IO) {
                val (first, second) = label.split("/")


                val marketHistory = repository.getMarketOrders("${first}_$second")


                val candleList = MarketOrdersMapper.map(marketHistory)
                state.postValue(Success(candleList))


            }
        }
    }

}