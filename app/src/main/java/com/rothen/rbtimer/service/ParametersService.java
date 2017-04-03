package com.rothen.rbtimer.service;

import android.content.Context;

import com.rothen.rbtimer.model.IActivitySettingsParameters;
import com.rothen.rbtimer.model.SettingsCategory;
import com.rothen.rbtimer.model.SettingsParameter;
import com.rothen.rbtimer.model.SettingsParameterType;

import java.util.List;

/**
 * Created by apest on 02/04/2017.
 */

public class ParametersService extends SettingsService {

    private List<SettingsCategory> settingsParameters;

    public ParametersService(Context context, IActivitySettingsParameters settingsParameters) {
        super(context);
        this.settingsParameters = getListCategories(settingsParameters.getCategories());

    }

    public List<SettingsCategory> getListCategories()
    {
        return settingsParameters;
    }

    public int getInt(String categoryId, String parameterId)
    {
        for(SettingsCategory c : settingsParameters)
        {
            if(c.getCategoryId().equals(categoryId))
            {
                for(SettingsParameter p : c.getParameterList())
                {
                    if(p.getKey().equals(parameterId))
                    {
                        if(p.getParameterType()== SettingsParameterType.TYPE_INT)
                        {
                            return (int) p.getValue();
                        }
                        throw new IllegalArgumentException(categoryId + " " + parameterId + " is not an int");
                    }
                }
            }
        }
        throw new IllegalArgumentException(categoryId + " " + parameterId + " not found");
    }

    public boolean getBoolean(String categoryId, String parameterId)
    {
        for(SettingsCategory c : settingsParameters)
        {
            if(c.getCategoryId().equals(categoryId))
            {
                for(SettingsParameter p : c.getParameterList())
                {
                    if(p.getKey().equals(parameterId))
                    {
                        if(p.getParameterType()== SettingsParameterType.TYPE_BOOLEAN)
                        {
                            return (boolean) p.getValue();
                        }
                        throw new IllegalArgumentException(categoryId + " " + parameterId + " is not an boolean");
                    }
                }
            }
        }
        throw new IllegalArgumentException(categoryId + " " + parameterId + " not found");
    }

    public float getFloat(String categoryId, String parameterId)
    {
        for(SettingsCategory c : settingsParameters)
        {
            if(c.getCategoryId().equals(categoryId))
            {
                for(SettingsParameter p : c.getParameterList())
                {
                    if(p.getKey().equals(parameterId))
                    {
                        if(p.getParameterType()== SettingsParameterType.TYPE_FLOAT)
                        {
                            return (float) p.getValue();
                        }
                        throw new IllegalArgumentException(categoryId + " " + parameterId + " is not an flaot");
                    }
                }
            }
        }
        throw new IllegalArgumentException(categoryId + " " + parameterId + " not found");
    }

    public String getString(String categoryId, String parameterId)
    {
        for(SettingsCategory c : settingsParameters)
        {
            if(c.getCategoryId().equals(categoryId))
            {
                for(SettingsParameter p : c.getParameterList())
                {
                    if(p.getKey().equals(parameterId))
                    {
                        if(p.getParameterType()== SettingsParameterType.TYPE_STRING)
                        {
                            return (String) p.getValue();
                        }
                        throw new IllegalArgumentException(categoryId + " " + parameterId + " is not an string");
                    }
                }
            }
        }
        throw new IllegalArgumentException(categoryId + " " + parameterId + " not found");
    }

    public void setInt(String categoryId, String parameterId, int value)
    {
        for(SettingsCategory c : settingsParameters)
        {
            if(c.getCategoryId().equals(categoryId))
            {
                for(SettingsParameter p : c.getParameterList())
                {
                    if(p.getKey().equals(parameterId))
                    {
                        if(p.getParameterType()== SettingsParameterType.TYPE_INT)
                        {
                            p.setValue(value);
                        }
                        throw new IllegalArgumentException(categoryId + " " + parameterId + " is not an int");
                    }
                }
            }
        }
        throw new IllegalArgumentException(categoryId + " " + parameterId + " not found");
    }

    public void setBoolean(String categoryId, String parameterId, Boolean value)
    {
        for(SettingsCategory c : settingsParameters)
        {
            if(c.getCategoryId().equals(categoryId))
            {
                for(SettingsParameter p : c.getParameterList())
                {
                    if(p.getKey().equals(parameterId))
                    {
                        if(p.getParameterType()== SettingsParameterType.TYPE_BOOLEAN)
                        {
                           p.setValue(value);
                        }
                        throw new IllegalArgumentException(categoryId + " " + parameterId + " is not an boolean");
                    }
                }
            }
        }
        throw new IllegalArgumentException(categoryId + " " + parameterId + " not found");
    }

    public void setFloat(String categoryId, String parameterId,float value)
    {
        for(SettingsCategory c : settingsParameters)
        {
            if(c.getCategoryId().equals(categoryId))
            {
                for(SettingsParameter p : c.getParameterList())
                {
                    if(p.getKey().equals(parameterId))
                    {
                        if(p.getParameterType()== SettingsParameterType.TYPE_FLOAT)
                        {
                            p.setValue(value);
                        }
                        throw new IllegalArgumentException(categoryId + " " + parameterId + " is not an flaot");
                    }
                }
            }
        }
        throw new IllegalArgumentException(categoryId + " " + parameterId + " not found");
    }
    public void setString(String categoryId, String parameterId,String value)
    {
        for(SettingsCategory c : settingsParameters)
        {
            if(c.getCategoryId().equals(categoryId))
            {
                for(SettingsParameter p : c.getParameterList())
                {
                    if(p.getKey().equals(parameterId))
                    {
                        if(p.getParameterType()== SettingsParameterType.TYPE_STRING)
                        {
                            p.setValue(value);
                        }
                        throw new IllegalArgumentException(categoryId + " " + parameterId + " is not a String");
                    }
                }
            }
        }
        throw new IllegalArgumentException(categoryId + " " + parameterId + " not found");
    }
}
