package com.rothen.rbtimer.service;

import android.content.Context;
import android.content.SharedPreferences;

import com.rothen.rbtimer.model.SettingsCategory;
import com.rothen.rbtimer.model.SettingsParameter;

import java.util.Dictionary;
import java.util.List;

/**
 * save and restore settings
 * Created by apest on 16/03/2017.
 */

public class SettingsService {

    private static final String PREFERENCES = "Preference";


    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    public SettingsService(Context context) {
        prefs = context.getSharedPreferences(PREFERENCES,Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void saveListCategories(List<SettingsCategory> list)
    {
        for(SettingsCategory c : list)
        {
            putCategory(c);
        }
    }

    public List<SettingsCategory> getListCategories(List<SettingsCategory> list)
    {
        for(SettingsCategory c : list)
        {
            c=getCategory(c);
        }
        return list;
    }

    public void putParameter(SettingsParameter parameter, boolean multipleInsert) {
        switch (parameter.getParameterType()) {
            case TYPE_BOOLEAN:
                editor.putBoolean(parameter.getKey(), (Boolean) parameter.getValue());
                break;
            case TYPE_FLOAT:
                editor.putFloat(parameter.getKey(), (float) parameter.getValue());
                break;
            case TYPE_INT:
                editor.putInt(parameter.getKey(), (int) parameter.getValue());
                break;
        }
        if (!multipleInsert) {
            editor.commit();
        }
    }

    public void putCategory(SettingsCategory category)
    {
        for(SettingsParameter p : category.getParameterList())
        {
            putParameter(p,true);
        }
        editor.commit();
    }

    public SettingsParameter getParameter( SettingsParameter parameter)
    {
        switch (parameter.getParameterType())
        {
            case TYPE_BOOLEAN:
                parameter.setValue(prefs.getBoolean(parameter.getKey(),(Boolean)parameter.getDefaultValue()));
                break;
            case TYPE_FLOAT:
                parameter.setValue(prefs.getFloat(parameter.getKey(),(float)parameter.getDefaultValue()));
                break;
            case TYPE_INT:
                parameter.setValue(prefs.getInt(parameter.getKey(),(int)parameter.getDefaultValue()));
                break;
        }
        return parameter;
    }

    public SettingsCategory getCategory(SettingsCategory category)
    {
        for(SettingsParameter p : category.getParameterList())
        {
            getParameter(p);
        }
        return category;
    }



}
