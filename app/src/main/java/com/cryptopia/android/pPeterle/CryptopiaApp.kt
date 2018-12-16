package com.cryptopia.android.pPeterle

import android.app.Application
import com.cryptopia.android.pPeterle.di.myModule
import org.koin.android.ext.android.startKoin

class CryptopiaApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin(this, listOf(myModule))

    }
}