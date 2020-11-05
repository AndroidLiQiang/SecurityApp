package com.example.mobiletest.util;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * author: liqiang
 * e-mail: qiang_li1@asdc.com.cn
 * date  : 2020/8/18
 * desc  :
 */
public class StringUtil {
    public static String getRandomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 获取时间
     */
    public static String getTime() {
        Date date = new Date();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        return dateFormat.format(date);
    }
}
