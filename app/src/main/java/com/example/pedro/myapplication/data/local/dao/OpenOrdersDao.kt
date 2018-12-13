package com.example.pedro.myapplication.data.local.dao

import android.arch.persistence.room.*
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import com.example.pedro.myapplication.data.model.Balance
import com.example.pedro.myapplication.data.model.OpenOrder

@Dao
interface OpenOrdersDao {

    @Insert(onConflict = REPLACE)
    fun insertOrders(order: List<OpenOrder>)

    @Query("SELECT * FROM openorder")
    fun getAllOrders(): List<OpenOrder>

    @Query("DELETE FROM openorder WHERE orderId = :id")
    fun removeOrder(id: Double)

    @Query("DELETE FROM openorder")
    fun removeAllOrders()
}