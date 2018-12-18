package com.cryptopia.android.pPeterle.data.remote

import android.util.Base64
import com.cryptopia.android.pPeterle.data.model.Balance
import com.cryptopia.android.pPeterle.data.model.MarketHistory
import com.cryptopia.android.pPeterle.data.model.OpenOrder
import com.cryptopia.android.pPeterle.data.model.TradePair
import com.cryptopia.android.pPeterle.data.remote.exceptions.CryptopiaException
import com.cryptopia.android.pPeterle.utils.ApiConstants
import com.google.gson.JsonObject
import okhttp3.MediaType
import okhttp3.RequestBody
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.security.InvalidKeyException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

class RemoteRepository(var apiKey: String?, var secretKey: String?, private val cryptopiaService: CryptopiaService) {

    fun getMarket(label: String): DeferredApi<TradePair> {
        val (symbol, baseSymbol) = label.split("/")
        return cryptopiaService.getMarket("${symbol}_${baseSymbol}")
    }

    fun getCurrencies(market: String) = cryptopiaService.getCurrencies(market)

    fun getOpenOrders(): DeferredApiList<OpenOrder> {


        val params = buildJsonObject {
            val currency: String? = null
            addProperty("Market", currency)
        }

        val requestBody = buildRequestBody(params)

        return cryptopiaService.getOpenOrders(
            getAuthString(
                CryptopiaService.OpenOrders,
                params
            ), requestBody
        )
    }

    fun testKeys(apiKey: String, secretKey: String): DeferredApiList<Balance> {
        val params = buildJsonObject {
            val currency: String? = null
            addProperty("Currency", currency)
        }
        val requestBody = buildRequestBody(params)

        return cryptopiaService.getBalance(
            getAuthString(
                CryptopiaService.Balance,
                params,
                apiKey,
                secretKey
            ), requestBody
        )
    }

    fun getMarketOrders(label: String) = cryptopiaService.getMarketOrders(label)


    fun removeOrder(id: Double): DeferredApiList<Double> {

        val params = buildJsonObject {
            addProperty("Type", "Trade")
            addProperty("OrderId", id)
        }

        val requestBody = buildRequestBody(params)
        return cryptopiaService.cancelTrade(
            getAuthString(
                CryptopiaService.CancelTrade,
                params
            ), requestBody
        )
    }

    fun getBalanceByLabel(label: String): DeferredApiList<Balance> {

        val params = buildJsonObject {
            addProperty("Currency", label)
        }

        val requestBody = buildRequestBody(params)
        return cryptopiaService.getBalance(getAuthString(CryptopiaService.Balance, params), requestBody)

    }

    fun getAllBalances(): DeferredApiList<Balance> {
        val params = buildJsonObject {
            val label: String? = null
            addProperty("Currency", label)
        }

        val requestBody = buildRequestBody(params)
        return cryptopiaService.getBalance(getAuthString(CryptopiaService.Balance, params), requestBody)
    }

    fun getMarketHistory(label: String, hours: Int): DeferredApiList<MarketHistory> {
        val (symbol, baseSymbol) = label.split("/")
        return cryptopiaService.getMarketHistory("${symbol}_${baseSymbol}", hours)
    }

    fun submitTrade(label: String, type: String, price: String, amount: String): DeferredApi<Unit> {
        val params = buildJsonObject {
            addProperty("Market", label)
            addProperty("Type", type)
            addProperty("Rate", price)
            addProperty("Amount", amount)
        }

        val requestBody = buildRequestBody(params)
        return cryptopiaService.submitTrade(getAuthString(CryptopiaService.SubmitTrade, params), requestBody)
    }

    private fun buildJsonObject(block: JsonObject.() -> Unit): JsonObject {
        val jsonObject = JsonObject()
        jsonObject.block()
        return jsonObject
    }


    private fun buildRequestBody(params: JsonObject) =
        RequestBody.create(MediaType.parse(ApiConstants.CONTENT_TYPE_APPLICATION_JSON), params.toString())

    private fun getAuthString(
        uri: String,
        postParam: JsonObject,
        apiKey: String? = this.apiKey,
        secretKey: String? = this.secretKey
    ): String {
        try {
            val nonce = System.currentTimeMillis().toString()

            val requestSignature = StringBuilder();
            requestSignature.append(apiKey)
                .append("POST")
                .append(
                    URLEncoder.encode(
                        ApiConstants.API_BASE_URL + uri,
                        StandardCharsets.UTF_8.toString()
                    ).toLowerCase()
                )
                .append(nonce)
                .append(getMd5B64String(postParam.toString()))

            val auth = StringBuilder()
            auth.append(ApiConstants.AUTHENTICATION_SCHEMA)
                .append(this.apiKey)
                .append(":")
                .append(getHmacSha256B64String(requestSignature.toString(), secretKey))
                .append(":")
                .append(nonce)

            return auth.toString()

        } catch (e: UnsupportedEncodingException) {
            throw  CryptopiaException(
                "Unsupport encoding exception.",
                e
            )
        } catch (e: NoSuchAlgorithmException) {
            throw  CryptopiaException(
                "no such algorithm exception.",
                e
            )
        } catch (e: InvalidKeyException) {
            throw  CryptopiaException(
                "invalid key exception.",
                e
            )
        }
    }

    @Throws(NoSuchAlgorithmException::class, UnsupportedEncodingException::class)
    private fun getMd5B64String(postParameter: String): String {

        val md5Digest = MessageDigest.getInstance(ApiConstants.HASH_ALGORITHM_MD5)
        val digestBytes = md5Digest.digest(postParameter.toByteArray(charset(ApiConstants.UTF_8)))
        return Base64.encodeToString(digestBytes, android.util.Base64.DEFAULT).trim { it <= ' ' }
    }

    @Throws(NoSuchAlgorithmException::class, InvalidKeyException::class, UnsupportedEncodingException::class)
    private fun getHmacSha256B64String(msg: String, secretKey: String?): String {

        val hmacSHA2561 = Mac.getInstance("HmacSHA256")
        val secretSpec1 = SecretKeySpec(
            Base64.decode(secretKey, Base64.DEFAULT), ApiConstants.SIGN_ALGORITHM_HMAC_SHA256
        )

        hmacSHA2561.init(secretSpec1)
        val android64 = android.util.Base64.encodeToString(
            hmacSHA2561.doFinal(msg.toByteArray(charset(ApiConstants.UTF_8))),
            Base64.DEFAULT
        )

        return android64.trim { it <= ' ' }
    }
}