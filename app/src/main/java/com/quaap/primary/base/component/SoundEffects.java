package com.quaap.primary.base.component;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Handler;

import com.quaap.primary.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tom on 1/5/17.
 * <p>
 * Copyright (C) 2017  tom
 * <p>
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */
public class SoundEffects {

    private SoundPool mSounds;

    private Map<Integer,Integer> mSoundIds = new HashMap<>();

    private static final int GOODBING = 0;
    private static final int BADBING = 1;
    private static final int HIGHCLICK = 2;
    private static final int LOWCLICK = 3;

    private final int [] soundFiles = {R.raw.goodbing, R.raw.badbing, R.raw.highclick, R.raw.lowclick};
    private final float [] soundVolumes = {.9f, .4f, .5f, .5f};


    private volatile boolean mReady = false;

    public SoundEffects(final Context context) {
        if (Build.VERSION.SDK_INT>=21) {
            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            mSounds = new SoundPool.Builder()
                    .setAudioAttributes(attributes)
                    .build();
        } else {
            mSounds = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        }



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i=0; i<soundFiles.length; i++) {
                    mSoundIds.put(i, mSounds.load(context, soundFiles[i],1));
                }
                mReady = true;
            }
        }, 10);
    }


    private boolean isReady() {
        return mReady;
    }

    private void play(int soundKey) {
        if (isReady()) {
            float vol = soundVolumes[soundKey];
            mSounds.play(mSoundIds.get(soundKey), vol, vol, 1, 0, 1);
        }
    }

    public void playGood() {
        play(GOODBING);
    }
    public void playBad() {
        play(BADBING);
    }
    public void playHighClick() {
        play(HIGHCLICK);
    }
    public void playLowClick() {
        play(LOWCLICK);
    }

    public void release() {
        mSounds.release();
    }
}
