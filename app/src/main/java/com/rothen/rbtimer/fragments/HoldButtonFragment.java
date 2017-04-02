package com.rothen.rbtimer.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.rothen.rbtimer.R;

/**
 * Created by apest on 14/03/2017.
 */

public class HoldButtonFragment extends Fragment {

    public static final int BACK_TO_NORMAL_DURATION = 3 * 1000;

    public interface HoldButtonListener
    {
        public void holdButtonBeginTouch();
        public void holdButtonEndTouch();
    }

    private HoldButtonListener listener;
    private Button btnHold;
    private ProgressBar pgrProgress;

    private CountDownTimer timer;
    private double currentR;
    private double currentG;
    private double currentB;

    private double targetR;
    private double targetG;
    private double targetB;

    private double deltaR;
    private double deltaG;
    private double deltaB;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            listener = (HoldButtonListener) context;

            targetR = Color.red(getResources().getColor(R.color.colorDefault));
            targetG = Color.green(getResources().getColor(R.color.colorDefault));
            targetB = Color.blue(getResources().getColor(R.color.colorDefault));
        }
        catch(Exception e)
        {
            throw new ClassCastException(context.toString() + " does not implement HoldButtonListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.hold_button_frag,container);
        btnHold = (Button) v.findViewById(R.id.btnHoldButton);
        btnHold.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    listener.holdButtonBeginTouch();
                    return true;
                }
                else if(event.getAction() == MotionEvent.ACTION_UP)
                {
                    listener.holdButtonEndTouch();
                    return true;
                }
                return false;
            }
        });
        pgrProgress = (ProgressBar) v.findViewById(R.id.pgrProgress);
        btnHold.setBackgroundColor(getResources().getColor(R.color.colorDefault));
        return v;
    }

    public void setProgressState(int progressState)
    {
        if(pgrProgress != null) {
            pgrProgress.setProgress(progressState);
        }
    }

    public void setBtnHoldText(String text)
    {
        btnHold.setText(text);
    }

    public void setPercentage(double percentage)
    {
        int base = getResources().getColor(R.color.colorDanger);
        int target = getResources().getColor(R.color.colorDefault);
        int r = (int) (Color.red(target)+ (Color.red(base) - Color.red(target)) * percentage/100);
        int g = (int) (Color.green(target)+ (Color.green(base) - Color.green(target)) * percentage/100);
        int b = (int) (Color.blue(target)+ (Color.blue(base) - Color.blue(target)) * percentage/100);
        onChangeColor(Color.rgb(r,g,b));
    }

    private void onChangeColor(int color)
    {
        if(timer!=null)
        {
            timer.cancel();
        }
        if(Math.abs(currentR - targetR) < Math.abs(Color.red(color) - targetR))
        {
            currentR = Color.red(color);
        }
        if(Math.abs(currentG - targetG) < Math.abs(Color.green(color) - targetG))
        {
            currentG = Color.green(color);
        }
        if(Math.abs(currentB - targetB) < Math.abs(Color.blue(color) - targetB))
        {
            currentB = Color.blue(color);
        }



        deltaR = Math.abs(targetR - currentR) / (BACK_TO_NORMAL_DURATION/100.0);
        deltaG = Math.abs(targetG - currentG) / (BACK_TO_NORMAL_DURATION/100.0);
        deltaB = Math.abs(targetB - currentB) / (BACK_TO_NORMAL_DURATION/100.0);

        timer = new CountDownTimer(BACK_TO_NORMAL_DURATION,100) {
            @Override
            public void onTick(long millisUntilFinished) {
                if(currentR>targetR)
                {
                    currentR-=deltaR;
                }
                else
                {
                    currentR+=deltaR;
                }
                if(currentG>targetG)
                {
                    currentG-=deltaG;
                }
                else
                {
                    currentG+=deltaG;
                }
                if(currentB>targetB)
                {
                    currentB-=deltaB;
                }
                else
                {
                    currentB+=deltaB;
                }
                btnHold.setBackgroundColor(Color.rgb((int)currentR,(int)currentG,(int)currentB));
            }

            @Override
            public void onFinish() {
                btnHold.setBackgroundColor(getResources().getColor(R.color.colorDefault));
            }
        }.start();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if(timer != null)
        {
            timer.cancel();
        }

    }
}
