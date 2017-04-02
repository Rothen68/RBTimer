package com.rothen.rbtimer.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apest on 02/04/2017.
 */

public class SettingsCategory {

    private String categoryTitle;
    private String categoryId;
    private List<SettingsParameter> parameterList;

    public SettingsCategory() {
        parameterList = new ArrayList<SettingsParameter>();
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }

    public void addParameter(SettingsParameter parameter)
    {
        parameterList.add(parameter);
    }


    public List<SettingsParameter> getParameterList() {
        return parameterList;
    }

    public void setParameterList(List<SettingsParameter> parameterList) {
        this.parameterList = parameterList;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
}
