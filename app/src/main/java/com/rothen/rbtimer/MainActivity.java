package com.rothen.rbtimer;

import android.app.Activity;
import android.content.Intent;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.rothen.rbtimer.model.IActivitySettingsParameters;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button bombPlant = (Button) findViewById(R.id.btnBombPlantActivity);
        bombPlant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startBombPlantActivity();
            }
        });

        Button captureTheFlaq = (Button) findViewById(R.id.btnCaptureTheFlagActivity);
        captureTheFlaq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startCaptureTheFlagActivity();
            }
        });
    }

    private void startCaptureTheFlagActivity() {
        Intent i = new Intent(this,CaptureTheFlagActivity.class);
        startActivity(i);
    }
    private void startBombPlantActivity() {
        Intent i = new Intent(this,BombPlantActivity.class);
        startActivity(i);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

}
