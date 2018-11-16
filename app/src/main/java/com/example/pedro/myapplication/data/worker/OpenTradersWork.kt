package com.example.pedro.myapplication.data.worker

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
import com.example.pedro.myapplication.MainActivity
import com.example.pedro.myapplication.R
import com.example.pedro.myapplication.data.CryptopiaRepositoty
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject


class OpenTradersWork : BroadcastReceiver(), KoinComponent {

    private val cryptopiaRepositoty: CryptopiaRepositoty by inject()

    override fun onReceive(context: Context?, intent: Intent?) {
        runBlocking(IO) {
                try {
                    val apiReturn = cryptopiaRepositoty.getOpenOrders().await()
                    val ordersLocal = async { cryptopiaRepositoty.getOpenOrdersSql() }.await() as MutableList

                    if (apiReturn.success && apiReturn.data.size < ordersLocal.size) {
                        ordersLocal.removeAll(apiReturn.data)
                        ordersLocal.forEach {
                            buildNotofication(context!!, it.market)
                        }
                        cryptopiaRepositoty.saveOrders(apiReturn.data)
                    } else {
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

        }
    }

    private fun buildNotofication(context: Context, message: String) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val CHANNEL_ID = "my_channel_01"
            val name = "my_channel"
            val Description = "This is my channel"
            val importance = NotificationManager.IMPORTANCE_HIGH
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
            .setContentTitle("NOTIFICACAO")
            .setContentText(message)

        val resultIntent = Intent(context, MainActivity::class.java)
        val stackBuilder = TaskStackBuilder.create(context)
        stackBuilder.addParentStack(MainActivity::class.java)
        stackBuilder.addNextIntent(resultIntent)
        val resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)

        builder.setContentIntent(resultPendingIntent)

        notificationManager.notify(12, builder.build())

    }
}