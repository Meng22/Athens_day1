package com.example.athens.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.athens.R
import com.example.athens.api.API
import kotlinx.android.synthetic.main.activity_choose.*

class ChooseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose)

        btn_logout.setOnClickListener{
            API.token = ""
            val intent = Intent(this, MainActivity::class.java)
            startActivityForResult(intent, 100)
        }

        entry_athens.setOnClickListener {
            entry(1)
        }
        entry_phocis.setOnClickListener {
            entry(2)
        }
        entry_arcadia.setOnClickListener {
            entry(3)
        }
        entry_sparta.setOnClickListener {
            entry(4)
        }

    }

    //跳轉進入各驛站
    fun entry(mode: Int){
        val intent = Intent(this, StationActivity::class.java)
        intent.putExtra("mode", mode)
        startActivityForResult(intent, 1)
    }
}
