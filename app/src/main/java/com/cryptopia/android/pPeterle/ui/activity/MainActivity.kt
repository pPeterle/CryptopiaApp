package com.cryptopia.android.pPeterle.ui.activity

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.cryptopia.android.pPeterle.R
import com.cryptopia.android.pPeterle.data.worker.OpenTradersWork
import com.cryptopia.android.pPeterle.ui.fragment.BalanceFragment
import com.cryptopia.android.pPeterle.ui.fragment.HomeFragment
import com.cryptopia.android.pPeterle.ui.fragment.OrdersFragment
import com.ncapdevi.fragnav.FragNavController
import com.ncapdevi.fragnav.FragNavSwitchController
import com.ncapdevi.fragnav.FragNavTransactionOptions
import com.ncapdevi.fragnav.tabhistory.UniqueTabHistoryStrategy
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), FragNavController.RootFragmentListener {

    private val fragNavController: FragNavController = FragNavController(supportFragmentManager, R.id.container).apply {
        navigationStrategy = UniqueTabHistoryStrategy(object : FragNavSwitchController {
            override fun switchTab(index: Int, transactionOptions: FragNavTransactionOptions?) {
                main_navigation.selectedItemId = when (index) {
                    0 -> R.id.navigation_home
                    1 -> R.id.navigation_order
                    2 -> R.id.navigation_balance
                    else -> throw IllegalStateException("Wrong Index")
                }
            }
        })

    }

    override val numberOfRootFragments: Int
        get() = 3

    override fun onBackPressed() {
        if (fragNavController.popFragment().not()) {
            super.onBackPressed()
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        fragNavController.onSaveInstanceState(outState!!)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragments = listOf(HomeFragment.newInstance(), OrdersFragment.newInstance(), BalanceFragment.newInstance())
        fragNavController.rootFragments = fragments

        main_navigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    fragNavController.switchTab(0)
                    true
                }
                R.id.navigation_order -> {
                    fragNavController.switchTab(1)
                    true

                }
                R.id.navigation_balance -> {
                    fragNavController.switchTab(2)
                    true
                }
                else -> false
            }

        }

        fragNavController.initialize(FragNavController.TAB1, savedInstanceState)

        startAlarmManager()
    }

    override fun getRootFragment(index: Int) =
        when (index) {
            0 -> HomeFragment.newInstance()
            1 -> OrdersFragment.newInstance()
            2 -> BalanceFragment.newInstance()
            else -> throw IllegalStateException("Wrong Index")
        }

    private fun startAlarmManager() {
        val alarmReceiver = Intent(this, OpenTradersWork::class.java)
        val pendinIntent = PendingIntent.getBroadcast(this, 0, alarmReceiver, 0)
        val manager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 900000, pendinIntent)
    }

}
