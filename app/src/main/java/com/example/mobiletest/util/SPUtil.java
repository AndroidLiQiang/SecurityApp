package com.example.mobiletest.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.mobiletest.App;

import java.util.HashSet;
import java.util.Set;


/**
 * SP
 */
public class SPUtil {

    private static String KEY = "Mobile";

    private static SharedPreferences getSp() {
        SharedPreferences sp = App.getInstance().getSharedPreferences(KEY, Context.MODE_PRIVATE);
        return sp;
    }


    public static void putString(String key, String value) {
        SharedPreferences sp = getSp();
        sp.edit().putString(key, value).commit();
    }


    public static String getString(String key, String defValue) {
        SharedPreferences sp = getSp();
        return sp.getString(key, defValue);
    }


    public static String getString(String key) {
        return getString(key, "");
    }

    public static void remove(String key) {
        SharedPreferences sp = getSp();
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        editor.commit();
    }

    public static void clear() {
        SharedPreferences sp = getSp();
        sp.edit().clear().commit();
    }
}
