package com.rothen.rbtimer.fragments;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.rothen.rbtimer.R;

/**
 * Created by apest on 10/04/2017.
 */

public class CaptureTheFlagChronoProgressFragment extends Fragment {
    private ProgressBar pbRed;
    private ProgressBar pbBlue;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.capture_the_flag_chrono_progress_frag,container);
        pbRed = (ProgressBar) v.findViewById(R.id.pgrRedProgress);
        pbBlue = (ProgressBar) v.findViewById(R.id.pgrBlueProgress);
        pbBlue.getProgressDrawable().setColorFilter(Color.BLUE, PorterDuff.Mode.MULTIPLY);
        pbRed.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
        return v;
    }

    public void setProgress(int progress)
    {
        if(progress<0)
        {
            pbBlue.setProgress(-progress);
            pbRed.setProgress(0);
        }
        else if (progress>0)
        {
            pbRed.setProgress(progress);
            pbBlue.setProgress(0);
        }
        else
        {
            pbRed.setProgress(0);
            pbBlue.setProgress(0);
        }

    }

}
