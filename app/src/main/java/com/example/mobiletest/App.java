package com.example.mobiletest;

import android.app.Application;
import android.os.Handler;

/**
 * author: liqiang
 * e-mail: qiang_li1@asdc.com.cn
 * date  : 2020/7/27
 * desc  :
 */
public class App extends Application {
    private static App instance;
    private static int reason;//nfc是否贴近设备

    public static int getReason() {
        return reason;
    }

    public static void setReason(int reason) {
        App.reason = reason;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        //初始化handler
        mHandler = new Handler();
    }

    public static App getInstance() {
        return instance;
    }

    /**
     * 在主线程中刷新UI的方法
     *
     * @param r
     */
    public static void runOnUIThread(Runnable r) {
        App.getMainHandler().post(r);
    }

    //qcl用来在主线程中刷新ui
    private static Handler mHandler;

    public static Handler getMainHandler() {
        return mHandler;
    }
}
