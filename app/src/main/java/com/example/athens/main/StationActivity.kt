package com.example.athens.main

import android.graphics.Color
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.athens.station.HistoryFragment
import com.example.athens.station.station_Iifo.InfoFragment
import com.example.athens.R
import com.example.athens.station.LevelFragment
import kotlinx.android.synthetic.main.activity_station.*

class StationActivity : AppCompatActivity() {
    private val manager = supportFragmentManager
    private var mode : Int = 1
    private val infoFragment1 = InfoFragment(1)
    private val infoFragment2 = InfoFragment(2)
    private val infoFragment3 = InfoFragment(3)
    private val infoFragment4 = InfoFragment(4)

    private val historyFragment1 = HistoryFragment(1)
    private val historyFragment2 = HistoryFragment(2)
    private val historyFragment3 = HistoryFragment(3)
    private val historyFragment4 = HistoryFragment(4)

    private val levelFragment1 = LevelFragment(1)
    private val levelFragment2 = LevelFragment(2)
    private val levelFragment3 = LevelFragment(3)
    private val levelFragment4 = LevelFragment(4)

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.nav_information -> {
                val transaction = manager.beginTransaction()
                transaction.replace(R.id.framelayout_station, infoFragment_switch(mode)).commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_history -> {
                val transaction = manager.beginTransaction()
                transaction.replace(R.id.framelayout_station, historyFragment_switch(mode)).commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_achieve -> {
                val transaction = manager.beginTransaction()
                transaction.replace(R.id.framelayout_station, levelFragment_switch(mode)).commit()

                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_station)

        mode = intent.getIntExtra("mode", 1)
        switchColor(mode)

        //預設
        val transaction = manager.beginTransaction()
        transaction.replace(R.id.framelayout_station, infoFragment_switch(mode)).commit()

        val navView: BottomNavigationView = findViewById(R.id.nav_station)
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
    }


    fun levelFragment_switch(mode: Int): Fragment {
        when(mode){
            1 ->{
                return levelFragment1
            }
            2 ->{
                return levelFragment2
            }
            3 ->{
                return levelFragment3
            }
            4 ->{
                return levelFragment4
            }
        }
        return levelFragment1
    }

    fun infoFragment_switch(mode: Int): Fragment {
        when(mode){
            1 ->{
                return infoFragment1
            }
            2 ->{
                return infoFragment2
            }
            3 ->{
                return infoFragment3
            }
            4 ->{
                return infoFragment4
            }
        }
        return infoFragment1
    }
    fun historyFragment_switch(mode: Int): Fragment {
        when(mode){
            1 ->{
                return historyFragment1
            }
            2 ->{
                return historyFragment2
            }
            3 ->{
                return historyFragment3
            }
            4 ->{
                return historyFragment4
            }
        }
        return historyFragment1
    }



    fun switchColor(mode: Int) {
        when (mode) {
            1 -> {
                nav_station.setBackgroundColor(Color.rgb(0, 84, 147))
            }
            2 -> {
                nav_station.setBackgroundColor(Color.rgb(70, 124, 36))
            }
            3 -> {
                nav_station.setBackgroundColor(Color.rgb(245, 180, 51))
            }
            4 -> {
                nav_station.setBackgroundColor(Color.rgb(148, 17, 0))
            }
        }
    }
}
