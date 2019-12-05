package com.example.athens.main

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import com.example.athens.HistoryFragment
import com.example.athens.InfoFragment
import com.example.athens.R
import okhttp3.MediaType
import okhttp3.RequestBody
import java.io.File

class StationActivity : AppCompatActivity() {
    private val manager = supportFragmentManager
    private val infoFragment = InfoFragment()
    private val historyFragment = HistoryFragment()


    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.nav_information -> {
                val transaction = manager.beginTransaction()
                transaction.replace(R.id.framelayout_station, infoFragment).commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_history -> {
                val transaction = manager.beginTransaction()
                transaction.replace(R.id.framelayout_station, historyFragment).commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_achieve -> {
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_station)
        //預設
        val transaction = manager.beginTransaction()
        transaction.replace(R.id.framelayout_station, infoFragment).commit()


        val navView: BottomNavigationView = findViewById(R.id.nav_station)
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
    }
}
