package com.rothen.rbtimer;

import android.content.Context;
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
import com.rothen.rbtimer.Service.SoundService;
import com.rothen.rbtimer.fragments.EditTimeFragment;
import com.rothen.rbtimer.fragments.HoldButtonFragment;
import com.rothen.rbtimer.fragments.TimerFragment;
import com.rothen.rbtimer.utils.Utils;

public class MainActivity extends AppCompatActivity implements HoldButtonFragment.HoldButtonListener, EditTimeFragment.EditTimeDialogListener {

    private CountDownTimer buttonCountDown;
    private CountDownTimer bombCountDown;

    private TimerFragment timerFragment;
    private HoldButtonFragment holdButtonFragment;

    private static final int BUTTON_DEFAULT_TIME = 5*1000;
    public static final String BUTTON_PRESS_TIME = "ButtonPressTime";

    private int buttonPressTime;

    private static final int BOMB_DEFAULT_TIME =  30*1000;
    public static final String BOMB_TIME = "BombTime";

    private int bombTime;

    private boolean isBombArmed;
    private boolean isBombExplosed;

    private SensorManagement sensorManagement;
    private SoundService soundService;

    private void setBombDisarmed()
    {
        isBombArmed = false;
        holdButtonFragment.setBtnHoldText("Arm bomb");
        soundService.playSound(R.raw.ct_win,this);
    }
    private void setBombArmed()
    {
        soundService.playSound(R.raw.bomb_planted,this);
        isBombArmed = true;
        holdButtonFragment.setBtnHoldText("Disarm Bomb");
    }

    private void setBombExplosed()
    {
        soundService.playSound(R.raw.terro_win,this);
        timerFragment.setTimer("BOOM");
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
        timerFragment.setTimer(Utils.millisecondToStringTimer(bombTime));
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

        soundService = new SoundService();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
        bombTime = prefs.getInt(BOMB_TIME, BOMB_DEFAULT_TIME);
        buttonPressTime = prefs.getInt(BUTTON_PRESS_TIME,BUTTON_DEFAULT_TIME);
        resetBomb();
        sensorManagement.onResume();
    }

    @Override
    protected void onPause() {
        SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(BOMB_TIME,bombTime);
        editor.putInt(BUTTON_PRESS_TIME,buttonPressTime);
        editor.apply();
        sensorManagement.onPause();
        super.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(!isBombArmed) {
            switch (item.getItemId()) {
                case R.id.mnuBombDuration:
                    displayEditTime(bombTime / 1000, BOMB_TIME);
                    return true;
                case R.id.mnuHoldButtonDuration:
                    displayEditTime(buttonPressTime / 1000, BUTTON_PRESS_TIME);
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
            buttonCountDown = new CountDownTimer(buttonPressTime, 100) {
                @Override
                public void onTick(long millisUntilFinished) {
                    holdButtonFragment.setProgressState((int) (100 - (100 * millisUntilFinished / buttonPressTime)));
                }

                @Override
                public void onFinish() {
                    holdButtonFragment.setProgressState(0);
                    if (isBombArmed) {
                        bombCountDown.cancel();
                        diplayRemainingTime();
                        setBombDisarmed();
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

    private void startBombTimer() {
        bombCountDown = new CountDownTimer(bombTime,13) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerFragment.setTimer(Utils.millisecondToStringTimer(millisUntilFinished));
            }

            @Override
            public void onFinish() {
                setBombExplosed();
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



    //EditTimeDialog
    private void displayEditTime(int currentDuration, String valueType)
    {
        EditTimeFragment frag = new EditTimeFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(EditTimeFragment.OLD_VALUE,currentDuration);
        bundle.putString(EditTimeFragment.VALUE_TYPE, valueType);
        frag.setArguments(bundle);
        frag.show(getSupportFragmentManager(),"EditTime");

    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog,String valueType, int value) {
        if(valueType.equals(BOMB_TIME))
        {
            bombTime = value*1000;
            resetBomb();
        }
        else if(valueType.equals(BUTTON_PRESS_TIME))
        {
            buttonPressTime = value*1000;
        }
    }



}
