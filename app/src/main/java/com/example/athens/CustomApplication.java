package com.example.athens;

import android.app.Application;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

public class CustomApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ZXingLibrary.initDisplayOpinion(this);

    }
}
