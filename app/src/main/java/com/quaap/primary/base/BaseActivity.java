package com.quaap.primary.base;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quaap.primary.Levels;
import com.quaap.primary.MainActivity;
import com.quaap.primary.R;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by tom on 12/15/16.
 * <p>
 * Copyright (C) 2016   Tom Kliethermes
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
    private SharedPreferences mPrefs;
    protected int correct=0;
    private int incorrect=0;
    protected int levelnum = 0;
    private int highestLevelnum = 0;
    private int totalCorrect=0;
    private int totalIncorrect=0;
    private int tscore = 0;
    private int todaysScore = 0;
    private long starttime = System.currentTimeMillis();
    private ActivityWriter actwriter;
    private int correctInARow = 0;
    private String bonuses;

    private String subject;
    private final int subjectId;
    private final int statusId;

    private Level getLevel(int leveln) {
        return levels[leveln];
    }

    public Level[] getLevels() {
        return levels;
    }

    protected void setLevels(Level[] levels) {
        this.levels = levels;
    }

    protected Level[] levels;

    private final int layoutId;

    private String username;

    protected BaseActivity(String levelSetName, int subjectId, int layoutIdtxt, int statusTxtId) {

        this.subjectId = subjectId;
        levels = Levels.getLevels(levelSetName);
        statusId = statusTxtId;
        layoutId = layoutIdtxt;
    }

    private void showProb() {
        showProbImpl();
        starttime = System.currentTimeMillis();
    }

    protected abstract void showProbImpl();

    private boolean hasStorageAccess() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    public static SharedPreferences getSharedPreferences(Context context, String username, String subject) {
        return context.getSharedPreferences("scores:" + username + ":" + subject, MODE_PRIVATE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        username = intent.getStringExtra(MainActivity.USERNAME);
        subject = getString(subjectId);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null) {
            actionBar.setTitle(getString(R.string.app_name) + ": " + subject + " ("+username+")");
        }


        mPrefs = getSharedPreferences(this, username, subject);

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

        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        String todaylast = mPrefs.getString("today","0");

        if (todaylast.equals(today)) {
            todaysScore = mPrefs.getInt("todaysScore", todaysScore);
        } else {
            todaysScore = 0;
        }

        setContentView(layoutId);

        int orientation = getResources().getConfiguration().orientation;
        if (orientation== Configuration.ORIENTATION_LANDSCAPE) {
            LinearLayout answerarea = (LinearLayout)findViewById(R.id.answer_area);
            answerarea.setOrientation(LinearLayout.HORIZONTAL);
        }
        setLevelFields();
        showProb();
        View todayview = (View)findViewById(R.id.todays_area);
        todayview.setVisibility(todaysScore==tscore ? View.GONE : View.VISIBLE);
    }

    protected void answerDone(boolean isright, int addscore, String problem, String answer, String useranswer) {
        long timespent = System.currentTimeMillis() - starttime;
        final TextView status = (TextView)findViewById(statusId);
        bonuses = null;
        if (isright) {
            correct++;
            correctInARow++;
            totalCorrect++;
            int points = addscore * (int)Math.sqrt(correctInARow+1);
            if (timespent<1000) {
                bonuses = getString(R.string.superfast);
                points *= 2;
            } else if (timespent<2000) {
                bonuses = getString(R.string.fast);
                points *= 1.5;
            }
            tscore += points;
            todaysScore += points;

            if (actwriter !=null) {
                actwriter.log(levelnum+1, problem, answer, useranswer, isright, timespent, getCurrentPercentFloat(), todaysScore);
            }

            if (correct>=levels[levelnum].getRounds()) {
                status.setText(R.string.correct);
                correct = 0;
                incorrect = 0;
                if (levelnum+1>=levels.length) {
                    saveState();
                    status.setText(R.string.levels_done);
                    return;
                } else {
                    if (highestLevelnum<levelnum+1) {
                        highestLevelnum = levelnum+1;
                    }
                    status.setText("");
                    new AlertDialog.Builder(this)
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .setTitle(R.string.level_complete)
                            .setMessage(R.string.do_next_level)
                            .setPositiveButton(R.string.next_level, new DialogInterface.OnClickListener()  {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    levelnum++;
                                    saveState();
                                    showProb();
                                    setLevelFields();
                                }

                            })
                            .setNegativeButton(R.string.repeat_level, new DialogInterface.OnClickListener()  {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    saveState();
                                    correct = 0;
                                    incorrect = 0;
                                    showProb();
                                    setLevelFields();
                                }

                            })
                            .show();
                    setLevelFields();
                    return;
                }
            } else {
                status.setText(getString(R.string.correct));
            }
            final int corrects = correct;
            final int incorrects = incorrect;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (corrects == correct && incorrects == incorrect) {
                        status.setText(" ");
                    }
                }
            }, status.getText().length() * 300);
            showProb();
        } else {
            incorrect++;
            correctInARow = 0;
            totalIncorrect++;
            status.setText(R.string.try_again);
            if (actwriter !=null) {
                actwriter.log(levelnum+1, problem, answer, useranswer, isright, timespent, getCurrentPercentFloat(), todaysScore);
            }

        }
        setLevelFields();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (hasStorageAccess()) {
            try {
                actwriter = new ActivityWriter(this, username, subject);
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

        saveState();

        try {
            if (actwriter !=null) actwriter.close();
            actwriter = null;
        } catch (IOException e) {
            Log.e("Primary", "Error closing activity file.",e);
        }
    }

    private void saveState() {
        SharedPreferences.Editor ed = mPrefs.edit();

        ed.putInt("levelnum", levelnum);
        ed.putInt("correct", correct);
        ed.putInt("incorrect", incorrect);
        ed.putInt("totalCorrect", totalCorrect);
        ed.putInt("totalIncorrect", totalIncorrect);
        ed.putInt("highestLevelnum", highestLevelnum);
        ed.putInt("correctInARow", correctInARow);
        ed.putInt("tscore", tscore);
        ed.putInt("todaysScore", todaysScore);
        ed.putString("today",new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));
        ed.apply();
    }

    private void setLevelFields() {
        TextView leveltxt = (TextView)findViewById(R.id.level);
        leveltxt.setText(getString(R.string.level, getLevel(levelnum).getLevelNum()));

        TextView leveldesc = (TextView)findViewById(R.id.level_desc);
        leveldesc.setText(getLevel(levelnum).getShortDescription());

        TextView correcttxt = (TextView)findViewById(R.id.correct);

        correcttxt.setText(String.format(Locale.getDefault(), "%d", getLevel(levelnum).getRounds() - correct));



        TextView scoretxt = (TextView) findViewById(R.id.score);
        scoretxt.setText(getCurrentPercent());


        TextView total_ratio = (TextView)findViewById(R.id.total_ratio);
        total_ratio.setText(String.format(Locale.getDefault(), "%d / %d", totalCorrect, totalCorrect + totalIncorrect));

        TextView todayscore_txt = (TextView)findViewById(R.id.todayscore);
        todayscore_txt.setText(String.format(Locale.getDefault(), "%d", todaysScore));

        TextView tscore_txt = (TextView)findViewById(R.id.tscore);
        tscore_txt.setText(String.format(Locale.getDefault(), "%d", tscore));


        String btext = null;
        if (correctInARow>2) {
            btext = correctInARow + " in a row!";
        }
        if (bonuses!=null) {
            if (btext!=null) btext +="\n"; else btext = "";
            btext += bonuses;
        }
        TextView bonusestxt = (TextView) findViewById(R.id.bonuses);
        bonusestxt.setText(btext);
    }

    private float getCurrentPercentFloat() {
        if (correct + incorrect == 0) {
            return 0;
        }
        return 100 * correct / (float) (correct + incorrect);
    }

    private String getCurrentPercent() {
        float per = getCurrentPercentFloat();
        if ((int)per == per) {
            return String.format(Locale.getDefault(), "%3.0f%%", per);
        } else {
            return String.format(Locale.getDefault(), "%3.1f%%", per);
        }
    }

    protected int getRand(int upper) {
        return getRand(0,upper);
    }

    protected int getRand(int lower, int upper) {
        return (int) (Math.random() * (upper + 1 - lower)) + lower;
    }
}
