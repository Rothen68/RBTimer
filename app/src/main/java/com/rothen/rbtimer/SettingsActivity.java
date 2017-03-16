package com.rothen.rbtimer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.rothen.rbtimer.Service.SettingsService;
import com.rothen.rbtimer.utils.Utils;

import java.util.Set;


public class SettingsActivity extends AppCompatActivity {


    private EditText txtBombDuration;
    private EditText txtHoldButtonDuration;
    private EditText txtBipSlowDuration;
    private EditText txtBipFastDuration;
    private Button btnSave;

    private SettingsService settingsService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        settingsService = new SettingsService(this);

        btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckAndSaveSettings();
            }
        });

        txtBombDuration =(EditText) findViewById(R.id.txtBombDuration);
        txtHoldButtonDuration =(EditText) findViewById(R.id.txtHoldButtonDuration);
        txtBipSlowDuration =(EditText) findViewById(R.id.txtSlowBipDuration);
        txtBipFastDuration =(EditText) findViewById(R.id.txtFastBipDuration);

        txtBipFastDuration.setText(settingsService.getBipFastTime()/1000 + "");
        txtBipSlowDuration.setText(settingsService.getBipSlowTime()/1000 + "");
        txtHoldButtonDuration.setText(settingsService.getButtonPressTime()/1000 + "");
        txtBombDuration.setText(settingsService.getBombTime()/1000 + "");

    }

    private void CheckAndSaveSettings() {
        int i = Integer.valueOf(txtBipFastDuration.getText().toString()) * 1000;
        if(i>=SettingsService.BIP_FAST_TIME_MIN && i <=SettingsService.BIP_FAST_TIME_MAX) {
            settingsService.setBipFastTime(i);
        }
        else
        {
            txtBipFastDuration.setText(settingsService.getBipFastTime()/1000 + "");
        }

        i = Integer.valueOf(txtBipSlowDuration.getText().toString())* 1000;
        if(i>=SettingsService.BIP_SLOW_TIME_MIN && i <=SettingsService.BIP_SLOW_TIME_MAX) {
            settingsService.setBipSlowTime(i);
        }
        else
        {
            txtBipSlowDuration.setText(settingsService.getBipSlowTime()/1000 + "");
        }

        i = Integer.valueOf(txtHoldButtonDuration.getText().toString())* 1000;
        if(i>=SettingsService.BUTTON_DEFAULT_TIME_MIN && i <=SettingsService.BUTTON_DEFAULT_TIME_MAX) {
            settingsService.setButtonPressTime(i);
        }
        else
        {
            txtHoldButtonDuration.setText(settingsService.getButtonPressTime()/1000 + "");
        }

        i = Integer.valueOf(txtBombDuration.getText().toString())* 1000;
        if(i>=SettingsService.BOMB_TIME_MIN && i <=SettingsService.BOMB_TIME_MAX) {
            settingsService.setBombTime(i);
        }
        else
        {
            txtBombDuration.setText(settingsService.getBombTime()/1000 + "");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();


    }

}
