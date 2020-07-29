package com.example.mobiletest.net;

import com.example.mobiletest.App;
import com.example.mobiletest.util.WifiUtil;

public class Constans {

    //设置默认超时时间
    public static final int DEFAULT_TIME = 10;

    public final static String BaseUrl = "http://"+ WifiUtil.getIP(App.getInstance().getApplicationContext())+":5050/";
}
