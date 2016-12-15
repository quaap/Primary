package com.quaap.primary.base;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quaap.primary.R;

import java.io.IOException;
import java.util.Locale;

/**
 * Created by tom on 12/15/16.
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
public abstract class BaseActivity extends AppCompatActivity {
    public static final String LEVELNAME = "levelnum";
    //protected Math1Level[] levels;

    final protected Handler handler = new Handler();
    protected SharedPreferences mPrefs;
    protected int correct=0;
    protected int incorrect=0;
    protected int levelnum = 0;
    protected int highestLevelnum = 0;
    protected int totalCorrect=0;
    protected int totalIncorrect=0;
    protected int tscore = 0;
    protected long starttime = System.currentTimeMillis();
    protected ActivityWriter actwriter;
    protected int correctInARow = 0;

    protected String subject;


    public Level getLevel(int leveln) {
        return levels[leveln];
    };

    public Level[] getLevels() {
        return levels;
    }

    protected void setLevels(Level[] levels) {
        this.levels = levels;
    }

    protected Level[] levels;


    private boolean hasStorageAccess() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }

    protected void OnCreateCommon(int layoutId) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null) {
            actionBar.setTitle(getString(R.string.app_name) + ": " + subject);
        }

        mPrefs = getSharedPreferences("scores:"+subject, MODE_PRIVATE);

        levelnum = getIntent().getIntExtra(LEVELNAME, -1);

        if (levelnum==-1) {
            levelnum = mPrefs.getInt("levelnum", levelnum);
            correct = mPrefs.getInt("correct", correct);
            incorrect = mPrefs.getInt("incorrect", incorrect);
            correctInARow = mPrefs.getInt("correctInARow", correctInARow);
        }
        totalCorrect = mPrefs.getInt("totalCorrect", totalCorrect);
        totalIncorrect = mPrefs.getInt("totalIncorrect", totalIncorrect);
        highestLevelnum = mPrefs.getInt("highestLevelnum", highestLevelnum);
        tscore = mPrefs.getInt("tscore", tscore);

        setContentView(layoutId);

        int orientation = getResources().getConfiguration().orientation;
        if (orientation== Configuration.ORIENTATION_LANDSCAPE) {
            LinearLayout answerarea = (LinearLayout)findViewById(R.id.answer_area);
            answerarea.setOrientation(LinearLayout.HORIZONTAL);
        }
        setLevelFields();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (hasStorageAccess()) {
            try {
                actwriter = new ActivityWriter(this, subject);
            } catch (IOException e) {
                Log.e("Primary", "Could not write file. Not logging user.", e);
            }
        } else {
            actwriter = null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences.Editor ed = mPrefs.edit();

        ed.putInt("levelnum", levelnum);
        ed.putInt("correct", correct);
        ed.putInt("incorrect", incorrect);
        ed.putInt("totalCorrect", totalCorrect);
        ed.putInt("totalIncorrect", totalIncorrect);
        ed.putInt("highestLevelnum", highestLevelnum);
        ed.putInt("correctInARow", correctInARow);
        ed.putInt("tscore", tscore);

        ed.commit();

        try {
            if (actwriter !=null) actwriter.close();
            actwriter = null;
        } catch (IOException e) {
            Log.e("Primary", "Error closing activity file.",e);
        }
    }

    protected void setLevelFields() {
        TextView leveltxt = (TextView)findViewById(R.id.level);
        //leveltxt.setText(String.format("%d", level.ordinal()));
        leveltxt.setText(getLevel(levelnum).toString());
        TextView correcttxt = (TextView)findViewById(R.id.correct);
        correcttxt.setText(String.format(Locale.getDefault(), "%d", correct));

        TextView neededtxt = (TextView)findViewById(R.id.needed);
        neededtxt.setText(String.format(Locale.getDefault(), "%d", getLevel(levelnum).getRounds()));

        if (correct + incorrect>0) {
            TextView scoretxt = (TextView) findViewById(R.id.score);
            scoretxt.setText(getCurrentPercent());
        }

        TextView total_ratio = (TextView)findViewById(R.id.total_ratio);
        total_ratio.setText(String.format(Locale.getDefault(), "%d / %d", totalCorrect, totalCorrect + totalIncorrect));

        TextView tscore_txt = (TextView)findViewById(R.id.tscore);
        tscore_txt.setText(String.format(Locale.getDefault(), "%d", tscore));

        TextView bonuses = (TextView) findViewById(R.id.bonuses);
        if (correctInARow>2) {
            String btext = correctInARow + " in a row!";
            bonuses.setText(btext);
        } else {
            bonuses.setText(" ");
        }
    }

    protected float getCurrentPercentFloat() {
        if (correct + incorrect == 0) {
            return 0;
        }
        return 100 * correct / (float) (correct + incorrect);
    }

    private String getCurrentPercent() {
        return String.format(Locale.getDefault(), "%3.1f%%", getCurrentPercentFloat());
    }

    public int getRand(int upper) {
        return getRand(0,upper);
    }

    public int getRand(int lower, int upper) {
        return (int) (Math.random() * (upper + 1 - lower)) + lower;
    }
}
