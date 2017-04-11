package com.rothen.rbtimer.model;

import android.content.Context;
import android.content.res.Resources;

import com.rothen.rbtimer.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lecros0 on 11/04/2017.
 */

public class CaptureTheFlagSettingsParameters implements IActivitySettingsParameters {

    public static final String CAT_TIMER = "CAT_TIMER";
    public static final String PAR_TIMER_GAMETIME = "PAR_TIMER_GAMETIME";
    public static final String PAR_TIMER_CAPTURETIME = "PAR_TIMER_CAPTURETIME";
    public static final String PAR_TIMER_BUTTONPRESSTIME = "PAR_TIMER_BUTTONPRESSTIME";

    private List<SettingsCategory> listSettings;

    public CaptureTheFlagSettingsParameters(Context context) {
        Resources resources = context.getResources();
        String title;
        listSettings = new ArrayList<>();

        SettingsCategory timers = new SettingsCategory();
        timers.setCategoryTitle(resources.getString(R.string.settings_Time));
        timers.setCategoryId(CAT_TIMER);
        title = resources.getString(R.string.settings_Time_GameTime);
        timers.addParameter(new SettingsParameter(
                title,
                PAR_TIMER_GAMETIME,
                0,
                900,
                SettingsParameterType.TYPE_INT
        ));
        title = resources.getString(R.string.settings_Time_CaptureTime);
        timers.addParameter(new SettingsParameter(
                title,
                PAR_TIMER_CAPTURETIME,
                0,
                60,
                SettingsParameterType.TYPE_INT
        ));
        title = resources.getString(R.string.settings_Time_ButtonPress);
        timers.addParameter(new SettingsParameter(
                title,
                PAR_TIMER_BUTTONPRESSTIME,
                0,
                10,
                SettingsParameterType.TYPE_INT
        ));
        listSettings.add(timers);
    }

    @Override
    public List<SettingsCategory> getCategories() {
        return listSettings;
    }
}
