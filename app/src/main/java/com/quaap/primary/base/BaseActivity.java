package com.quaap.primary.base;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
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
public abstract class BaseActivity extends AppCompatActivity  {
    public static final String LEVELNAME = "levelnum";
    //protected BasicMathLevel[] levels;

    private SharedPreferences mPrefs;
    protected int correct=0;
    protected int incorrect=0;
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


    private Level getLevel(int leveln) {
        return levels[leveln];
    }

    private int[] fasttimes = {1000, 2000, 3000};

    protected Level[] levels;

    private final int layoutId;

    private String username;
    private PopupWindow levelCompletePopup;

    protected BaseActivity(int layoutIdtxt) {

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
        subject = intent.getStringExtra(MainActivity.SUBJECT);

        levels = Levels.getLevels(intent.getStringExtra(MainActivity.LEVELSET));


        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null) {
            actionBar.setTitle(getString(R.string.app_name) + ": " + subject + " ("+username+")");
        }

        mPrefs = getSharedPreferences(this, username, subject);

        levelnum = getIntent().getIntExtra(LEVELNAME, -1);
        getIntent().removeExtra(LEVELNAME);

        setContentView(layoutId);


    }

    protected abstract void setStatus(String text);


    private void setStatus(String text, int timeout) {
        final int corrects = correct;
        final int incorrects = incorrect;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (corrects == correct && incorrects == incorrect) {
                    setStatus("");
                }
            }
        }, timeout);
    }

    private void setStatus(int resid, int timeout) {
        setStatus(getString(resid));
    }
    final protected Handler handler = new Handler();

    protected void answerDone(boolean isright, int addscore, String problem, String answer, String useranswer) {
        long timespent = System.currentTimeMillis() - starttime;

        bonuses = null;
        if (isright) {
            correct++;
            correctInARow++;
            totalCorrect++;

            int points = getBonuses(addscore, timespent);
            tscore += points;
            todaysScore += points;

            if (actwriter !=null) {
                actwriter.log(levelnum+1, problem, answer, useranswer, isright, timespent, getCurrentPercentFloat(), todaysScore);
            }

            if (correct>=levels[levelnum].getRounds()) {
                correct = 0;
                incorrect = 0;
                setStatus(R.string.correct, 1200);
                if (levelnum+1>=levels.length) {
                    saveState();
                    showLevelCompletePopup(true);
                    return;
                } else {
                    if (highestLevelnum<levelnum+1) {
                        highestLevelnum = levelnum+1;
                    }

                    showLevelCompletePopup(false);

                    setLevelFields();
                    return;
                }
            } else {
                setStatus(getString(R.string.correct), 1200);
            }
            showProb();
        } else {
            incorrect++;
            correctInARow = 0;
            totalIncorrect++;
            setStatus(R.string.try_again, 1200);
            if (actwriter !=null) {
                actwriter.log(levelnum+1, problem, answer, useranswer, isright, timespent, getCurrentPercentFloat(), todaysScore);
            }

        }
        setLevelFields();

    }


    private void showLevelCompletePopup(boolean alldone) {
        final LinearLayout levelcompleteView = (LinearLayout)LayoutInflater.from(this).inflate(R.layout.level_complete, null);

        TextView lc = (TextView)levelcompleteView.findViewById(R.id.level_complete_text);
        lc.setText(getString(R.string.level_complete, getLevel(levelnum).getLevelNum()));
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        //int width = 400;
        //int height = 300;


       // levelCompletePopup = new PopupWindow(levelcompleteView, levelcompleteView.getWidth(), levelcompleteView.getHeight());
        levelCompletePopup = new PopupWindow(levelcompleteView, width, height, true);

        View no_more_levels_txt = levelcompleteView.findViewById(R.id.no_more_levels_txt);



        View nextlevel_button = levelcompleteView.findViewById(R.id.nextlevel_button);
        View repeatlevel_button = levelcompleteView.findViewById(R.id.repeatlevel_button);

        repeatlevel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                repeatLevel();
                levelCompletePopup.dismiss();
                levelCompletePopup = null;

            }
        });

        if (!alldone) {
            no_more_levels_txt.setVisibility(View.GONE);
            nextlevel_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    nextLevel();
                    levelCompletePopup.dismiss();
                    levelCompletePopup = null;

                }
            });
        } else {
            no_more_levels_txt.setVisibility(View.VISIBLE);

            nextlevel_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    levelCompletePopup.dismiss();
                    levelCompletePopup = null;
                    goBackToMain();
                }
            });
        }


//        int width = wwidth - wwidth/6;
//        int height = width/2;

        levelcompleteView.post(new Runnable() {
            public void run() {
                levelCompletePopup.showAsDropDown(levelcompleteView, Gravity.CENTER, 0, 0);
            }
        });


    }

    public void repeatLevel() {
        saveState();
        correct = 0;
        incorrect = 0;
        showProb();
        setLevelFields();
        setStatus("");
    }


    public void nextLevel() {
        levelnum++;
        saveState();
        showProb();
        setLevelFields();
        setStatus("");
    }

    public void goBackToMain() {
        setResult(SubjectMenuActivity.SUBMENU_RESULT_CLOSE);
        finish();
    }

    private int getBonuses(int addscore, long timespent) {
        int points = addscore;

        if (timespent<fasttimes[0]) {
            bonuses = getString(R.string.superfast) + " ×3";
            points *= 3;
        } else if (timespent<fasttimes[1]) {
            bonuses = getString(R.string.fast) + " ×2";
            points *= 2;
        } else if (timespent<fasttimes[2]) {
            bonuses = getString(R.string.quick) + " +50%";
            points *= 1.5;
        }

        int crbonus = (int)Math.sqrt(correctInARow);
        if (crbonus>1) {
            if (bonuses==null) bonuses = "\n"; else bonuses += "\n";
            bonuses += correctInARow + " in a row! ×" + crbonus;

            points *= crbonus;
        }

        return points;
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
        restoreState();

    }


    @Override
    protected void onPause() {

        saveState();

        if (levelCompletePopup!=null) {
            levelCompletePopup.dismiss();
         //   levelnum++;
            levelCompletePopup = null;
        }
        try {
            if (actwriter !=null) actwriter.close();
            actwriter = null;
        } catch (IOException e) {
            Log.e("Primary", "Error closing activity file.",e);
        }
        super.onPause();
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
        ed.putBoolean("levelCompletePopup", levelCompletePopup!=null);
        ed.apply();
    }

    private void restoreState() {
        boolean showpopup = false;
        if (levelnum==-1) {
            levelnum = mPrefs.getInt("levelnum", levelnum);
            correct = mPrefs.getInt("correct", correct);
            incorrect = mPrefs.getInt("incorrect", incorrect);
            correctInARow = mPrefs.getInt("correctInARow", correctInARow);
            showpopup = mPrefs.getBoolean("levelCompletePopup", false);
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
        int orientation = getResources().getConfiguration().orientation;
        if (orientation== Configuration.ORIENTATION_LANDSCAPE) {
            LinearLayout answerarea = (LinearLayout)findViewById(R.id.answer_area);
            answerarea.setOrientation(LinearLayout.HORIZONTAL);
        }
        View todayview = findViewById(R.id.todays_area);
        todayview.setVisibility(todaysScore==tscore ? View.GONE : View.VISIBLE);
        setLevelFields();
        if (showpopup) {
            showLevelCompletePopup(false);
        }
        showProb();
    }

    private void setLevelFields() {
        TextView leveltxt = (TextView)findViewById(R.id.level);
        leveltxt.setText(getString(R.string.level, getLevel(levelnum).getLevelNum()));

        TextView leveldesc = (TextView)findViewById(R.id.level_desc);
        leveldesc.setText(getLevel(levelnum).getShortDescription(this));

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


//        String btext = null;
//        if (correctInARow>2) {
//            btext = correctInARow + " in a row!";
//        }
//        if (bonuses!=null) {
//            if (btext!=null) btext +="\n"; else btext = "";
//            btext += bonuses;
//        }
        TextView bonusestxt = (TextView) findViewById(R.id.bonuses);
        bonusestxt.setText(bonuses);
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

    protected void setFasttimes(int superfast, int fast, int quick) {
        if (superfast >= fast || fast >= quick) {
            throw new IllegalArgumentException(
                    "'superfast' should be less than 'fast', and 'fast' should be less than 'quick'. " +
                            "Actual values:" + superfast +"," + fast + "," + quick);
        }
        fasttimes[0] = superfast;
        fasttimes[1] = fast;
        fasttimes[2] = quick;

    }
}
