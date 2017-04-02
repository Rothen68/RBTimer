package com.rothen.rbtimer.model;

/**
 * Created by apest on 02/04/2017.
 */

public class SettingsParameter {
    private String title;
    private String key;
    private Object value;
    private Object defaultValue;
    private SettingsParameterType parameterType;

    public SettingsParameter(String title, String key, Object value, Object defaultValue, SettingsParameterType parameterType) {
        this.title = title;
        this.key = key;
        this.value = value;
        this.defaultValue = defaultValue;
        this.parameterType = parameterType;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Object getValue() {
        return value;
    }

    public Object getDefaultValue()
    {
        return defaultValue;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public SettingsParameterType getParameterType() {
        return parameterType;
    }

    public void setParameterType(SettingsParameterType parameterType) {
        this.parameterType = parameterType;
    }
}


