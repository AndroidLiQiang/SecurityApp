package com.example.mobiletest.net;

import android.app.Activity;

import com.example.mobiletest.bean.MacBean;
import com.example.mobiletest.bean.RandomBean;

import java.util.HashMap;

/**
 * author : liqiang
 * e-mail : qiang_li1@asdc.com.cn
 * date   : 2020/7/13
 * desc   :
 */
public class RequestUtils {

    /**
     * 获取随机数
     */
    public static void getRandom(Activity context, HashMap<String, String> map, MyObserver<RandomBean> observer) {
        RetrofitUtils.getApiUrl()
                .getRandom(map).compose(RxHelper.observableIO2Main(context))
                .subscribe(observer);
    }

    /**
     * 验证mac
     */
    public static void verifyMac(Activity context, HashMap<String, Object> map, MyObserver<MacBean> observer) {
        RetrofitUtils.getApiUrl()
                .verifyMac(map).compose(RxHelper.observableIO2Main(context))
                .subscribe(observer);
    }

    public static void postRandom(Activity context, HashMap<String, String> map, MyObserver<RandomBean> observer) {
        RetrofitUtils.getApiUrl()
                .postRandom(map).compose(RxHelper.observableIO2Main(context))
                .subscribe(observer);
    }

}

