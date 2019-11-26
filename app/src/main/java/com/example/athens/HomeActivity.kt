package com.example.athens

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import com.example.athens.api.MissionFragment

class HomeActivity : AppCompatActivity() {
    private val manager = supportFragmentManager
    private val shopFragment = ShopFragment()
    private val missionFragment = MissionFragment()

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
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val transaction = manager.beginTransaction()
        transaction.replace(R.id.framelayout, shopFragment).commit()

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
    }
}
