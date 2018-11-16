package com.example.pedro.myapplication

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.pedro.myapplication.data.worker.OpenTradersWork
import com.example.pedro.myapplication.di.myModule
import org.koin.android.ext.android.startKoin

class CryptopiaApp : Application() {

    override fun onCreate() {
        super.onCreate()

        val alarmReceiver = Intent(this, OpenTradersWork::class.java)
        val pendinIntent = PendingIntent.getBroadcast(this, 0, alarmReceiver, 0)
        val manager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 900000, pendinIntent)

        startKoin(this, listOf(myModule))

    }
}