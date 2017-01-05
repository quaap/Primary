package com.quaap.primary.base;

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
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Point;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.quaap.primary.MainActivity;
import com.quaap.primary.Primary;
import com.quaap.primary.R;
import com.quaap.primary.base.component.ActivityWriter;
import com.quaap.primary.base.component.Keyboard;
import com.quaap.primary.base.component.SoundEffects;
import com.quaap.primary.base.data.AppData;
import com.quaap.primary.base.data.Subjects;
import com.quaap.primary.base.data.UserData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public abstract class SubjectBaseActivity extends CommonBaseActivity {

    // Things here are common to all level activities.


    public static final String LEVELNUM = "levelnum";
    public static final String START_AT_ZERO = "startover";
    protected static int INPUTTYPE_TEXT = InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_CLASS_TEXT;
    protected static int INPUTTYPE_NUMBER = InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED | InputType.TYPE_NUMBER_FLAG_DECIMAL;
    protected static int INPUTTYPE_TIME = InputType.TYPE_CLASS_DATETIME | InputType.TYPE_DATETIME_VARIATION_TIME;
    final protected Handler handler = new Handler();
    private final int layoutId;
    protected int correct = 0;
    protected int incorrect = 0;
    protected int levelnum = 0;
    private UserData.Subject mSubjectData;
    private int highestLevelnum = 0;
    private int totalCorrect = 0;
    private int totalIncorrect = 0;
    private int tscore = 0;
    private int todaysScore = 0;
    private long starttime = System.currentTimeMillis();
    private ActivityWriter actwriter;
    private int correctInARow = 0;
    private boolean useInARow = true;
    private String bonuses;
    private Subjects.Desc mSubject;
    private String mSubjectCode;
    private int[] fasttimes = {1000, 2000, 3000};
    private Level[] levels;
    private String username;
    private boolean startover;
    private PopupWindow levelCompletePopup;
    private boolean readyForProblem = true;
    private boolean resumeDone = false;
    private List<String> seenProblems = new ArrayList<>();
    private SharedPreferences appPreferences;
    private int maxseensize = 40;
    private long timespent;
    private boolean timeMeasured;

    private SoundEffects soundEffects;

    protected SubjectBaseActivity(int layoutIdtxt) {

        layoutId = layoutIdtxt;
    }

    protected static int getRand(int upper) {
        return getRand(0, upper);
    }

    protected static int getRand(int lower, int upper) {
        return (int) (Math.random() * (upper + 1 - lower)) + lower;
    }

    private Level getLevel(int leveln) {
        return levels[leveln];
    }

    protected Level getLevel() {
        return levels[levelnum];
    }

    private void showProb() {
        showProbImpl();
        startTimer();
    }

    protected abstract void showProbImpl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        soundEffects = ((Primary)getApplicationContext()).getSoundEffects();

        appPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());


        //Log.d("base", "onCreate savedInstanceState=" + (savedInstanceState==null?"null":"notnull"));
        if (savedInstanceState == null) {
            Intent intent = getIntent();
            levelnum = intent.getIntExtra(LEVELNUM, 0);
            //Log.d("base", "intent says levelnum=" + levelnum);
            username = intent.getStringExtra(MainActivity.USERNAME);
            mSubjectCode = intent.getStringExtra(MainActivity.SUBJECTCODE);

            startover = intent.getBooleanExtra(START_AT_ZERO, false);

        } else {
            levelnum = savedInstanceState.getInt(LEVELNUM, 0);
            //Log.d("base", "savedInstanceState says levelnum=" + levelnum);
            username = savedInstanceState.getString(MainActivity.USERNAME);
            mSubjectCode = savedInstanceState.getString(MainActivity.SUBJECTCODE);


        }

        Subjects subjects = Subjects.getInstance(this);
        mSubject = subjects.get(mSubjectCode);
        levels = mSubject.getLevels();


        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getString(R.string.app_name) + ": " + mSubject.getName() + " (" + username + ")");
        }

        mSubjectData = AppData.getSubjectForUser(this, username, mSubjectCode);

        setContentView(layoutId);

        setVolumeControlStream(AudioManager.STREAM_MUSIC);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //Log.d("base", "onSaveInstanceState called! levelnum=" + levelnum);

        outState.putInt(LEVELNUM, levelnum);
        outState.putString(MainActivity.SUBJECTCODE, mSubjectCode);
        outState.putString(MainActivity.USERNAME, username);


        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        //Log.d("base", "onResume1. levelnum=" + levelnum);
        super.onResume();

        if (hasStorageAccess() && appPreferences.getBoolean("use_csv_preference", true)) {
            try {
                actwriter = new ActivityWriter(this, username, mSubjectCode);
            } catch (IOException e) {
                Log.e("Primary", "Could not write file. Not logging user.", e);
            }
        } else {
            actwriter = null;
        }
        restoreGameData();
        resumeDone = true;
        //Log.d("base", "onResume2. levelnum=" + levelnum);
        onShowLevel();
        if (readyForProblem) {
            showProb();
        }
    }

    @Override
    protected void onPause() {

        //Log.d("base", "onPause. levelnum=" + levelnum);
        saveGameData();

        if (levelCompletePopup != null) {
            levelCompletePopup.dismiss();
            //   levelnum++;
            levelCompletePopup = null;
        }
        try {
            if (actwriter != null) actwriter.close();
            actwriter = null;
        } catch (IOException e) {
            Log.e("Primary", "Error closing activity file.", e);
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

        mSubjectData.setPopUpShown(levelCompletePopup != null);

        mSubjectData.saveValue("seenProblems", seenProblems);

    }

    private void restoreGameData() {
        boolean showpopup = false;
        //Log.d("base", "restoreGameData. levelnum=" + levelnum);

        if (!startover) {
            levelnum = mSubjectData.getLevelNum();
            correct = mSubjectData.getCorrect();
            incorrect = mSubjectData.getIncorrect();
            correctInARow = mSubjectData.getCorrectInARow();
            showpopup = mSubjectData.getPopUpShown();
            seenProblems = mSubjectData.getValue("seenProblems", seenProblems);
        }
        //Log.d("base", "restoreGameData2. levelnum=" + levelnum);

        totalCorrect = mSubjectData.getTotalCorrect();
        totalIncorrect = mSubjectData.getTotalIncorrect();
        highestLevelnum = mSubjectData.getHighestLevelNum();
        tscore = mSubjectData.getTotalPoints();

        todaysScore = mSubjectData.getTodayPoints();


        View todayview = findViewById(R.id.todays_area);
        todayview.setVisibility(todaysScore == tscore ? View.GONE : View.VISIBLE);
        setLevelFields();
        if (showpopup) {
            showLevelCompletePopup(false);
        }

    }

    private static final String LDKEY = "_level_";
    protected void saveLevelValue(String name, int value) {
        mSubjectData.saveValue(name + LDKEY + levelnum, value);
    }

    protected void saveLevelValue(String name, String value) {
        mSubjectData.saveValue(name + LDKEY + levelnum, value);
    }

    protected int getSavedLevelValue(String name, int value) {
        return mSubjectData.getValue(name + LDKEY + levelnum, value);
    }

    protected String getSavedLevelValue(String name, String value) {
        return mSubjectData.getValue(name + LDKEY + levelnum, value);
    }

    protected void deleteSavedLevelValue(String name) {
        mSubjectData.deleteValue(name + LDKEY + levelnum);
    }

    protected void saveLevelValue(String name, Set<String> stringset) {
        mSubjectData.saveValue(name + LDKEY + levelnum, stringset);
    }

    protected void saveLevelValue(String name, List<String> stringlist) {
        mSubjectData.saveValue(name + LDKEY + levelnum, stringlist);
    }

    protected Set<String> getSavedLevelValue(String name, Set<String> stringset) {
        return mSubjectData.getValue(name + LDKEY + levelnum, stringset);
    }

    protected List<String> getSavedLevelValue(String name, List<String> stringlist) {
        return mSubjectData.getValue(name + LDKEY + levelnum, stringlist);
    }

    protected void deleteLevelKeys() {
        Set<String> keys = mSubjectData.getKeys("^.*" + LDKEY + levelnum + "$");
        for (String key: keys) {
            mSubjectData.deleteValue(key);
        }
    }

    protected String join(String sep, List<?> list) {
        return join(sep, list.toArray());
    }

    protected String join(String sep, Object... list) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (Object item : list) {
            if (first) {
                first = false;
            } else {
                sb.append(sep);
            }
            sb.append(item);
        }
        return sb.toString();
    }

    protected List<Integer> splitInts(String sep, String string) {
        String[] vals = string.split(sep);
        List<Integer> intlist = new ArrayList<>();
        for (String s : vals) {
            intlist.add(Integer.parseInt(s));
        }
        return intlist;
    }

    protected abstract void onShowLevel();

    protected void setReadyForProblem(boolean ready) {
        readyForProblem = ready;
        if (readyForProblem && resumeDone) {
            showProb();
        }
    }

    protected void makeInputBox(final ViewGroup answerlayout, final ViewGroup keypadarea, final AnswerTypedListener listener, int inputttpe, int emwidth, float fontsize) {
        makeInputBox(answerlayout, keypadarea, listener, inputttpe, emwidth, fontsize, null);
    }

    protected void makeInputBox(final ViewGroup answerlayout, final ViewGroup keypadarea, final AnswerTypedListener listener, int inputttpe, int emwidth, float fontsize, final String defaultInput) {

        answerlayout.removeAllViews();
        ViewGroup type_area = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.typed_input, answerlayout);

        final EditText uinput = (EditText) type_area.findViewById(R.id.uinput_edit);


        if ((inputttpe & InputType.TYPE_CLASS_NUMBER) == InputType.TYPE_CLASS_NUMBER) {
            uinput.setGravity(Gravity.END);
        }

        if (emwidth > 0) {
            uinput.setEms(emwidth);
        }
        if (fontsize > 0) {
            uinput.setTextSize(fontsize);
        }

        uinput.setPrivateImeOptions("nm");

        uinput.setImeOptions(EditorInfo.IME_ACTION_DONE);
        uinput.setInputType(inputttpe);

        uinput.setOnEditorActionListener(
                new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
                        //Log.d("rrr", actionId + " " + event);
                        if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER))
                                || (actionId == EditorInfo.IME_ACTION_DONE)
                                || (actionId == EditorInfo.IME_ACTION_NEXT)
                                || (actionId == EditorInfo.IME_ACTION_GO)
                                ) {
                            if (!listener.answerTyped(uinput.getText().toString())) {
                                showSoftKeyboard(uinput, keypadarea, defaultInput);
                            }
                        }
                        return true;
                    }
                });

        Button clear = (Button) type_area.findViewById(R.id.uinput_clear);
        Button done = (Button) type_area.findViewById(R.id.uinput_done);
        if (keypadarea == null) {
            clear.setVisibility(View.VISIBLE);
            done.setVisibility(View.VISIBLE);
            clear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    uinput.setText("");
                }
            });

            done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!listener.answerTyped(uinput.getText().toString())) {
                        showSoftKeyboard(uinput, keypadarea, defaultInput);
                    }
                }
            });
        } else {

            clear.setVisibility(View.GONE);
            done.setVisibility(View.GONE);

        }

        uinput.setText("");
        showSoftKeyboard(uinput, keypadarea, defaultInput);
    }

    private Map<String,String> mNumpadkeyMap = new TreeMap<>();

    protected void addToNumpadKeyMap(String orig, String repacement) {
        mNumpadkeyMap.put(orig, repacement);
    }

    private Map<String,String> mKeyboardkeyMap = new TreeMap<>();

    protected void addToKeyboardKeyMap(String orig, String repacement) {
        mKeyboardkeyMap.put(orig, repacement);
    }

    protected <T> List<Button> makeChoiceButtons(
            ViewGroup answerlayout,
            List<T> choices,
            final AnswerGivenListener listener) {
        return makeChoiceButtons(answerlayout, choices, listener, 0, null, Gravity.CENTER);
    }

    protected <T> List<Button> makeChoiceButtons(
            ViewGroup answerlayout,
            List<T> choices,
            final AnswerGivenListener listener,
            float fontsize,
            ViewGroup.LayoutParams lparams,
            int gravity) {
        answerlayout.removeAllViews();

        final List<Button> buttons = new ArrayList<>();
        for (T choice : choices) {
            Button ansbutt = new Button(this);
            buttons.add(ansbutt);
            ansbutt.setEnabled(false);
            ansbutt.setTypeface(Typeface.MONOSPACE);
            if (fontsize > 0) {
                ansbutt.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontsize);
            } else {
                ansbutt.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            }

            ansbutt.setAllCaps(false);
            String text = choice.toString();
            ansbutt.setText(capitalize(text));
            ansbutt.setTag(choice);
            ansbutt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (Button ab : buttons) {
                        ab.setEnabled(false);
                    }
                    boolean isright = listener.answerGiven(view.getTag());
                    if (!isright) {
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                for (Button ab : buttons) {
                                    ab.setEnabled(true);
                                }
                            }
                        }, 1000);
                    }
                }
            });
            ansbutt.setGravity(gravity);
            if (lparams != null) {
                ansbutt.setLayoutParams(lparams);
            }
            answerlayout.addView(ansbutt);
        }

        //enable buttons ~2/10 of a second after display. prevents accidental clicks on new problem
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                for (Button ab : buttons) {
                    ab.setEnabled(true);
                }
            }
        }, 220);

        return buttons;
    }

    protected String capitalize(String text) {
        return text.substring(0, 1).toUpperCase(Locale.getDefault()) + (text.length() > 1 ? text.substring(1) : "");
    }

    protected void showSoftKeyboard(final EditText view, ViewGroup keypadarea, String defaultInput) {

        view.clearFocus();

        if (defaultInput != null) {
            view.setText(defaultInput);
            view.setSelection(defaultInput.length());
        }
        boolean isnumeric = ((view.getInputType() & InputType.TYPE_CLASS_NUMBER) == InputType.TYPE_CLASS_NUMBER)
                         || ((view.getInputType() & InputType.TYPE_CLASS_DATETIME) == InputType.TYPE_CLASS_DATETIME);

        int whichkeyboard = Integer.parseInt(appPreferences.getString("keyboard_preference", "1"));
        int whichkeypad = Integer.parseInt(appPreferences.getString("keypad_preference", "1"));

        boolean gkeyboardAvail = getResources().getBoolean(R.bool.game_keyboard_available);
        boolean gkeypadAvail = getResources().getBoolean(R.bool.game_keypad_available);

        if (isnumeric && gkeypadAvail && whichkeypad == 1 && keypadarea != null) {
            Keyboard.showNumberpad(this, view, keypadarea, mNumpadkeyMap);

        } else if (!isnumeric && gkeyboardAvail && whichkeyboard == 1 && keypadarea != null) {
            Keyboard.showKeyboard(this, view, keypadarea, mKeyboardkeyMap);

        } else if (whichkeypad == 2 || whichkeyboard == 2 || (isnumeric && !gkeypadAvail) || (!isnumeric && !gkeyboardAvail)) {
            showSystemKeyboard(view);
        }


    }

    protected void showSystemKeyboard(final EditText view) {
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (view.requestFocus()) {
                    InputMethodManager imm = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
                }
            }
        }, 100);
    }

    protected void setMaxSeenSize(int size) {
        maxseensize = size;
    }

    protected void clearSeenProblem() {
        seenProblems.clear();
    }

    protected boolean seenProblem(Object... parts) {
        while (seenProblems.size() > maxseensize) {
            seenProblems.remove(0);
        }

        String key = "";
        for (Object p : parts) {
            key += p.toString() + "#.#";
        }

        if (seenProblems.contains(key)) {
            return true;
        }
        seenProblems.add(key);

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

    protected void startTimer() {
        starttime = System.currentTimeMillis();
        timeMeasured = false;
    }

    protected void stopTimer() {
        if (!timeMeasured) {
            timespent = System.currentTimeMillis() - starttime;
            timeMeasured = true;
        }
    }

    /**
     *  Calculate the points from the current problem
     *  By default this is based on simply the level number.
     *
     *  Override this in each activity.
     *
     *  Range, in general:
     *    difficulty 1:   1 - 50
     *    difficulty 2:   up to 100
     *    difficulty 3:   up to 200
     *    difficulty 4:   up to 500
     *    difficulty 5:   up to 1000
     *
     * @return the points for the current problem
     */
    protected int calculatePoints() {
        return levelnum+1;
    }

    protected void answerDone(boolean isright, String problem, String answer, String useranswer) {
        stopTimer();

        if (isright) {
            soundEffects.playGood();
        } else {
            soundEffects.playBad();
        }

        bonuses = null;
        if (isright) {
            correct++;
            correctInARow++;
            totalCorrect++;

            int points = getBonuses(calculatePoints(), timespent);
            tscore += points;
            todaysScore += points;

            if (actwriter != null) {
                actwriter.log(levelnum + 1, problem, answer, useranswer, isright, timespent, getCurrentPercentFloat(), points);
            }

            if (correct >= levels[levelnum].getRounds()) {
                onDeleteLevelValues();
                correct = 0;
                incorrect = 0;
                setStatus(R.string.correct, 1200);
                if (levelnum + 1 >= levels.length) {
                    mSubjectData.setSubjectCompleted(true);
                    saveGameData();
                    showLevelCompletePopup(true);
                    return;
                } else {
                    if (highestLevelnum < levelnum + 1) {
                        highestLevelnum = levelnum + 1;
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
            if (actwriter != null) {
                actwriter.log(levelnum + 1, problem, answer, useranswer, isright, timespent, getCurrentPercentFloat(), 0);
            }

        }
        setLevelFields();

    }


    private void showLevelCompletePopup(boolean alldone) {
        final LinearLayout levelcompleteView = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.level_complete, null);

        TextView lc = (TextView) levelcompleteView.findViewById(R.id.level_complete_text);
        lc.setText(getString(R.string.level_complete, getLevel(levelnum).getLevelNum()));

        Point size = getScreenSize();

        int width = size.x;
        int height = size.y;

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

        if (timespent < fasttimes[0]) {
            bonuses = getString(R.string.superfast) + " ×3";
            points *= 3;
        } else if (timespent < fasttimes[1]) {
            bonuses = getString(R.string.fast) + " ×2";
            points *= 2;
        } else if (timespent < fasttimes[2]) {
            bonuses = getString(R.string.quick) + " +50%";
            points *= 1.5;
        }

        if (useInARow) {
            int crbonus = (int) Math.sqrt(correctInARow);
            if (crbonus > 1) {
                if (bonuses == null) bonuses = "\n";
                else bonuses += "\n";
                bonuses += getString(R.string.in_a_row, correctInARow) + " ×" + crbonus;

                points *= crbonus;
            }
        }

        return points;
    }


    private void setLevelFields() {
        TextView leveltxt = (TextView) findViewById(R.id.level);
        leveltxt.setText(getString(R.string.level, getLevel(levelnum).getLevelNum()));

        TextView leveldesc = (TextView) findViewById(R.id.level_desc);
        leveldesc.setText(getLevel(levelnum).getShortDescription(this));

        TextView correcttxt = (TextView) findViewById(R.id.correct);

        correcttxt.setText(String.format(Locale.getDefault(), "%d", getLevel(levelnum).getRounds() - correct));


        TextView scoretxt = (TextView) findViewById(R.id.score);
        scoretxt.setText(getCurrentPercent());


        TextView total_ratio = (TextView) findViewById(R.id.total_ratio);
        total_ratio.setText(String.format(Locale.getDefault(), "%d / %d", totalCorrect, totalCorrect + totalIncorrect));

        TextView todayscore_txt = (TextView) findViewById(R.id.todayscore);
        todayscore_txt.setText(String.format(Locale.getDefault(), "%d", todaysScore));

        TextView tscore_txt = (TextView) findViewById(R.id.tscore);
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
        if ((int) per == per) {
            return String.format(Locale.getDefault(), "%3.0f%%", per);
        } else {
            return String.format(Locale.getDefault(), "%3.1f%%", per);
        }
    }

    protected void setFasttimes(int superfast, int fast, int quick) {
        if (superfast >= fast || fast >= quick) {
            throw new IllegalArgumentException(
                    "'superfast' should be less than 'fast', and 'fast' should be less than 'quick'. " +
                            "Actual values:" + superfast + "," + fast + "," + quick);
        }
        fasttimes[0] = superfast;
        fasttimes[1] = fast;
        fasttimes[2] = quick;

    }

    protected void onDeleteLevelValues() {
        deleteLevelKeys();
    }

    public boolean isLandscape() {
        int orientation = getResources().getConfiguration().orientation;
        return orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    public Point getScreenSize() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    protected void setUseInARow(boolean use) {
        useInARow = use;
    }

    protected SoundEffects getSoundEffects() {
        return soundEffects;
    }


    public interface AnswerGivenListener<T> {
        boolean answerGiven(T answer);
    }

    public interface AnswerTypedListener {
        boolean answerTyped(String answer);
    }
}
