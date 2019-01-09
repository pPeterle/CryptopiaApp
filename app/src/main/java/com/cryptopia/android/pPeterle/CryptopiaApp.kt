package com.cryptopia.android.pPeterle

import android.app.Application
import com.cryptopia.android.pPeterle.di.applicationModule
import com.cryptopia.android.pPeterle.di.mapperModule
import com.cryptopia.android.pPeterle.di.viewModelModule
import com.squareup.leakcanary.LeakCanary
import org.koin.android.ext.android.startKoin

class CryptopiaApp : Application() {

    override fun onCreate() {
        super.onCreate()
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this)
        startKoin(this, listOf(applicationModule, mapperModule, viewModelModule))

    }
}