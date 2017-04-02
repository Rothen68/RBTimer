package com.rothen.rbtimer.service;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.os.CountDownTimer;

import com.rothen.rbtimer.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Manage sounds
 * Created by apest on 16/03/2017.
 */

public class SoundService {

    public enum BipSpeed {
        NONE,
        SLOW,
        FAST
    }

    private BipSpeed bipSpeed;
    private MediaPlayer mediaPlayer;
    private Context context;
    private CountDownTimer bipTimer;
    private List<Integer> playNextIds = new ArrayList<>();

    private Thread audioThread;

    public SoundService(Context context) {
        this.context = context;
        mediaPlayer = new MediaPlayer();
        bipSpeed = BipSpeed.NONE;

    }

    public void ctWins() {
        playSound(R.raw.ct_win, false);
    }

    public void terroWins() {
        playSound(R.raw.terro_win, false);
    }

    public void bombPlanted() {
        playSound(R.raw.bomb_planted, true);
    }

    private void playSound(int soundId, boolean startNowAndCleanPlaylist) {
        if (startNowAndCleanPlaylist) {
            playNextIds.clear();
            launchMediaPlayer(soundId);
        } else {
            if (mediaPlayer.isPlaying()) {
                playNextIds.add(soundId);
            } else {
                launchMediaPlayer(soundId);
            }
        }
    }

    private void launchMediaPlayer(int soundId) {
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = MediaPlayer.create(context, soundId);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (playNextIds.size() > 0) {
                    int soundId = playNextIds.get(0);
                    playNextIds.remove(0);
                    launchMediaPlayer(soundId);
                }
            }
        });
        mediaPlayer.start();
    }

    public void makeBip() {
        playSound(R.raw.bomb_bip, true);
    }

    public void makeBuzz(double frequency, int durationInMs) {

        final int th_duration = (int) (44100 *(durationInMs/1000.0));
        final double th_frequency = frequency;

        if(audioThread != null)
        {
            //audioThread.stop();
        }

        audioThread = new Thread() {
            @Override
            public void run() {


                    int mBufferSize = AudioTrack.getMinBufferSize(44100,
                            AudioFormat.CHANNEL_OUT_MONO,
                            AudioFormat.ENCODING_PCM_8BIT);

                    AudioTrack mAudioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, 44100,
                            AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT,
                            mBufferSize, AudioTrack.MODE_STREAM);

                    // Sine wave
                    double[] mSound = new double[th_duration];
                    short[] mBuffer = new short[th_duration];
                    for (int i = 0; i < th_duration; i++) {
                        mSound[i] = Math.sin((2.0 * Math.PI * i / (44100 / th_frequency)));
                        mBuffer[i] = (short) (mSound[i] * Short.MAX_VALUE);
                    }

                    mAudioTrack.setStereoVolume(AudioTrack.getMaxVolume(), AudioTrack.getMaxVolume());
                    mAudioTrack.play();

                    mAudioTrack.write(mBuffer, 0, mSound.length);
                    mAudioTrack.stop();
                    mAudioTrack.release();


            }
        };

        audioThread.start();
    }


    public void bombExplose() {
        playSound(R.raw.bomb_explose, true);
    }
}
