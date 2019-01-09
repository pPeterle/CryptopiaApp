package com.cryptopia.android.pPeterle.data.local

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.cryptopia.android.pPeterle.data.local.dao.BalanceDao
import com.cryptopia.android.pPeterle.data.local.dao.OpenOrdersDao
import com.cryptopia.android.pPeterle.data.model.Balance
import com.cryptopia.android.pPeterle.data.model.OpenOrder

@Database(entities = arrayOf(OpenOrder::class, Balance::class), version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun balanceDao(): BalanceDao

    abstract fun openOrdersDao(): OpenOrdersDao

}