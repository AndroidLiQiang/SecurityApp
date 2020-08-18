package com.example.mobiletest.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.mobiletest.App;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;


/**
 * SP
 */
public class SPUtil {

    private static SharedPreferences getSp() {
        String KEY = "mobile";
        return App.getInstance().getSharedPreferences(KEY, Context.MODE_PRIVATE);
    }

    public static void putList(String key, List<String> value) {
        SharedPreferences sp = getSp();
        Gson gson = new Gson();
        String data = gson.toJson(value);
        sp.edit().putString(key, data).apply();
    }

    public static List<String> getList(String key) {
        SharedPreferences sp = getSp();
        String data = sp.getString(key, "");
        Gson gson = new Gson();
        Type listType = new TypeToken<List<String>>() {
        }.getType();
        return gson.fromJson(data, listType);
    }

    public static void putString(String key, String value) {
        SharedPreferences sp = getSp();
        sp.edit().putString(key, value).apply();
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
        editor.apply();
    }

    public static void clear() {
        SharedPreferences sp = getSp();
        sp.edit().clear().apply();
    }
}
