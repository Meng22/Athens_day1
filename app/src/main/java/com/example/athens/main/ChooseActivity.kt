package com.example.athens.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.athens.R
import kotlinx.android.synthetic.main.activity_choose.*

class ChooseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose)

        entry_athens.setOnClickListener {
            entry(1)
        }

    }

    //跳轉進入各驛站
    fun entry(mode: Int){
        val intent = Intent(this, StationActivity::class.java)
        startActivityForResult(intent, mode)
    }
}
