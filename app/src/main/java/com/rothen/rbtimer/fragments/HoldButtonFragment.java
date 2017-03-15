package com.rothen.rbtimer.fragments;

import android.content.Context;
import android.os.Bundle;
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

    public interface HoldButtonListener
    {
        public void holdButtonBeginTouch();
        public void holdButtonEndTouch();
    }

    private HoldButtonListener listener;
    private Button btnHold;
    private ProgressBar pgrProgress;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            listener = (HoldButtonListener) context;
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

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
