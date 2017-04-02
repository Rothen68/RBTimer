package com.rothen.rbtimer;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PersistableBundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.rothen.rbtimer.model.BombSettingsParameters;
import com.rothen.rbtimer.model.SettingsCategory;
import com.rothen.rbtimer.service.ParametersService;
import com.rothen.rbtimer.service.SensorManagement;
import com.rothen.rbtimer.service.SoundService;
import com.rothen.rbtimer.fragments.HoldButtonFragment;
import com.rothen.rbtimer.fragments.TimerFragment;
import com.rothen.rbtimer.utils.Utils;

/**
 * Manage Bomb planted by terro and disarmed by CT
 * Created by apest on 24/03/2017.
 */

public class BombPlantActivity extends AppCompatActivity implements HoldButtonFragment.HoldButtonListener {

    public static final String BOMBPLANT_ACTIVITY = "BOMBPLANT_ACTIVITY";

    private double MIN_ACCELERATION_LEVEL = 1;

    private double BOMB_EXPLODE_LEVEL = 9;

    private double MAX_BUZZ_FREQUENCY = 6000;
    private double MIN_BUZZ_FREQUENCY = 200;
    private int BUZZ_DURATION = 100;

    private CountDownTimer buttonCountDown;
    private CountDownTimer bombCountDown;

    private TimerFragment timerFragment;
    private HoldButtonFragment holdButtonFragment;

    private ParametersService parametersService;

    private boolean isBombArmed;
    private boolean isBombExplosed;

    private SensorManagement sensorManagement;
    private SoundService soundService;


    private void setBombDisarmed() {
        isBombArmed = false;
        holdButtonFragment.setBtnHoldText("Arm bomb");
    }

    private void setBombArmed() {
        isBombArmed = true;
        holdButtonFragment.setBtnHoldText("Disarm Bomb");
        soundService.bombPlanted();
    }

    private void setBombExplosed() {
        timerFragment.setTimer("BOOM");
        soundService.bombExplose();
        isBombExplosed = true;
        holdButtonFragment.setBtnHoldText("Reset bomb");
        if (bombCountDown != null) {
            bombCountDown.cancel();
        }
    }

    private void resetBomb() {
        isBombArmed = false;
        holdButtonFragment.setBtnHoldText("Arm bomb");
        isBombExplosed = false;
        timerFragment.setTimer(Utils.millisecondToStringTimer(parametersService.getInt(BombSettingsParameters.CAT_BOMB, BombSettingsParameters.PAR_BOMB_DURATION) * 1000));
    }

    private void terroWins() {
        soundService.terroWins();
    }

    private void ctWins() {
        soundService.ctWins();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bomb_plant);
        timerFragment = (TimerFragment) getSupportFragmentManager().findFragmentById(R.id.fragTimer);
        holdButtonFragment = (HoldButtonFragment) getSupportFragmentManager().findFragmentById(R.id.fragHoldButton);

        sensorManagement = new SensorManagement(this, new SensorManagement.SensorListener() {

            CountDownTimer timer;

            void resetTimer() {
                timer = null;
            }

            @Override
            public void onMouvement(double intencityPercentage) {
                if (timer == null) {
                    if (intencityPercentage >= 100) {
                        setBombExplosed();
                        if (isBombArmed) {
                            terroWins();
                        } else {
                            ctWins();
                        }
                    } else {
                        if (intencityPercentage < 50) {
                            holdButtonFragment.setPercentage(intencityPercentage * 2);
                        } else {
                            holdButtonFragment.setPercentage(100);
                        }

                        soundService.makeBuzz(MIN_BUZZ_FREQUENCY + ((MAX_BUZZ_FREQUENCY - MIN_BUZZ_FREQUENCY) * intencityPercentage / 100), BUZZ_DURATION);
                    }
                    timer = new CountDownTimer(BUZZ_DURATION * 2, BUZZ_DURATION) {
                        @Override
                        public void onTick(long millisUntilFinished) {

                        }

                        @Override
                        public void onFinish() {
                            resetTimer();
                        }
                    }.start();
                }
            }

            @Override
            public void onNonSignificantMovement() {
            }
        },
                BOMB_EXPLODE_LEVEL,
                MIN_ACCELERATION_LEVEL);

        soundService = new SoundService(this);

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        parametersService = new ParametersService(this, new BombSettingsParameters(this));
        resetBomb();
        sensorManagement.onResume();

    }

    @Override
    protected void onPause() {
        sensorManagement.onPause();
        if (buttonCountDown != null) buttonCountDown.cancel();
        if (bombCountDown != null) bombCountDown.cancel();
        super.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (!isBombArmed) {
            switch (item.getItemId()) {
                case R.id.mnuSettings:
                    Intent i = new Intent(this, SettingsActivity.class);
                    i.putExtra(SettingsActivity.SETTINGS_ACTIVITY_TYPE,BOMBPLANT_ACTIVITY);
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
        if (!isBombExplosed) {
            if (isBombArmed) {
                final int disarmDuration = parametersService.getInt(BombSettingsParameters.CAT_BOMB, BombSettingsParameters.PAR_BOMB_DISARM_DURATION) * 1000;
                buttonCountDown = new CountDownTimer(disarmDuration, 100) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        holdButtonFragment.setProgressState((int) (100 - (100 * millisUntilFinished / disarmDuration)));
                    }

                    @Override
                    public void onFinish() {
                        holdButtonFragment.setProgressState(0);

                        bombCountDown.cancel();
                        diplayRemainingTime();
                        setBombDisarmed();
                        ctWins();
                        resetBomb();

                    }
                }.start();
            } else {
                final int armDuration = parametersService.getInt(BombSettingsParameters.CAT_BOMB, BombSettingsParameters.PAR_BOMB_ARM_DURATION) * 1000;
                buttonCountDown = new CountDownTimer(armDuration, 100) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        holdButtonFragment.setProgressState((int) (100 - (100 * millisUntilFinished / armDuration)));
                    }

                    @Override
                    public void onFinish() {
                        holdButtonFragment.setProgressState(0);

                        setBombArmed();
                        startBombTimer();

                    }
                }.start();
            }
        } else {
            resetBomb();
        }
    }

    private void diplayRemainingTime() {
        new AlertDialog.Builder(this)
                .setMessage("Remaining time : \n" + timerFragment.getTimer())
                .setTitle("Bomb disarmed ! ").show();
    }

    private long second;

    private void startBombTimer() {
        second = parametersService.getInt(BombSettingsParameters.CAT_BOMB, BombSettingsParameters.PAR_BOMB_DURATION)*1000;
        bombCountDown = new CountDownTimer(parametersService.getInt(BombSettingsParameters.CAT_BOMB, BombSettingsParameters.PAR_BOMB_DURATION)*1000, 10) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerFragment.setTimer(Utils.millisecondToStringTimer(millisUntilFinished));

                if (millisUntilFinished < parametersService.getInt(BombSettingsParameters.CAT_BOMB, BombSettingsParameters.PAR_BOMB_FAST_BIP_DURATION)*1000) {
                    if (millisUntilFinished + 1000 < second) {
                        soundService.makeBip();
                        second = millisUntilFinished;
                    }
                } else if (millisUntilFinished < parametersService.getInt(BombSettingsParameters.CAT_BOMB, BombSettingsParameters.PAR_BOMB_SLOW_BIP_DURATION)*1000)

                    if (millisUntilFinished + 2000 < second) {
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
        if (buttonCountDown != null) {
            buttonCountDown.cancel();
        }

        holdButtonFragment.setProgressState(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }


}
