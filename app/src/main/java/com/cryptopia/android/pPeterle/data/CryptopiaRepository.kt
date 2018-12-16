package com.cryptopia.android.pPeterle.data

import com.cryptopia.android.pPeterle.data.local.AppDatabase
import com.cryptopia.android.pPeterle.data.local.AppPreferences
import com.cryptopia.android.pPeterle.data.model.Balance
import com.cryptopia.android.pPeterle.data.model.OpenOrder
import com.cryptopia.android.pPeterle.data.remote.DeferredApi
import com.cryptopia.android.pPeterle.data.remote.DeferredApiList
import com.cryptopia.android.pPeterle.data.remote.RemoteRepository
import com.cryptopia.android.pPeterle.data.remote.exceptions.CryptopiaException
import com.cryptopia.android.pPeterle.data.remote.exceptions.NoConnectivityException
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class CryptopiaRepository(private val sharedPreferences: AppPreferences, appDatabase: AppDatabase, private val remoteRepository: RemoteRepository) {

    var apiKey: String?
        get() = sharedPreferences.apiKey
        set(value) {
            remoteRepository.apiKey = value
            sharedPreferences.apiKey = value
        }

    var secretKey: String?
        get() = sharedPreferences.secretKey
        set(value) {
            remoteRepository.secretKey = value
            sharedPreferences.secretKey = value
        }

    private val balancesDao = appDatabase.balanceDao()

    private val openOrdersDao = appDatabase.openOrdersDao()


    /*
    @Return details of market
     */
    suspend fun getMarket(label: String) = getApiReturnData {
        remoteRepository.getMarket(label)
    }

    suspend fun getMarketHistory(label: String, hours: Int = 48) = getApiReturnData {
        remoteRepository.getMarketHistory(label, hours)
    }

    suspend fun getMarketOrders(label: String) = getApiReturnData {
        remoteRepository.getMarketOrders(label)
    }

    /*
    @Return tradePair of btc market
     */
    suspend fun getBtcMarket() = getApiReturnData {
        remoteRepository.getBtcMarket()
    }

    /*
    @Return OPenOrders
     */
    suspend fun getOpenOrders() = coroutineScope {

        val orders = remoteRepository.getOpenOrders().await()
        if (orders.success) {
            openOrdersDao.insertOrders(orders.data)
        } else {
            throw CryptopiaException(orders.error)
        }
        orders.data

    }

    fun getOpenOrdersLocal() = openOrdersDao.getAllOrders()

    suspend fun insertOrdersLocal(orders: List<OpenOrder>) {
        coroutineScope {
            launch {
                openOrdersDao.removeAllOrders()
                openOrdersDao.insertOrders(orders)
            }
        }
    }


    /*
     Test the secret and api keys
     */
    //TODO("melhorar o testkeys")
    fun testKeys(apiKey: String, secretKey: String): DeferredApiList<Balance> {
        return remoteRepository.testKeys(apiKey, secretKey)
    }

    /*
      Remove Open Order
     */
    suspend fun removeOrder(id: Double) = getApiReturnData {
        remoteRepository.removeOrder(id)
    }

    /*
     @Returns the balance
     */
    suspend fun getBalanceAvaible(label: String) = coroutineScope {

        val apiReturnBalance = remoteRepository.getBalanceByLabel(label).await()
        if (apiReturnBalance.success) {
            return@coroutineScope apiReturnBalance.data[0].available
        } else {
            return@coroutineScope 0.0
        }
    }


    suspend fun getAllBalances() = getApiReturnData {
        remoteRepository.getAllBalances()
    }

    fun getBalancesLocal() = balancesDao.getAllBalances()

    fun insertBalancesLocal(balances: List<Balance>) {
        balancesDao.removeAllBalances()
        balancesDao.insertBalance(balances)
    }

    /*
       submit a trade
     */
    suspend fun submitTrade(label: String, type: String, price: String, amount: String) = getApiReturnData {
        remoteRepository.submitTrade(label, type, price, amount)
    }

    @Throws(CryptopiaException::class, NoConnectivityException::class)
    private suspend fun <T> getApiReturnData(block: () -> DeferredApi<T>) = coroutineScope {
        val apiReturn = block().await()
        if (apiReturn.success) {
            return@coroutineScope apiReturn.data
        } else {
            throw CryptopiaException(apiReturn.error)
        }
    }

}


