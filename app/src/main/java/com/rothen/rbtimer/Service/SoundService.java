package com.rothen.rbtimer.Service;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * Manage sounds
 * Created by apest on 16/03/2017.
 */

public class SoundService {
    private MediaPlayer mediaPlayer;

    public void playSound(int soundId, Context context)
    {
        if(mediaPlayer!=null && mediaPlayer.isPlaying())
        {
            mediaPlayer.stop();
        }
        mediaPlayer = MediaPlayer.create(context,soundId);
        mediaPlayer.start();
    }
}
