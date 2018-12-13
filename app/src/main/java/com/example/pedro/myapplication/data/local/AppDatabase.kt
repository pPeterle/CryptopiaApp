package com.example.pedro.myapplication.data.local

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.example.pedro.myapplication.data.local.dao.BalanceDao
import com.example.pedro.myapplication.data.local.dao.OpenOrdersDao
import com.example.pedro.myapplication.data.model.Balance
import com.example.pedro.myapplication.data.model.OpenOrder

@Database(entities = arrayOf(OpenOrder::class, Balance::class), version = 3)
abstract class AppDatabase : RoomDatabase() {

    abstract fun balanceDao(): BalanceDao

    abstract fun openOrdersDao(): OpenOrdersDao

}