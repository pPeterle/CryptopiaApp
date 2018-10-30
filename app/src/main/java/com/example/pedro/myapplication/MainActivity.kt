package com.example.pedro.myapplication

import android.os.Bundle
import android.os.PersistableBundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import com.example.pedro.myapplication.home.HomeFragment
import com.example.pedro.myapplication.orders.OrdersFragment
import com.ncapdevi.fragnav.FragNavController
import com.ncapdevi.fragnav.FragNavTransactionOptions
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), FragNavController.RootFragmentListener {

    private lateinit var navController: FragNavController
    lateinit var toolbarTitle: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        main_toolbar.apply {
            setSupportActionBar(this)
        }
        toolbarTitle = main_toolbar_title
        supportActionBar?.setDisplayShowTitleEnabled(false)

        navController = FragNavController.newBuilder(savedInstanceState, supportFragmentManager, R.id.main_frameLayout)
            .rootFragments(mutableListOf(HomeFragment.newInstance(), OrdersFragment.newInstance()))
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
                    return@setOnNavigationItemSelectedListener true
                }
            }
            false
        }
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
            else -> throw RuntimeException("Fragment do not added")
        }

}
