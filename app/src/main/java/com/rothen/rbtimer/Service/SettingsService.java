package com.rothen.rbtimer.Service;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Manage and save settings
 * Created by apest on 16/03/2017.
 */

public class SettingsService {

    private static final String PREFERENCES = "Preference";

    private static final int BUTTON_DEFAULT_TIME = 5*1000;
    public static final int BUTTON_DEFAULT_TIME_MIN = 1000;
    public static final int BUTTON_DEFAULT_TIME_MAX = 30*1000;

    public static final String BUTTON_PRESS_TIME = "ButtonPressTime";


    private static final int BIP_FAST_DEFAULT_TIME =  10*1000;
    public static final int BIP_FAST_TIME_MIN = 1000;
    public static final int BIP_FAST_TIME_MAX = 30*1000;
    public static final String BIP_FAST_TIME = "BipFastTime";


    private static final int BIP_SLOW_DEFAULT_TIME =  30*1000;
    public static final int BIP_SLOW_TIME_MIN = 1000;
    public static final int BIP_SLOW_TIME_MAX = 30*1000;
    public static final String BIP_SLOW_TIME = "BipSlowTime";


    private static final int BOMB_DEFAULT_TIME =  60*1000;
    public static final int BOMB_TIME_MIN = 10*1000;
    public static final int BOMB_TIME_MAX = 600*1000;
    public static final String BOMB_TIME = "BombTime";


    private SharedPreferences prefs;
    SharedPreferences.Editor editor;

    public SettingsService(Context context) {
        prefs = context.getSharedPreferences(PREFERENCES,Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public int getButtonPressTime() {
        return  prefs.getInt(BUTTON_PRESS_TIME, BUTTON_DEFAULT_TIME);
    }

    public void setButtonPressTime(int buttonPressTime) {
        editor.putInt(BUTTON_PRESS_TIME,buttonPressTime);
        editor.apply();
    }

    public int getBipFastTime() {
        return  prefs.getInt(BIP_FAST_TIME, BIP_FAST_DEFAULT_TIME);
    }

    public void setBipFastTime(int bipFastTime) {
        editor.putInt(BIP_FAST_TIME,bipFastTime);
        editor.apply();
    }

    public int getBipSlowTime() {
        return  prefs.getInt(BIP_SLOW_TIME, BIP_SLOW_DEFAULT_TIME);
    }

    public void setBipSlowTime(int bipSlowTime) {
        editor.putInt(BIP_SLOW_TIME,bipSlowTime);
        editor.apply();
    }

    public int getBombTime() {
        return  prefs.getInt(BOMB_TIME, BOMB_DEFAULT_TIME);
    }

    public void setBombTime(int bombTime) {
        editor.putInt(BOMB_TIME,bombTime);
        editor.apply();
    }
}
