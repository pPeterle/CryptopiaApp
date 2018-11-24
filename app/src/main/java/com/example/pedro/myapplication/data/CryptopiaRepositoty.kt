package com.example.pedro.myapplication.data

import com.example.pedro.myapplication.data.local.AppDatabase
import com.example.pedro.myapplication.data.local.AppPreferences
import com.example.pedro.myapplication.data.model.*
import com.example.pedro.myapplication.data.remote.DeferredApi
import com.example.pedro.myapplication.data.remote.DeferredApiList
import com.example.pedro.myapplication.data.remote.RemoteRepository

class CryptopiaRepositoty(private val sharedPreferences: AppPreferences, appDatabase: AppDatabase) {

    var apiKey: String?
        get() = sharedPreferences.apiKey
        set(value) {
            sharedPreferences.apiKey = value
        }

    var secretKey: String?
        get() = sharedPreferences.secretKey
        set(value) {
            sharedPreferences.secretKey = value
        }

    private val remoteRepository by lazy {
        RemoteRepository(apiKey, secretKey)
    }

    private val openOrdersDao = appDatabase.openOdersDao()


    /*
    @Return details of market
     */
    fun getMarket(label: String): DeferredApi<TradePair> {
        return remoteRepository.getMarket(label)
    }

    fun getMarketHistory(label: String, hours: Int = 24): DeferredApiList<MarketHistory> {
        return remoteRepository.getMarketHistory(label, hours)
    }

    /*
    @Return tradePair of btc market
     */
    fun getBtcMarket() = remoteRepository.getBtcMarket()

    /*
    @Return OPenOrders
     */
    fun getOpenOrders(): DeferredApiList<OpenOrder> {
        return remoteRepository.getOpenOrders()
    }

    /*
     Save the OpenOrders in SQL
     */
    fun saveOrders(orders: List<OpenOrder>) {
        openOrdersDao.removeAllOrders()
        openOrdersDao.insert(orders)
    }

    /*
     @Returns the OpenOrders in SQL
     */
    fun getOpenOrdersSql(): List<OpenOrder> {
        return openOrdersDao.getAllOpenOrders()
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
    fun removeOrder(id: Double): DeferredApiList<Unit> {
        return remoteRepository.removeOrder(id)
    }

    /*
     @Returns the balance
     */
    fun getBalance(label: String): DeferredApiList<Balance> {
        return remoteRepository.getBalanceByLabel(label)
    }

    fun getAllBalances(): DeferredApiList<Balance> {
        return remoteRepository.getAllBalances()
    }

    /*
       submit a trade
     */
    fun submitTrade(label: String, type: String, price: String, amount: String): DeferredApi<Unit> {
        return remoteRepository.submitTrade(label, type, price, amount)
    }
}


