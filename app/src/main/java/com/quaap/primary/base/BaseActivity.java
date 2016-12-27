package com.quaap.primary.base;

import android.Manifest;

import android.content.Context;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.quaap.primary.AboutActivity;
import com.quaap.primary.Levels;
import com.quaap.primary.MainActivity;
import com.quaap.primary.R;
import com.quaap.primary.base.data.AppData;
import com.quaap.primary.base.data.UserData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
    public static final String LEVELNUM = "levelnum";
    public static final String START_AT_ZERO = "startover";
    //protected BasicMathLevel[] levels;

    private UserData.Subject mSubjectData;

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
    private String subjectName;

    private Level getLevel(int leveln) {
        return levels[leveln];
    }

    private int[] fasttimes = {1000, 2000, 3000};

    protected Level[] levels;

    private final int layoutId;

    private String username;
    private String levelsetname;

    private boolean startover;

    private PopupWindow levelCompletePopup;

    private boolean readyForProblem = true;
    private boolean resumeDone = false;

    private List<String> seenProblems = new ArrayList<>();


    protected BaseActivity(int layoutIdtxt) {

        layoutId = layoutIdtxt;
    }

    private void showProb() {
        showProbImpl();
        startTimer();
    }

    protected abstract void showProbImpl();

    protected void startTimer() {
        starttime = System.currentTimeMillis();
    }

    private boolean hasStorageAccess() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Log.d("base", "onCreate savedInstanceState=" + (savedInstanceState==null?"null":"notnull"));
        if (savedInstanceState==null) {
            Intent intent = getIntent();
            levelnum = intent.getIntExtra(LEVELNUM, 0);
            //Log.d("base", "intent says levelnum=" + levelnum);
            username = intent.getStringExtra(MainActivity.USERNAME);
            subject = intent.getStringExtra(MainActivity.SUBJECT);
            levelsetname = intent.getStringExtra(MainActivity.LEVELSET);
            subjectName = intent.getStringExtra(MainActivity.SUBJECTNAME);
            startover = intent.getBooleanExtra(START_AT_ZERO, false);

        } else {
            levelnum = savedInstanceState.getInt(LEVELNUM, 0);
            //Log.d("base", "savedInstanceState says levelnum=" + levelnum);
            username = savedInstanceState.getString(MainActivity.USERNAME);
            subject = savedInstanceState.getString(MainActivity.SUBJECT);
            levelsetname = savedInstanceState.getString(MainActivity.LEVELSET);
            subjectName = savedInstanceState.getString(MainActivity.SUBJECTNAME);

        }
        levels = Levels.getLevels(levelsetname);


        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null) {
            actionBar.setTitle(getString(R.string.app_name) + ": " + subjectName + " ("+username+")");
        }

        mSubjectData = AppData.getSubjectForUser(this, username, subject);

        setContentView(layoutId);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //Log.d("base", "onSaveInstanceState called! levelnum=" + levelnum);

        outState.putInt(LEVELNUM, levelnum);
        outState.putString(MainActivity.SUBJECT, subject);
        outState.putString(MainActivity.LEVELSET, levelsetname);
        outState.putString(MainActivity.USERNAME, username);
        outState.putString(MainActivity.SUBJECTNAME, subjectName);

        super.onSaveInstanceState(outState);
    }



    @Override
    protected void onResume() {
        //Log.d("base", "onResume1. levelnum=" + levelnum);
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
        restoreGameData();
        resumeDone = true;
        //Log.d("base", "onResume2. levelnum=" + levelnum);
        if (readyForProblem) {
            showProb();
        }
        onShowLevel();
    }


    @Override
    protected void onPause() {

        //Log.d("base", "onPause. levelnum=" + levelnum);
        saveGameData();

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

    private void saveGameData() {
        //Log.d("base", "saveGameData. levelnum=" + levelnum);

        mSubjectData.setLevelNum(levelnum);
        mSubjectData.setCorrect(correct);
        mSubjectData.setIncorrect(incorrect);
        mSubjectData.setTotalCorrect(totalCorrect);
        mSubjectData.setTotalIncorrect(totalIncorrect);
        mSubjectData.setHighestLevelNum(highestLevelnum);
        mSubjectData.setCorrectInARow(correctInARow);
        mSubjectData.setTotalPoints(tscore);
        mSubjectData.setTodayPoints(todaysScore);
        //mSubjectData.setToday(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));
        mSubjectData.setPopUpShown(levelCompletePopup!=null);

    }

    private void restoreGameData() {
        boolean showpopup = false;
        //Log.d("base", "restoreGameData. levelnum=" + levelnum);

        if (!startover) {
            levelnum = mSubjectData.getLevelNum();
            correct =  mSubjectData.getCorrect();
            incorrect = mSubjectData.getIncorrect();
            correctInARow = mSubjectData.getCorrectInARow();
            showpopup = mSubjectData.getPopUpShown();
        }
        //Log.d("base", "restoreGameData2. levelnum=" + levelnum);

        totalCorrect = mSubjectData.getTotalCorrect();
        totalIncorrect = mSubjectData.getTotalIncorrect();
        highestLevelnum = mSubjectData.getHighestLevelNum();
        tscore = mSubjectData.getTotalPoints();

        todaysScore = mSubjectData.getTodayPoints();


        View todayview = findViewById(R.id.todays_area);
        todayview.setVisibility(todaysScore==tscore ? View.GONE : View.VISIBLE);
        setLevelFields();
        if (showpopup) {
            showLevelCompletePopup(false);
        }

    }


    protected abstract void onShowLevel();

    protected void setReadyForProblem(boolean ready) {
        readyForProblem = ready;
        if (readyForProblem && resumeDone) {
            showProb();
        }
    }


    public interface AnswerGivenListener<T> {
        boolean answerGiven(T answer);
    }

    protected <T> List<Button> makeChoiceButtons(
                    ViewGroup answerlayout,
                    List<T> choices,
                    final AnswerGivenListener listener,
                    float fontsize,
                    ViewGroup.LayoutParams lparams,
                    int gravity)
    {
        answerlayout.removeAllViews();

        final List<Button> buttons = new ArrayList<>();
        for (T choice: choices) {
            Button ansbutt = new Button(this);
            buttons.add(ansbutt);
            ansbutt.setEnabled(false);
            ansbutt.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontsize);

            ansbutt.setText(choice.toString());
            ansbutt.setTag(choice);
            ansbutt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (Button ab: buttons) {
                        ab.setEnabled(false);
                    }
                    boolean isright = listener.answerGiven((T)view.getTag());
                    if (!isright) {
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                for (Button ab: buttons) {
                                    ab.setEnabled(true);
                                }
                            }
                        }, 1000);
                    }
                }
            });
            ansbutt.setGravity(gravity);
            ansbutt.setLayoutParams(lparams);
            answerlayout.addView(ansbutt);
        }

        //enable buttons ~1/10 of a second after display. prevents accidental clicks on new problem
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                for (Button ab: buttons) {
                    ab.setEnabled(true);
                }
            }
        }, 120);

        return buttons;
    }

    protected void showSoftKeyboard(final View view) {
        view.clearFocus();
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (view.requestFocus()) {
                    InputMethodManager imm = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
                }
            }
        }, 10);

    }


    private int maxseensize = 20;
    protected boolean seenProblem(Object ... parts) {
        String key = "";
        for (Object p: parts) {
            key += "." + p.toString();
        }
        if (seenProblems.contains(key)) {
            return true;
        }
        seenProblems.add(key);
        if (seenProblems.size()>maxseensize) {
            seenProblems.remove(0);
        }
        return false;
    }

    protected abstract void setStatus(String text);


    private void setStatus(String text, int timeout) {
        setStatus(text);
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
        setStatus(getString(resid), timeout);
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
                actwriter.log(levelnum+1, problem, answer, useranswer, isright, timespent, getCurrentPercentFloat(), points);
            }

            if (correct>=levels[levelnum].getRounds()) {
                correct = 0;
                incorrect = 0;
                setStatus(R.string.correct, 1200);
                if (levelnum+1>=levels.length) {
                    mSubjectData.setSubjectCompleted(true);
                    saveGameData();
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
                actwriter.log(levelnum+1, problem, answer, useranswer, isright, timespent, getCurrentPercentFloat(), 0);
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
        saveGameData();
        correct = 0;
        incorrect = 0;
        setLevelFields();
        setStatus("");
        showProb();
    }


    public void nextLevel() {
        levelnum++;
        saveGameData();
        setLevelFields();
        setStatus("");
        onShowLevel();
        showProb();

    }

    public void goBackToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(MainActivity.LEVELSETDONE, true);
        startActivity(intent);
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

    protected static int getRand(int upper) {
        return getRand(0,upper);
    }

    protected static int getRand(int lower, int upper) {
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        if (id==R.id.menu_about) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

}
