package com.lripl.database;

import android.content.Context;
import android.content.SharedPreferences;




public class SharedPrefsHelper {

    public static String PREF_KEY = "app_prefs";
    private SharedPreferences mSharedPreferences;
    private static SharedPrefsHelper sharedPrefsHelper;

    private SharedPrefsHelper(Context context) {
        mSharedPreferences = context.getSharedPreferences(PREF_KEY,context.MODE_PRIVATE);
    }

    public static SharedPrefsHelper getInstanse(Context context){
        if(sharedPrefsHelper == null){
            sharedPrefsHelper = new SharedPrefsHelper(context);
        }
        return sharedPrefsHelper;
    }
    public void put(String key, String value) {
        mSharedPreferences.edit().putString(key, value).apply();
    }

    public void put(String key, int value) {
        mSharedPreferences.edit().putInt(key, value).apply();
    }

    public void put(String key, float value) {
        mSharedPreferences.edit().putFloat(key, value).apply();
    }

    public void put(String key, boolean value) {
        mSharedPreferences.edit().putBoolean(key, value).apply();
    }

    public String get(String key, String defaultValue) {
        return mSharedPreferences.getString(key, defaultValue);
    }

    public Integer get(String key, int defaultValue) {
        return mSharedPreferences.getInt(key, defaultValue);
    }

    public Float get(String key, float defaultValue) {
        return mSharedPreferences.getFloat(key, defaultValue);
    }

    public Boolean get(String key, boolean defaultValue) {
        return mSharedPreferences.getBoolean(key, defaultValue);
    }

    public void deleteSavedData(String key) {
        mSharedPreferences.edit().remove(key).apply();
    }
}
