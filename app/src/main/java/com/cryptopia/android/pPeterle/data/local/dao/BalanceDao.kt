package com.cryptopia.android.pPeterle.data.local.dao

import android.arch.persistence.room.*
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import com.cryptopia.android.pPeterle.data.model.Balance

@Dao
interface BalanceDao {

    @Insert(onConflict = REPLACE)
    fun insertBalance(order: List<Balance>)

    @Query("SELECT * FROM balances")
    fun getAllBalances(): List<Balance>

    @Query("DELETE FROM balances WHERE currencyId = :id")
    fun removeBalance(id: Double)

    @Query("DELETE FROM balances")
    fun removeAllBalances()
}