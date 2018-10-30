package com.example.pedro.myapplication.di

import com.example.pedro.myapplication.data.CryptopiaRepositoty
import com.example.pedro.myapplication.data.local.AppPreferences
import com.example.pedro.myapplication.details.DetailsViewModel
import com.example.pedro.myapplication.home.HomeViewModel
import com.example.pedro.myapplication.orders.OrdersViewModel
import com.example.pedro.myapplication.start.StartViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val myModule = module {
    single { AppPreferences(context = androidContext()) }
    single { CryptopiaRepositoty(sharedPreferences = get()) }

    viewModel { HomeViewModel(cryptopiaRepositoty = get ()) }
    viewModel { DetailsViewModel(cryptopiaRepositoty = get ()) }
    viewModel { OrdersViewModel(cryptopiaRepositoty = get()) }
    viewModel { StartViewModel(cryptopiaRepositoty = get()) }
}