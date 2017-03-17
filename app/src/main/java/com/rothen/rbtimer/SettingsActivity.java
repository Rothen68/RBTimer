package com.rothen.rbtimer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.rothen.rbtimer.Service.NFCService;
import com.rothen.rbtimer.Service.SettingsService;


public class SettingsActivity extends AppCompatActivity {


    private EditText txtBombDuration;
    private EditText txtHoldButtonDuration;
    private EditText txtBipSlowDuration;
    private EditText txtBipFastDuration;

    private TextView txtNFCAvailable;
    private TextView txtNFCId;

    private Button btnSave;

    private SettingsService settingsService;
    private NFCService nfcService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        settingsService = new SettingsService(this);
        nfcService = new NFCService(this, new NFCService.NFCAdapterListener() {
            @Override
            public void onTagConnectionLost() {
                txtNFCId.setText("");
            }
        });

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
        txtNFCAvailable = (TextView) findViewById(R.id.txtNFCAvailable);
        txtNFCId = (TextView)findViewById(R.id.txtNFCId);

        txtBipFastDuration.setText(settingsService.getBipFastTime()/1000 + "");
        txtBipSlowDuration.setText(settingsService.getBipSlowTime()/1000 + "");
        txtHoldButtonDuration.setText(settingsService.getButtonPressTime()/1000 + "");
        txtBombDuration.setText(settingsService.getBombTime()/1000 + "");

        if(nfcService.isNFCAvailable())
        {
            if(nfcService.isNFCEnabled())
            {
                txtNFCAvailable.setText(R.string.settings_NFCAvailableAndEnabled);
                txtNFCAvailable.setTextColor(getResources().getColor(R.color.colorDefault));
            }
            else
            {
                txtNFCAvailable.setText(R.string.settings_NFCAvailableNotEnabled);
                txtNFCAvailable.setTextColor(getResources().getColor(R.color.colorWarning));
            }
        }
        else
        {
            txtNFCAvailable.setText(R.string.settings_NFCNotAvailable);
            txtNFCAvailable.setTextColor(getResources().getColor(R.color.colorDanger));
        }
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
    protected void onPause() {
        super.onPause();
        nfcService.unRegisterIntentFilter(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        nfcService.registerIntentFilter(SettingsActivity.class, this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        txtNFCId.setText(nfcService.lookupTag(intent));
    }
}
