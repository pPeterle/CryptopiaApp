package com.example.pedro.myapplication.data

import android.content.SharedPreferences
import com.example.pedro.myapplication.data.local.AppPreferences
import com.example.pedro.myapplication.data.model.Api
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import org.knowm.xchange.Exchange
import org.knowm.xchange.ExchangeFactory
import org.knowm.xchange.cryptopia.CryptopiaExchange
import org.knowm.xchange.currency.CurrencyPair
import org.knowm.xchange.dto.meta.ExchangeMetaData
import org.knowm.xchange.dto.trade.LimitOrder
import org.knowm.xchange.service.account.AccountService
import org.knowm.xchange.service.marketdata.MarketDataService
import org.knowm.xchange.service.trade.TradeService

class CryptopiaRepositoty(private val sharedPreferences: AppPreferences) {

    var mApiKey: String?
    get() = sharedPreferences.apiKey
    set(value) { sharedPreferences.apiKey = value }

    var mSecretKey: String?
        get() = sharedPreferences.secretKey
        set(value) { sharedPreferences.secretKey = value }

    private val exchange by lazy {
        GlobalScope.async {

            val exSpec = CryptopiaExchange().defaultExchangeSpecification.apply {
                apiKey = mApiKey
                secretKey = mSecretKey
            }
            ExchangeFactory.INSTANCE.createExchange(exSpec)

        }
    }

    private val cryptopiaService = CryptopiaService.getInstance()

    suspend fun testKeys() {
        exchange.await()
    }

    fun getBtcMarket(): Deferred<Api> {
        return cryptopiaService.getBtcMarkets()
    }

    suspend fun removeOrder(id: String) = exchange.tradeService {
        cancelOrder(id)
    }

    suspend fun getOpenOrders() = exchange.tradeService {
        openOrders
    }

    suspend fun getCurrencyDetails(currencyPair: CurrencyPair) = exchange.marketData {
        getTicker(currencyPair)
    }

    suspend fun getBalancePair(currencyPair: CurrencyPair) = exchange.accountService {
        val baseBalance = accountInfo.wallet.getBalance(currencyPair.counter)
        val counterBalance = accountInfo.wallet.getBalance(currencyPair.base)
        Pair(baseBalance, counterBalance)
    }

    suspend fun placeLimitOrder(limitOrder: LimitOrder) = exchange.tradeService {
        placeLimitOrder(limitOrder)
    }

    private suspend fun <R> Deferred<Exchange>.tradeService(block: TradeService.() -> R) =
        await().tradeService.block()

    private suspend fun <R> Deferred<Exchange>.exchangeData(block: ExchangeMetaData.() -> R) =
        await().exchangeMetaData.block()

    private suspend fun <R> Deferred<Exchange>.marketData(block: MarketDataService.() -> R) =
        await().marketDataService.block()

    private suspend fun <R> Deferred<Exchange>.accountService(block: AccountService.() -> R) =
        await().accountService.block()

}

