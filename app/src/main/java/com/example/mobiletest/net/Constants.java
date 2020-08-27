package com.example.mobiletest.net;

import com.example.mobiletest.App;
import com.example.mobiletest.util.NetUtil;

public class Constants {

    //设置默认超时时间
    public static final int DEFAULT_TIME = 10;

    //加密数据key
    public static final String DATA_LIST = "dataList";

    public final static String BaseUrl = "http://"+ NetUtil.getIP(App.getInstance().getApplicationContext())+":5050/";
}
