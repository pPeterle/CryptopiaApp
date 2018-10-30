package com.example.pedro.myapplication

import android.app.Application
import com.example.pedro.myapplication.di.myModule
import org.koin.android.ext.android.startKoin

class CryptopiaApp: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin(this, listOf(myModule))
    }
}