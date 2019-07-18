package com.khemsharmadev.simplecalculator;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Utility {

    public static void setTheme(Context context, int theme) {
        SharedPreferences prefs = context.getSharedPreferences( "app_setting", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(context.getString(R.string.prefs_theme_key), theme).commit();
        editor.apply();
    }
    public static int getTheme(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("app_setting",Context.MODE_PRIVATE);
        return prefs.getInt(context.getString(R.string.prefs_theme_key), -1);

    }
}
