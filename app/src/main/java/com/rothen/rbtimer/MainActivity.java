package com.rothen.rbtimer;

import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.rothen.rbtimer.fragments.HoldButtonFragment;
import com.rothen.rbtimer.fragments.TimerFragment;
import com.rothen.rbtimer.utils.Utils;

public class MainActivity extends AppCompatActivity implements HoldButtonFragment.HoldButtonListener {

    private CountDownTimer buttonCountDown;
    private CountDownTimer bombCountDown;

    private TimerFragment timerFragment;
    private HoldButtonFragment holdButtonFragment;

    private static final int BUTTONPRESSTIME = 5*1000;
    private static final int BOMBTIME =  30*1000;

    private boolean isBombArmed;
    private boolean isBombExplosed;

    private void setBombDisarmed()
    {
        isBombArmed = false;
        holdButtonFragment.setBtnHoldText("Arm bomb");
    }
    private void setBombArmed()
    {
        isBombArmed = true;
        holdButtonFragment.setBtnHoldText("Disarm Bomb");
    }

    private void resetBomb()
    {
        setBombDisarmed();
        isBombExplosed = false;
        timerFragment.setTimer(Utils.millisecondToStringTimer(BOMBTIME));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        timerFragment = (TimerFragment)  getSupportFragmentManager().findFragmentById(R.id.fragTimer);
        holdButtonFragment = (HoldButtonFragment) getSupportFragmentManager().findFragmentById(R.id.fragHoldButton);
        resetBomb();
    }

    @Override
    public void holdButtonBeginTouch() {
        if(!isBombExplosed) {
            buttonCountDown = new CountDownTimer(BUTTONPRESSTIME, 100) {
                @Override
                public void onTick(long millisUntilFinished) {
                    holdButtonFragment.setProgressState((int) (100 - (100 * millisUntilFinished / BUTTONPRESSTIME)));
                }

                @Override
                public void onFinish() {
                    holdButtonFragment.setProgressState(0);
                    if (isBombArmed) {
                        bombCountDown.cancel();
                        diplayRemainingTime();
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
        bombCountDown = new CountDownTimer(BOMBTIME,13) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerFragment.setTimer(Utils.millisecondToStringTimer(millisUntilFinished));
            }

            @Override
            public void onFinish() {
                timerFragment.setTimer("BOOM");
                isBombExplosed = true;
                holdButtonFragment.setBtnHoldText("Reset bomb");
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
}
