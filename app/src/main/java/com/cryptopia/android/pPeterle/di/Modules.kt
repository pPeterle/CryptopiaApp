package com.cryptopia.android.pPeterle.di

import android.arch.persistence.room.Room
import com.cryptopia.android.pPeterle.data.CryptopiaRepository
import com.cryptopia.android.pPeterle.data.local.AppDatabase
import com.cryptopia.android.pPeterle.data.local.AppPreferences
import com.cryptopia.android.pPeterle.data.remote.CryptopiaService
import com.cryptopia.android.pPeterle.data.remote.RemoteRepository
import com.cryptopia.android.pPeterle.presentation.mapper.BalanceMapper
import com.cryptopia.android.pPeterle.presentation.mapper.OpenOrdersMapper
import com.cryptopia.android.pPeterle.presentation.mapper.TradePairMapper
import com.cryptopia.android.pPeterle.presentation.viewModel.*
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val myModule = module {

    single {
        Room.databaseBuilder(
            androidApplication(),
            AppDatabase::class.java,
            "MyDatabase"
        ).fallbackToDestructiveMigration()
            .build()
    }

    single { AppPreferences(context = androidContext()) }

    single {
        val sharedPreferences: AppPreferences = get()
        RemoteRepository(apiKey = sharedPreferences.apiKey, secretKey = sharedPreferences.secretKey, cryptopiaService = CryptopiaService.getInstance(androidApplication()))
    }

    single { CryptopiaRepository(sharedPreferences = get(), appDatabase = get(), remoteRepository = get()) }

    factory { OpenOrdersMapper() }
    factory { BalanceMapper() }
    factory { TradePairMapper() }

    viewModel { HomeViewModel(cryptopiaRepository = get(), mapper = get()) }
    viewModel { DetailsViewModel(cryptopiaRepository = get()) }
    viewModel { OrdersViewModel(cryptopiaRepository = get(), mapper = get()) }
    viewModel { StartViewModel(cryptopiaRepository = get()) }
    viewModel { BalanceViewModel(repository = get(), mapper = get()) }
    viewModel { DepthViewModel(repository = get()) }
    viewModel { CandleStickViewModel(repository = get()) }
    viewModel { KeysViewModel(repository = get()) }
}