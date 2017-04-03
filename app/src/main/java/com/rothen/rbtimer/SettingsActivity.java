package com.rothen.rbtimer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.rothen.rbtimer.model.BombSettingsParameters;
import com.rothen.rbtimer.model.IActivitySettingsParameters;
import com.rothen.rbtimer.model.SettingsCategory;
import com.rothen.rbtimer.model.SettingsParameter;
import com.rothen.rbtimer.model.SettingsParameterType;
import com.rothen.rbtimer.service.ParametersService;
import com.rothen.rbtimer.service.SettingsService;

import java.util.HashMap;
import java.util.List;


public class SettingsActivity extends AppCompatActivity {
    public static final String SETTINGS_ACTIVITY_TYPE = "SETTINGS_ACTIVITY_TYPE";

    private IActivitySettingsParameters settingsParameters;
    private SettingsService settingsService;
    private ParametersService settings;

    private HashMap<String,EditText> editTextHashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        String activity = getIntent().getExtras().getString(SETTINGS_ACTIVITY_TYPE);

        settingsService = new SettingsService(this);

        if(activity.equals(BombPlantActivity.BOMBPLANT_ACTIVITY))
        {
            settingsParameters = new BombSettingsParameters(this);
        }
        else
        {
            throw new IllegalArgumentException(SETTINGS_ACTIVITY_TYPE + " is not set in intent");
        }
        settings = new ParametersService(this,settingsParameters);

        LinearLayout llSettings = (LinearLayout) findViewById(R.id.llSettings);
        editTextHashMap = new HashMap<>();


        for(SettingsCategory c : settings.getListCategories())
        {
            TextView categoryTitle = (TextView)getLayoutInflater().inflate(R.layout.template_category_textview,null).findViewById(R.id.templateCategoryTextView);
            categoryTitle.setText(c.getCategoryTitle());
            llSettings.addView(categoryTitle);

            for(SettingsParameter p : c.getParameterList())
            {
                LinearLayout llParameters = (LinearLayout) getLayoutInflater().inflate(R.layout.template_linearlayout_parameter,null).findViewById(R.id.templateParametersLineraLayout);
                TextView parameterTitle = (TextView) getLayoutInflater().inflate(R.layout.template_parameter_textview,null).findViewById(R.id.template_ParameterTextView);
                parameterTitle.setText(p.getTitle());
                EditText parameterValue = (EditText) getLayoutInflater().inflate(R.layout.template_parameter_edittext,null).findViewById(R.id.templateParametersEditText);

                switch (p.getParameterType())
                {
                    case TYPE_BOOLEAN:
                        if((boolean)p.getValue())
                        {
                            parameterValue.setText(1 +"");
                        }
                        else
                        {
                            parameterValue.setText(0 +"");
                        }
                        parameterValue.setInputType(InputType.TYPE_CLASS_NUMBER);
                        break;
                    case TYPE_STRING:
                        parameterValue.setText(p.getValue() +"" );
                        parameterValue.setInputType(InputType.TYPE_CLASS_TEXT);
                        break;
                    case TYPE_FLOAT:
                    case TYPE_INT:
                        parameterValue.setText(p.getValue() +"" );
                        parameterValue.setInputType(InputType.TYPE_CLASS_NUMBER);
                        break;
                }
                editTextHashMap.put(c.getCategoryId()+p.getKey(),parameterValue);
                llParameters.addView(parameterTitle,new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT,1.0f));
                llParameters.addView(parameterValue,new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT,3.0f));
                llSettings.addView(llParameters);
            }

            Button btnOk = (Button) findViewById(R.id.settings_btnOk);
            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<SettingsCategory> list = settings.getListCategories();
                    for(SettingsCategory c : list)
                    {
                        for(SettingsParameter p : c.getParameterList())
                        {
                            EditText etxt = editTextHashMap.get(c.getCategoryId()+p.getKey());
                            switch (p.getParameterType())
                            {
                                case TYPE_BOOLEAN:
                                    if(Integer.valueOf(etxt.getText().toString())>0)
                                    {
                                        p.setValue(true);
                                    }
                                    else
                                    {
                                        p.setValue(false);
                                    }
                                    break;

                                case TYPE_FLOAT:
                                    p.setValue(Float.valueOf(etxt.getText().toString()));
                                    break;
                                case TYPE_INT:
                                    p.setValue(Integer.valueOf(etxt.getText().toString()));
                                    break;
                                case TYPE_STRING:
                                    p.setValue(etxt.getText().toString());
                                    break;
                            }
                        }
                    }
                    settings.saveListCategories(list);
                    finish();
                }
            });
        }

    }
}
