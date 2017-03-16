package com.rothen.rbtimer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.rothen.rbtimer.Service.SensorManagement;
import com.rothen.rbtimer.Service.SettingsService;
import com.rothen.rbtimer.Service.SoundService;
import com.rothen.rbtimer.fragments.EditTimeFragment;
import com.rothen.rbtimer.fragments.HoldButtonFragment;
import com.rothen.rbtimer.fragments.TimerFragment;
import com.rothen.rbtimer.utils.Utils;

public class MainActivity extends AppCompatActivity implements HoldButtonFragment.HoldButtonListener {

    private CountDownTimer buttonCountDown;
    private CountDownTimer bombCountDown;

    private TimerFragment timerFragment;
    private HoldButtonFragment holdButtonFragment;

    private SettingsService settingsService;

    private boolean isBombArmed;
    private boolean isBombExplosed;

    private SensorManagement sensorManagement;
    private SoundService soundService;

    private void setBombDisarmed()
    {
        isBombArmed = false;
        holdButtonFragment.setBtnHoldText("Arm bomb");
    }

    private void setBombArmed()
    {
        isBombArmed = true;
        holdButtonFragment.setBtnHoldText("Disarm Bomb");
        soundService.bombPlanted();
    }

    private void setBombExplosed()
    {
        timerFragment.setTimer("BOOM");
        soundService.bombExplose();
        isBombExplosed = true;
        holdButtonFragment.setBtnHoldText("Reset bomb");
        if(bombCountDown!=null) {
            bombCountDown.cancel();
        }

    }

    private void resetBomb()
    {
        isBombArmed = false;
        holdButtonFragment.setBtnHoldText("Arm bomb");
        isBombExplosed = false;
        timerFragment.setTimer(Utils.millisecondToStringTimer(settingsService.getBombTime()));
    }

    private void terroWins()
    {
        soundService.terroWins();
    }

    private void ctWins()
    {
        soundService.ctWins();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        timerFragment = (TimerFragment)  getSupportFragmentManager().findFragmentById(R.id.fragTimer);
        holdButtonFragment = (HoldButtonFragment) getSupportFragmentManager().findFragmentById(R.id.fragHoldButton);

        sensorManagement = new SensorManagement(this, new SensorManagement.SensorListener() {
            @Override
            public void onStrongMouvement() {
                setBombExplosed();
                if(isBombArmed)
                {
                    terroWins();
                }
                else
                {
                    ctWins();
                }
                holdButtonFragment.onChangeColor(getResources().getColor(R.color.colorDefault));
            }

            @Override
            public void onWeakMouvement() {
                holdButtonFragment.onChangeColor(getResources().getColor(R.color.colorDanger));
            }

            @Override
            public void onNonSignificantMovement() {
            }
        });

        soundService = new SoundService(this);
        settingsService = new SettingsService(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        resetBomb();
        sensorManagement.onResume();
    }

    @Override
    protected void onPause() {
        sensorManagement.onPause();
        super.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(!isBombArmed) {
            switch (item.getItemId()) {
                case R.id.mnuSettings:
                    Intent i = new Intent(this, SettingsActivity.class);
                    startActivity(i);
                    return true;

                default:
                    return super.onOptionsItemSelected(item);
            }
        }
        return super.onOptionsItemSelected(item);
    }


    //HoldButtonFragment.HoldButtonListener
    @Override
    public void holdButtonBeginTouch() {
        if(!isBombExplosed) {
            buttonCountDown = new CountDownTimer(settingsService.getButtonPressTime(), 100) {
                @Override
                public void onTick(long millisUntilFinished) {
                    holdButtonFragment.setProgressState((int) (100 - (100 * millisUntilFinished / settingsService.getButtonPressTime())));

                }

                @Override
                public void onFinish() {
                    holdButtonFragment.setProgressState(0);
                    if (isBombArmed) {
                        bombCountDown.cancel();
                        diplayRemainingTime();
                        setBombDisarmed();
                        ctWins();
                        resetBomb();
                    } else {
                        setBombArmed();
                        startBombTimer();
                    }

                }
            }.start();
        }
        else
        {
            resetBomb();
        }
    }

    private void diplayRemainingTime()
    {
        new AlertDialog.Builder(this)
                .setMessage("Remaining time : \n" +  timerFragment.getTimer())
                .setTitle("Bomb disarmed ! ").show();
    }

    private long second;
    private void startBombTimer() {
        second = settingsService.getBombTime();
        bombCountDown = new CountDownTimer(settingsService.getBombTime(),10) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerFragment.setTimer(Utils.millisecondToStringTimer(millisUntilFinished));

                if(millisUntilFinished< settingsService.getBipFastTime() )
                {
                    if(millisUntilFinished+1000<second) {
                        soundService.makeBip();
                        second = millisUntilFinished;
                    }
                }
                else if (millisUntilFinished<settingsService.getBipSlowTime())

                    if(millisUntilFinished + 2000 <second) {
                        soundService.makeBip();
                        second = millisUntilFinished;
                    }
                }


            @Override
            public void onFinish() {
                setBombExplosed();

                terroWins();

            }
        }.start();
    }



    @Override
    public void holdButtonEndTouch() {
        if(buttonCountDown != null)
        {
            buttonCountDown.cancel();
        }

        holdButtonFragment.setProgressState(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return true;
    }





}
