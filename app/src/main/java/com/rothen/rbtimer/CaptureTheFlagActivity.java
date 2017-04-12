package com.rothen.rbtimer;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.rothen.rbtimer.fragments.CaptureTheFlagChronoProgressFragment;
import com.rothen.rbtimer.fragments.TimerFragment;
import com.rothen.rbtimer.model.BombSettingsParameters;
import com.rothen.rbtimer.model.CaptureTheFlagSettingsParameters;
import com.rothen.rbtimer.service.ParametersService;
import com.rothen.rbtimer.service.SoundService;
import com.rothen.rbtimer.utils.Utils;


public class CaptureTheFlagActivity extends AppCompatActivity implements Button.OnTouchListener {

    private CaptureTheFlagChronoProgressFragment fragProgress;
    private TimerFragment fragTimer;

    public static final String CAPTURETHEFLAG_ACTIVITY = "CAPTURETHEFLAG_ACTIVITY";

    private double MAX_BUZZ_FREQUENCY = 1000;
    private double MIN_BUZZ_FREQUENCY = 200;
    private int BUZZ_DURATION = 800;

    private Button btnRedTeam;
    private Button btnBlueTeam;

    private CountDownTimer gameCountDown;
    private CountDownTimer captureCountDown;
    private CountDownTimer buttonPressCountDown;
    private int buttonPressedId;

    private SoundService soundService;
    private ParametersService parametersService;

    private  int gameTime;
    private int captureTime;
    private int buttonPressionTime;

    private long currentCaptureTime;

    private int currentTeamCapture;

    private boolean gameStarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_the_flag);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        soundService = new SoundService(this);
        parametersService = new ParametersService(this, new BombSettingsParameters(this));

        fragProgress = (CaptureTheFlagChronoProgressFragment) getSupportFragmentManager().findFragmentById(R.id.fragProgress);
        fragTimer = (TimerFragment) getSupportFragmentManager().findFragmentById(R.id.fragTimer);
        btnRedTeam = (Button) findViewById(R.id.btnRedTeam);
        btnBlueTeam = (Button) findViewById(R.id.btnBlueTeam);

        btnBlueTeam.setOnTouchListener(this);
        btnRedTeam.setOnTouchListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        parametersService = new ParametersService(this, new CaptureTheFlagSettingsParameters(this));
        gameTime = parametersService.getInt(
                CaptureTheFlagSettingsParameters.CAT_TIMER,
                CaptureTheFlagSettingsParameters.PAR_TIMER_GAMETIME
        );
        captureTime= parametersService.getInt(
                CaptureTheFlagSettingsParameters.CAT_TIMER,
                CaptureTheFlagSettingsParameters.PAR_TIMER_CAPTURETIME
        );

                buttonPressionTime= parametersService.getInt(
                        CaptureTheFlagSettingsParameters.CAT_TIMER,
                        CaptureTheFlagSettingsParameters.PAR_TIMER_BUTTONPRESSTIME
                );
        currentCaptureTime = currentTeamCapture = 0;
        gameStarted =false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_start_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (!gameStarted) {
            switch (item.getItemId()) {
                case R.id.mnuSettings:
                    Intent i = new Intent(this, SettingsActivity.class);
                    i.putExtra(SettingsActivity.SETTINGS_ACTIVITY_TYPE, CAPTURETHEFLAG_ACTIVITY);
                    startActivity(i);
                    return true;
                case R.id.mnuStart:
                    startGame();
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void startGame() {
        gameStarted = true;
        gameCountDown = new CountDownTimer(gameTime * 1000, 10) {
            @Override
            public void onTick(long millisUntilFinished) {
                fragTimer.setTimer(Utils.millisecondToStringTimer(millisUntilFinished));
            }
            @Override
            public void onFinish() {
                noWins();
            }
        }.start();
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if(gameStarted) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                holdButtonBeginTouch(view);
                return true;
            } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                holdButtonEndTouch(view);
                return true;
            }
        }
        return false;
    }



    private void holdButtonEndTouch(View view) {
        if(buttonPressCountDown!=null)
        {
            buttonPressCountDown.cancel();
        }
        blockButton(currentTeamCapture);
    }

    private void holdButtonBeginTouch(View view) {
        buttonPressedId = view.getId();
        blockButton(buttonPressedId);

        buttonPressCountDown = new CountDownTimer(buttonPressionTime * 1000,300) {
            @Override
            public void onTick(long l) {
                soundService.makeBip();
            }

            @Override
            public void onFinish() {
                buttonHoldFinish(buttonPressedId);
            }
        }.start();
    }

    private void buttonHoldFinish(int buttonPressedId) {
        switch (buttonPressedId)
        {
            case R.id.btnRedTeam:
                redTeamClick();
                break;
            case R.id.btnBlueTeam:
                blueTeamClick();
                break;
            default:
                break;
        }
    }

    private void redTeamClick() {
        currentTeamCapture = R.id.btnRedTeam;
        blockButton(R.id.btnBlueTeam);
        if(captureCountDown==null)
        {
            launchCaptureCountdown();
        }
    }

    private void blueTeamClick() {
        currentTeamCapture = R.id.btnBlueTeam;
        blockButton(R.id.btnRedTeam);
        if(captureCountDown==null)
        {
            launchCaptureCountdown();
        }
    }

    private void launchCaptureCountdown()
    {
        if(captureCountDown==null) {
            captureCountDown = new CountDownTimer(gameTime * 1000, 700) {
                @Override
                public void onTick(long l) {

                    if (currentTeamCapture == R.id.btnBlueTeam) {
                        currentCaptureTime -= 700;
                    } else {
                        currentCaptureTime += 700;
                    }
                    int progressPercentage = (int) (100* currentCaptureTime / (captureTime * 1000));
                    soundService.makeBuzz(MIN_BUZZ_FREQUENCY + ((MAX_BUZZ_FREQUENCY - MIN_BUZZ_FREQUENCY) * Math.abs(progressPercentage) / 100), BUZZ_DURATION);
                    fragProgress.setProgress(progressPercentage);
                    if (Math.abs(currentCaptureTime) > captureTime * 1000) {
                        captureTimeFinish();
                    }

                }

                @Override
                public void onFinish() {

                }
            }.start();
        }
    }

    private void captureTimeFinish() {

        if(currentTeamCapture == R.id.btnBlueTeam)
        {
            blueTeamWins();
        }
        else {
            redTeamWins();
        }

    }

    private void blueTeamWins() {
        fragTimer.setTimer("Blue Team Wins");
        soundService.bombExplose();
        resetParty();
    }



    private void redTeamWins() {
        fragTimer.setTimer("Red Team Wins");
        soundService.bombExplose();
        resetParty();

    }

    private void noWins()
    {
        fragTimer.setTimer("No one Wins");
        soundService.bombExplose();
        resetParty();
    }

    private void resetParty() {
        if(captureCountDown!=null)
        {
            captureCountDown.cancel();

            captureCountDown = null;
        }
        if(gameCountDown !=null) {
            gameCountDown.cancel();
            gameCountDown = null;
        }
        currentCaptureTime = currentTeamCapture = 0;
        fragProgress.setProgress(0);
        blockButton(0);
        gameStarted = false;
    }

    private void blockButton(int buttonPressedId) {
        switch (buttonPressedId)
        {
            case R.id.btnRedTeam:
                btnBlueTeam.setEnabled(false);
                btnRedTeam.setEnabled(true);
                break;
            case R.id.btnBlueTeam:
                btnRedTeam.setEnabled(false);
                btnBlueTeam.setEnabled(true);
                break;
            default:
                btnRedTeam.setEnabled(true);
                btnBlueTeam.setEnabled(true);
                break;
        }
    }
}
