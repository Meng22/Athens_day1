package com.example.athens.main

import android.app.Application
import android.util.Log
import com.uuzuche.lib_zxing.activity.ZXingLibrary

fun println(message: String) {
    Log.d("APP", message)
}

class CustomApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        ZXingLibrary.initDisplayOpinion(this)
    }
}
