package com.cryptopia.android.pPeterle.ui.activity

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.cryptopia.android.pPeterle.R
import com.cryptopia.android.pPeterle.data.worker.OpenTradersWork
import com.cryptopia.android.pPeterle.ui.fragment.BalanceFragment
import com.cryptopia.android.pPeterle.ui.fragment.HomeFragment
import com.cryptopia.android.pPeterle.ui.fragment.MarketFragment
import com.cryptopia.android.pPeterle.ui.fragment.OrdersFragment
import com.ncapdevi.fragnav.FragNavController
import com.ncapdevi.fragnav.FragNavTransactionOptions
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    //private lateinit var navController: FragNavController

    private val BACK_STACK_ROOT_TAG = "root_fragment"

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 1) {
            super.onBackPressed()
        }
        super.onBackPressed()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        main_navigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    setupFragment(HomeFragment.newInstance())
                }
                R.id.navigation_dashboard -> {
                    setupFragment(OrdersFragment.newInstance())

                }
                R.id.navigation_notifications -> {
                    setupFragment(BalanceFragment.newInstance())
                }
                else -> false
            }
        }

        val fm = supportFragmentManager.apply { popBackStack(BACK_STACK_ROOT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE) }

        fm.beginTransaction()
            .replace(R.id.main_frameLayout, HomeFragment.newInstance())
            .addToBackStack(BACK_STACK_ROOT_TAG)
            .commit()

        startAlarmManager()
    }

    private fun setupFragment(fragment: Fragment): Boolean {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frameLayout, fragment)
            .addToBackStack(null)
            .commit()
        return true
    }

    private fun startAlarmManager() {
        val alarmReceiver = Intent(this, OpenTradersWork::class.java)
        val pendinIntent = PendingIntent.getBroadcast(this, 0, alarmReceiver, 0)
        val manager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 900000, pendinIntent)
    }
    
}
