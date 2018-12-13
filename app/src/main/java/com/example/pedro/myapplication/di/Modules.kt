package com.example.pedro.myapplication.di

import android.arch.persistence.room.Room
import com.example.pedro.myapplication.data.CryptopiaRepository
import com.example.pedro.myapplication.data.local.AppDatabase
import com.example.pedro.myapplication.data.local.AppPreferences
import com.example.pedro.myapplication.data.remote.CryptopiaService
import com.example.pedro.myapplication.data.remote.RemoteRepository
import com.example.pedro.myapplication.presentation.viewModel.*
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val myModule = module {

    single(definition = {
        Room.databaseBuilder(
            androidApplication(),
            AppDatabase::class.java,
            "MyDatabase"
        ).fallbackToDestructiveMigration()
            .build()
    })

    single { AppPreferences(context = androidContext()) }

    single {
        val sharedPreferences: AppPreferences = get()
        RemoteRepository(apiKey = sharedPreferences.apiKey, secretKey = sharedPreferences.secretKey, cryptopiaService = CryptopiaService.getInstance(androidApplication()))
    }

    single { CryptopiaRepository(sharedPreferences = get(), appDatabase = get(), remoteRepository = get()) }

    viewModel { HomeViewModel(cryptopiaRepository = get()) }
    viewModel { DetailsViewModel(cryptopiaRepository = get()) }
    viewModel { OrdersViewModel(cryptopiaRepository = get()) }
    viewModel { StartViewModel(cryptopiaRepository = get()) }
    viewModel { BalanceViewModel(repository = get()) }
    viewModel { DepthViewModel(repository = get()) }
    viewModel { CandleStickViewModel(repository = get()) }
}