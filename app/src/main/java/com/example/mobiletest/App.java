package com.example.mobiletest;

import android.app.Application;

import com.example.teesimmanager.TeeSimManager;

/**
 * author: liqiang
 * e-mail: qiang_li1@asdc.com.cn
 * date  : 2020/7/27
 * desc  :
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        TeeSimManager.getInstance().init(this);
    }
}
