package com.quaap.primary.spelling;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import com.quaap.primary.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by tom on 12/18/16.
 * <p>
 * Copyright (C) 2016  tom
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
public class TextToVoice {
    private TextToSpeech mTts = null;
    private Context mContext;
    private boolean isInit = false;

    private List<String> errors = new ArrayList<>();

    public TextToVoice(Context context) {
        mContext = context;
        try {
            mTts = new TextToSpeech(mContext, onInitListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private TextToSpeech.OnInitListener onInitListener = new TextToSpeech.OnInitListener() {
        @Override
        public void onInit(int status) {
            if (status == TextToSpeech.SUCCESS) {
                int result = mTts.setLanguage(Locale.getDefault());
                isInit = true;

                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {

                    Log.e("error", "This Language is not supported");
                }
                Log.d("TextToSpeech", "Initialization Suceeded! " + status);
            } else {
                Log.e("error", "Initialization Failed! " + status);
            }
        }
    };

    public void shutDown() {
        mTts.shutdown();
    }

    int utterid = 0;
    public void speak(String text) {
        if (isInit) {
            if (Build.VERSION.SDK_INT>=21) {
                mTts.speak(text, TextToSpeech.QUEUE_ADD, null, "utt" + utterid);
            } else {
                mTts.speak(text, TextToSpeech.QUEUE_ADD, null);
            }
            utterid++;
        } else {
            Log.e("error", "TTS Not Initialized");
        }
    }

}
