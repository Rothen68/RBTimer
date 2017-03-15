package com.rothen.rbtimer.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rothen.rbtimer.R;

/**
 * Created by apest on 14/03/2017.
 */

public class TimerFragment extends Fragment {

    private TextView txtTimer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.timer_frag,container);
        txtTimer = (TextView) v.findViewById(R.id.txtTimer);
        return v;
    }

    public void setTimer(String timerValue)
    {
        txtTimer.setText(timerValue);
    }

    public String getTimer()
    {
        return txtTimer.getText().toString();
    }
}
