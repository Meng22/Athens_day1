package com.example.athens.main

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import com.example.athens.R
import com.example.athens.runner_goods.ShopFragment
import com.example.athens.runner_mission.MissionFragment
import com.example.athens.runner_portfolio.PortfolioFragment

class RunnerActivity : AppCompatActivity() {

    private val manager = supportFragmentManager
    private val shopFragment = ShopFragment()
    private val missionFragment = MissionFragment()
    private val portfolioFragment = PortfolioFragment()

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.nav_station -> {
                val transaction = manager.beginTransaction()
                transaction.replace(R.id.framelayout_runner, shopFragment).commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_mission -> {
                val transaction = manager.beginTransaction()
                transaction.replace(R.id.framelayout_runner, missionFragment).commit()

                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_portfolio -> {
                val transaction = manager.beginTransaction()
                transaction.replace(R.id.framelayout_runner, portfolioFragment).commit()

                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_runner)

        //初始畫面
        val transaction = manager.beginTransaction()
        transaction.replace(R.id.framelayout_runner, shopFragment).commit()
        //導覽列
        val navView: BottomNavigationView = findViewById(R.id.nav_runner)
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)


    }

}
