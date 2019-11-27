package com.example.athens

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import com.example.athens.goods.ShopFragment
import com.example.athens.mission.MissionFragment
import com.example.athens.portfolio.PortfolioFragment

class HomeActivity : AppCompatActivity() {

    private val manager = supportFragmentManager
    private val shopFragment = ShopFragment()
    private val missionFragment = MissionFragment()
    private val portfolioFragment = PortfolioFragment(

    )
    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.nav_shop -> {
                val transaction = manager.beginTransaction()
                transaction.replace(R.id.framelayout, shopFragment).commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_mission -> {
                val transaction = manager.beginTransaction()
                transaction.replace(R.id.framelayout, missionFragment).commit()

                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_portfolio -> {
                val transaction = manager.beginTransaction()
                transaction.replace(R.id.framelayout, portfolioFragment).commit()

                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        //初始畫面
        val transaction = manager.beginTransaction()
        transaction.replace(R.id.framelayout, shopFragment).commit()
        //導覽列
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)


    }

}
