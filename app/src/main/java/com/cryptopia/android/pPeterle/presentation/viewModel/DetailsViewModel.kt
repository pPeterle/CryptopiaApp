package com.cryptopia.android.pPeterle.presentation.viewModel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.cryptopia.android.pPeterle.data.CryptopiaRepository
import com.cryptopia.android.pPeterle.presentation.model.TradePairDetailsBinding
import com.cryptopia.android.pPeterle.data.remote.exceptions.CryptopiaException
import com.cryptopia.android.pPeterle.presentation.*
import com.cryptopia.android.pPeterle.presentation.mapper.TradePairMapper
import com.cryptopia.android.pPeterle.utils.toFormattedString
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class DetailsViewModel(private val cryptopiaRepository: CryptopiaRepository, private val tradePairMapper: TradePairMapper) : BaseViewModel<TradePairDetailsBinding>() {


    lateinit var tradePair: String

    fun setBuyOrder(originalAmount: String, price: String): LiveData<ViewState<Unit>> {
        val mutableLiveData = MutableLiveData<ViewState<Unit>>()

        mutableLiveData.postValue(Loading())

        launch(IO) {
            if (originalAmount.isEmpty()) {
                mutableLiveData.postValue(Failure(Error("Amount is empty")))
                return@launch
            }
            if (price.isEmpty()) {
                mutableLiveData.postValue(Failure(Error("Price is empty")))
                return@launch
            }

            try {

                cryptopiaRepository.submitTrade(
                    tradePair,
                    "Buy",
                    price,
                    originalAmount
                )

                getDetails()
                mutableLiveData.postValue(Success(Unit))

            } catch (error: CryptopiaException) {
                mutableLiveData.postValue(Failure(error))
            } catch (error: Throwable) {
                mutableLiveData.postValue(Failure(error))
            }
        }

        return mutableLiveData
    }

    fun setSellOrder(orginalAmount: String, price: String): LiveData<ViewState<Unit>> {
        val mutableLiveData = MutableLiveData<ViewState<Unit>>()

        launch(IO) {

            mutableLiveData.postValue(Loading())

            if (orginalAmount.isEmpty()) {
                mutableLiveData.postValue(Failure(Error("Amount is empty")))
                return@launch
            }
            if (price.isEmpty()) {
                mutableLiveData.postValue(Failure(Error("Price is empty")))
                return@launch
            }

            try {
                cryptopiaRepository.submitTrade(
                    tradePair,
                    "Sell",
                    price,
                    orginalAmount
                )


                getDetails()
                mutableLiveData.postValue(Success(Unit))

            } catch (error: CryptopiaException) {
                mutableLiveData.postValue(Failure(error))
            } catch (error: Throwable) {
                mutableLiveData.postValue(Failure(error))
            }
        }

        return mutableLiveData

    }

    fun getDetails() {
        tryCatch {
            launch(IO) {
                val (symbol, baseSymbol) = tradePair.split("/")


                val balance = async { cryptopiaRepository.getBalanceAvaible(symbol) }
                val balanceBase = async { cryptopiaRepository.getBalanceAvaible(baseSymbol) }
                val tradePair =  async { cryptopiaRepository.getMarket(tradePair) }
                val marketOrders = async { cryptopiaRepository.getMarketOrders(symbol + "_" + baseSymbol) }

                state.postValue(
                    Success(
                        TradePairDetailsBinding(
                            tradePairMapper.fromModel(tradePair.await()),
                            balance.await().toFormattedString(),
                            balanceBase.await().toFormattedString(),
                            marketOrders.await()
                        )
                    )
                )

            }

        }
    }

    fun calculateTotal(price: String, amount: String): String {
        if (!amount.isEmpty() && !price.isEmpty()) {
            if (amount.toFloat() > 0 && price.toFloat() > 0)
                return (price.toDouble() * amount.toDouble()).toFormattedString()
        }

        return ""
    }

    fun calculateAmount(price: String, total: String): String {
        if (!total.isEmpty() && !price.isEmpty()) {
            if (total.toFloat() > 0 && price.toFloat() > 0)
                return (total.toDouble() / price.toDouble()).toFormattedString()
        }

        return ""
    }

}
