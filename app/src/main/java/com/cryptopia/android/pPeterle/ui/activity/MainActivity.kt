package com.cryptopia.android.pPeterle.ui.activity

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.cryptopia.android.pPeterle.R
import com.cryptopia.android.pPeterle.data.worker.OpenTradersWork
import com.cryptopia.android.pPeterle.ui.fragment.BalanceFragment
import com.cryptopia.android.pPeterle.ui.fragment.HomeFragment
import com.cryptopia.android.pPeterle.ui.fragment.OrdersFragment
import com.ncapdevi.fragnav.FragNavController
import com.ncapdevi.fragnav.FragNavTransactionOptions
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), FragNavController.RootFragmentListener {

    private lateinit var navController: FragNavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navController = FragNavController.newBuilder(savedInstanceState, supportFragmentManager,
            R.id.main_frameLayout
        )
            .rootFragments(mutableListOf(HomeFragment.newInstance(), OrdersFragment.newInstance(), BalanceFragment.newInstance()))
            .defaultTransactionOptions(FragNavTransactionOptions.newBuilder().build())
            .build()

        main_navigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    navController.switchTab(FragNavController.TAB1)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigation_dashboard -> {
                    navController.switchTab(FragNavController.TAB2)
                    return@setOnNavigationItemSelectedListener true

                }
                R.id.navigation_notifications -> {
                    navController.switchTab(FragNavController.TAB3)
                    return@setOnNavigationItemSelectedListener true
                }
            }
            false
        }

        startAlarmManager()
    }

    private fun startAlarmManager() {
        val alarmReceiver = Intent(this, OpenTradersWork::class.java)
        val pendinIntent = PendingIntent.getBroadcast(this, 0, alarmReceiver, 0)
        val manager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 900000, pendinIntent)
    }

    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
        super.onSaveInstanceState(outState, outPersistentState)
        if (outState != null)
        navController.onSaveInstanceState(outState)
    }

    override fun getRootFragment(fragmentId: Int): Fragment =
        when (fragmentId) {
            FragNavController.TAB1 -> HomeFragment.newInstance()
            FragNavController.TAB2 -> OrdersFragment.newInstance()
            FragNavController.TAB3 -> BalanceFragment.newInstance()
            else -> throw RuntimeException("Fragment do not added")
        }

}
