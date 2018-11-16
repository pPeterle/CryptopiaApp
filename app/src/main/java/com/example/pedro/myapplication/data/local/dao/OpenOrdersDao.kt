package com.example.pedro.myapplication.data.local.dao

import android.arch.persistence.room.*
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import com.example.pedro.myapplication.data.model.OpenOrder

@Dao
interface OpenOrdersDao {

    @Insert(onConflict = REPLACE)
    fun insert(order: List<OpenOrder>)

    @Query("SELECT * FROM OpenOrders")
    fun getAllOpenOrders(): List<OpenOrder>

    @Query("DELETE FROM OpenOrders WHERE orderId = :id")
    fun removeOpenOrder(id: Double)

    @Query("DELETE FROM OpenOrders")
    fun removeAllOrders()
}