package com.rothen.rbtimer;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.rothen.rbtimer.fragments.CaptureTheFlagChronoProgressFragment;
import com.rothen.rbtimer.fragments.TimerFragment;
import com.rothen.rbtimer.model.BombSettingsParameters;
import com.rothen.rbtimer.service.ParametersService;
import com.rothen.rbtimer.service.SoundService;
import com.rothen.rbtimer.utils.Utils;


public class CaptureTheFlagActivity extends AppCompatActivity {

    private CaptureTheFlagChronoProgressFragment fragProgress;
    private TimerFragment fragTimer;

    private Button btnRedTeam;
    private Button btnBlueTeam;

    private CountDownTimer partyCountDown;
    private CountDownTimer captureCountDown;

    private SoundService soundService;
    private ParametersService parametersService;

    private  int PARTYTIME = 20;
    private int CAPTURETIME = 4;

    private long second;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_the_flag);

        soundService = new SoundService(this);
        parametersService = new ParametersService(this, new BombSettingsParameters(this));

        fragProgress = (CaptureTheFlagChronoProgressFragment) getSupportFragmentManager().findFragmentById(R.id.fragProgress);
        fragTimer = (TimerFragment) getSupportFragmentManager().findFragmentById(R.id.fragTimer);
        btnRedTeam = (Button) findViewById(R.id.btnRedTeam);
        btnBlueTeam = (Button) findViewById(R.id.btnBlueTeam);

        second = parametersService.getInt(BombSettingsParameters.CAT_BOMB, BombSettingsParameters.PAR_BOMB_DURATION) * 1000;
        partyCountDown = new CountDownTimer(parametersService.getInt(BombSettingsParameters.CAT_BOMB, BombSettingsParameters.PAR_BOMB_DURATION) * 1000, 10) {
            @Override
            public void onTick(long millisUntilFinished) {
                fragTimer.setTimer(Utils.millisecondToStringTimer(millisUntilFinished));

                if (millisUntilFinished < parametersService.getInt(BombSettingsParameters.CAT_BOMB, BombSettingsParameters.PAR_BOMB_FAST_BIP_DURATION) * 1000) {
                    if (millisUntilFinished + 1000 < second) {
                        soundService.makeBip();
                        second = millisUntilFinished;
                    }
                } else if (millisUntilFinished < parametersService.getInt(BombSettingsParameters.CAT_BOMB, BombSettingsParameters.PAR_BOMB_SLOW_BIP_DURATION) * 1000)

                    if (millisUntilFinished + 2000 < second) {
                        soundService.makeBip();
                        second = millisUntilFinished;
                    }
            }


            @Override
            public void onFinish() {


            }
        }.start();
    }
}
