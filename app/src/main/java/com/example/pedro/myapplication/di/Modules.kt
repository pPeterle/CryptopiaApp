package com.example.pedro.myapplication.di

import android.arch.persistence.room.Room
import com.example.pedro.myapplication.balance.BalanceViewModel
import com.example.pedro.myapplication.data.CryptopiaRepositoty
import com.example.pedro.myapplication.data.local.AppDatabase
import com.example.pedro.myapplication.data.local.AppPreferences
import com.example.pedro.myapplication.details.DetailsViewModel
import com.example.pedro.myapplication.home.HomeViewModel
import com.example.pedro.myapplication.orders.OrdersViewModel
import com.example.pedro.myapplication.start.StartViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val myModule = module {

    single( definition = {
        Room.databaseBuilder(
            androidApplication(),
            AppDatabase::class.java,
            "MyDatabase"
        ).build()
    })

    single { AppPreferences(context = androidContext()) }
    single { CryptopiaRepositoty(sharedPreferences = get(), appDatabase = get()) }

    viewModel { HomeViewModel(cryptopiaRepositoty = get ()) }
    viewModel { DetailsViewModel(cryptopiaRepositoty = get ()) }
    viewModel { OrdersViewModel(cryptopiaRepositoty = get()) }
    viewModel { StartViewModel(cryptopiaRepositoty = get()) }
    viewModel { BalanceViewModel(repository = get()) }
}