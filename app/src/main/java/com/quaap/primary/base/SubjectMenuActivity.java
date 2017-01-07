package com.quaap.primary.base;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quaap.primary.MainActivity;
import com.quaap.primary.R;
import com.quaap.primary.base.data.AppData;
import com.quaap.primary.base.data.Subjects;
import com.quaap.primary.base.data.UserData;

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

/*
Future Subjects:
Math 1, but typed answers.
Spelling.
Reading/vocabulary.
New big words.
Goofy math word problems.


*/

public class SubjectMenuActivity extends CommonBaseActivity implements Button.OnClickListener {


    private UserData mUserData;
    private UserData.Subject mSubjectData;

    private Subjects.Desc mSubject;
    private String mSubjectCode;

    private String username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Log.d("onCreate", "onCreate savedInstanceState=" + (savedInstanceState==null?"null":"notnull"));
        if (savedInstanceState == null) {

            Intent intent = getIntent();
            mSubjectCode = intent.getStringExtra(MainActivity.SUBJECTCODE);
            username = intent.getStringExtra(MainActivity.USERNAME);

        } else {
            mSubjectCode = savedInstanceState.getString("mSubjectCode", mSubjectCode);
            username = savedInstanceState.getString("username", username);
        }
        //Log.d("onCreate", "onCreate username=" + username);

        if (mSubjectCode == null || username == null) {
            SharedPreferences state = getSharedPreferences(this.getClass().getName(), MODE_PRIVATE);
            mSubjectCode = state.getString("mSubjectCode", mSubjectCode);
            username = state.getString("username", username);
        }
        //Log.d("onCreate", "onCreate username=" + username);
        Subjects subjects = Subjects.getInstance(this);
        mSubject = subjects.get(mSubjectCode);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getString(R.string.app_name) + ": " + mSubject.getName() + " (" + username + ")");
        }

        mUserData = AppData.getAppData(this).getUser(username);
        mSubjectData = mUserData.getSubjectForUser(mSubjectCode);
        setContentView(R.layout.activity_subject_menu);


        Button resume_button = (Button) findViewById(R.id.resume_button);
        resume_button.setTag(-1);
        resume_button.setOnClickListener(this);

        Button clear_button = (Button) findViewById(R.id.clear_button);
        clear_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(SubjectMenuActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle(getString(R.string.clear_progress))
                        .setMessage(R.string.sure_clear_progress)
                        .setPositiveButton(R.string.clear, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                clearProgress();
                            }

                        })
                        .setNegativeButton(getString(R.string.cancel), null)
                        .show();


            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //Log.d("subjectmenu", "onSaveInstanceState called! username=" + username);
        outState.putString("mSubjectCode", mSubjectCode);
        outState.putString("username", username);
        super.onSaveInstanceState(outState);

    }

    @Override
    protected void onResume() {
        super.onResume();
        //Log.d("subjectmenu", "onResume called! username=" + username);

        show_hide_gip();
        showLevelButtons();

    }


    @Override
    protected void onPause() {
        SharedPreferences state = getSharedPreferences(this.getClass().getName(), MODE_PRIVATE);

        state.edit()
                .putString("mSubjectCode", mSubjectCode)
                .putString("username", username)
                .apply();

        super.onPause();
        //Log.d("subjectmenu", "onPause called! username=" + username);

    }


    @Override
    public void onBackPressed() {

        super.onBackPressed();
        finish();
    }


    private void showLevelButtons() {
        int highest = mUserData.getSubjectForUser(mSubjectCode).getHighestLevelNum();

        LinearLayout button_layout = (LinearLayout) findViewById(R.id.button_layout);

        button_layout.removeAllViews();

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER_VERTICAL;

        for (Level level : mSubject.getLevels()) {

            LinearLayout levelrow = new LinearLayout(this);
            levelrow.setOrientation(LinearLayout.HORIZONTAL);
            button_layout.addView(levelrow);

            Button levelbutt = new Button(this);
            levelbutt.setLayoutParams(lp);
            levelbutt.setText(getString(R.string.level, level.getLevelNum()));
            levelbutt.setTag(level.getLevelNum() - 1);
            levelbutt.setOnClickListener(this);
            levelrow.addView(levelbutt);

            TextView desc = new TextView(this);
            desc.setText(level.getDescription(this));
            desc.setLayoutParams(lp);
            desc.setTextSize(16);
            levelrow.addView(desc);

            boolean beenthere = level.getLevelNum() - 1 <= highest;
            levelbutt.setEnabled(beenthere);
            desc.setEnabled(beenthere);
        }
    }

    private void clearProgress() {
        mSubjectData.clearProgress();

        show_hide_gip();
        showLevelButtons();
    }

    private void show_hide_gip() {
        LinearLayout gip_layout = (LinearLayout) findViewById(R.id.gip_layout);
        TextView score_overview = (TextView) findViewById(R.id.score_overview);
        if (mSubjectData.getLevelNum() == -1) {
            gip_layout.setVisibility(View.GONE);
            score_overview.setText(" ");
        } else {
            gip_layout.setVisibility(View.VISIBLE);
            int correct = mSubjectData.getTotalCorrect();
            int incorrect = mSubjectData.getTotalIncorrect();

            int highest = mSubjectData.getHighestLevelNum() + 1;
            int levs = mSubject.getLevels().length;
            if (highest>levs) {
                highest = levs;  // in case an upgrade reduces the number of levels in a subject
            }

            int tscore = mSubjectData.getTotalPoints();
            if (correct + incorrect > 0) {
                String score = getString(R.string.score_overview, highest, correct, (correct + incorrect), tscore);
                score_overview.setText(score);
            }
        }
    }


    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this, mSubject.getActivityclass());
        if ((int) view.getTag() != -1) {
            intent.putExtra(SubjectBaseActivity.START_AT_ZERO, true);
            intent.putExtra(SubjectBaseActivity.LEVELNUM, (int) view.getTag());
        }
        intent.putExtra(MainActivity.USERNAME, username);
        intent.putExtra(MainActivity.SUBJECTCODE, mSubjectCode);
        startActivity(intent);
    }



}
