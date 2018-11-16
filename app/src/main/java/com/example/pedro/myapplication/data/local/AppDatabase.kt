package com.example.pedro.myapplication.data.local

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.example.pedro.myapplication.data.local.dao.OpenOrdersDao
import com.example.pedro.myapplication.data.model.OpenOrder

@Database(entities = arrayOf(OpenOrder::class), version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun openOdersDao(): OpenOrdersDao

}