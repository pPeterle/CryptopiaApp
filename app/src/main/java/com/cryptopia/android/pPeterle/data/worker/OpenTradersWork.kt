package com.cryptopia.android.pPeterle.data.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.support.v4.app.TaskStackBuilder
import android.util.Log
import com.cryptopia.android.pPeterle.ui.activity.MainActivity
import com.cryptopia.android.pPeterle.R
import com.cryptopia.android.pPeterle.data.CryptopiaRepository
import com.cryptopia.android.pPeterle.utils.toFormattedString
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import kotlin.random.Random


class OpenTradersWork : BroadcastReceiver(), KoinComponent {

    private val cryptopiaRepository: CryptopiaRepository by inject()

    override fun onReceive(context: Context?, intent: Intent?) {
        GlobalScope.launch(IO) {
            Log.i("test", "onReceive: comecando")
            try {
                val balancesApi = cryptopiaRepository.getAllBalances()
                val ordersAPi =  cryptopiaRepository.getOpenOrders()
                val balancesLocal = cryptopiaRepository.getBalancesLocal()
                val ordersLocal = cryptopiaRepository.getOpenOrdersLocal()

                val marketSet = ordersLocal.map { it.market to it }.toMap()

                marketSet.forEach {
                    val balanceRemote = balancesApi.first { remote -> it.key.split("/").first() == remote.symbol }
                    val balance = balancesLocal.first { local -> it.key.split("/").first() == local.symbol }
                    when {
                        balanceRemote.total > balance.total -> {
                            buildNotification(
                                context!!,
                                "You Bought ${it.key} : ${(it.value.amount).toFormattedString()}"
                            )
                        }
                        balanceRemote.total < balance.total -> {
                            buildNotification(
                                context!!,
                                "You Sold ${it.key} : ${(it.value.amount).toFormattedString()}"
                            )
                        }
                        else -> Log.i(
                            "test",
                            "onReceive ${it.key}: remote (${balanceRemote.total.toFormattedString()}) == local (${balance.total.toFormattedString()}) "
                        )
                    }
                }

                cryptopiaRepository.insertBalancesLocal(balancesApi)
                cryptopiaRepository.insertOrdersLocal(ordersAPi)
            } catch (e: Exception) {
                Log.i("test", "onReceive: error: ${e.message}")
                e.printStackTrace()
            }
        }
    }

    private fun buildNotification(context: Context, text: String) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val CHANNEL_ID = "my_channel_01"
            val name = "my_channel"
            val Description = "This is my channel"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val mChannel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = Description
                enableLights(true)
                lightColor = Color.RED
                setShowBadge(false)
            }
            notificationManager.createNotificationChannel(mChannel)
        }

        val builder = NotificationCompat.Builder(context, "my_channel_01")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("ORDEM ATIVADA")
            .setContentText(text)

        val resultIntent = Intent(context, MainActivity::class.java)
        val stackBuilder = TaskStackBuilder.create(context)
        stackBuilder.addParentStack(MainActivity::class.java)
        stackBuilder.addNextIntent(resultIntent)
        val resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)

        builder.setContentIntent(resultPendingIntent)

        notificationManager.notify(Random.nextInt(), builder.build())

    }
}