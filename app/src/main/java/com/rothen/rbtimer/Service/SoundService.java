package com.rothen.rbtimer.Service;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.CountDownTimer;

import com.rothen.rbtimer.R;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Manage sounds
 * Created by apest on 16/03/2017.
 */

public class SoundService {

    public enum BipSpeed
    {
        NONE,
        SLOW,
        FAST
    }
    private BipSpeed bipSpeed;
    private MediaPlayer mediaPlayer;
    private Context  context;
    private CountDownTimer bipTimer;
    private List<Integer> playNextIds = new ArrayList<>();

    public SoundService(Context context)
    {
        this.context=context;
        mediaPlayer = new MediaPlayer();
        bipSpeed = BipSpeed.NONE;

    }

    public void ctWins()
    {
        playSound(R.raw.ct_win,false);
    }

    public void terroWins()
    {
        playSound(R.raw.terro_win,false);
    }

    public void bombPlanted()
    {
        playSound(R.raw.bomb_planted,true);
    }

    private void playSound(int soundId, boolean startNowAndCleanPlaylist)
    {
        if(startNowAndCleanPlaylist)
        {
            playNextIds.clear();
            launchMediaPlayer(soundId);
        }
        else
        {
            if(mediaPlayer.isPlaying())
            {
                playNextIds.add(soundId);
            }
            else
            {
                launchMediaPlayer(soundId);
            }
        }
    }

    private void launchMediaPlayer(int soundId)
    {
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = MediaPlayer.create(context,soundId);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if(playNextIds.size()>0)
                {
                    int soundId = playNextIds.get(0);
                    playNextIds.remove(0);
                    launchMediaPlayer(soundId);
                }
            }
        });
        mediaPlayer.start();
    }

    public void makeBip()
    {
        playSound(R.raw.bomb_bip,true);
    }


    public void bombExplose()
    {
        playSound(R.raw.bomb_explose,true);
    }
}
