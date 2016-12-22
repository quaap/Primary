package com.quaap.primary.base;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.quaap.primary.AboutActivity;
import com.quaap.primary.Levels;
import com.quaap.primary.MainActivity;
import com.quaap.primary.R;
import com.quaap.primary.base.data.AppData;
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

public abstract class SubjectMenuActivity extends AppCompatActivity implements Button.OnClickListener {
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 121;

    private UserData mUserData;
    private UserData.Subject mSubjectData;

    private String mSubject;
    private String mSubjectName;
    private Class mTargetActivity;

    private String mLevelSetName;
    private String username;

    protected void setTargetActivity(Class targetActivity) {
        mTargetActivity = targetActivity;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Log.d("onCreate", "onCreate savedInstanceState=" + (savedInstanceState==null?"null":"notnull"));
        if (savedInstanceState==null) {

            Intent intent = getIntent();
            mSubject = intent.getStringExtra(MainActivity.SUBJECT);
            mSubjectName = intent.getStringExtra(MainActivity.SUBJECTNAME);
            mLevelSetName = intent.getStringExtra(MainActivity.LEVELSET);
            username = intent.getStringExtra(MainActivity.USERNAME);

        } else {
            mSubject = savedInstanceState.getString("mSubject", mSubject);
            mSubjectName = savedInstanceState.getString("mSubjectName", mSubjectName);
            mLevelSetName = savedInstanceState.getString("mLevelSetName", mLevelSetName);
            username = savedInstanceState.getString("username", username);
        }
        //Log.d("onCreate", "onCreate username=" + username);

        if (mSubject==null || username==null) {
            SharedPreferences state = getSharedPreferences(this.getClass().getName(), MODE_PRIVATE);
            mSubject = state.getString("mSubject", mSubject);
            mSubjectName = state.getString("mSubjectName",mSubjectName);
            mLevelSetName = state.getString("mLevelSetName", mLevelSetName);
            username = state.getString("username", username);
        }
        //Log.d("onCreate", "onCreate username=" + username);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null) {
            actionBar.setTitle(getString(R.string.app_name) + ": " + mSubjectName + " (" + username + ")");
        }

        mUserData = AppData.getAppData(this).getUser(username);
        mSubjectData = mUserData.getSubjectForUser(mSubject);
        setContentView(R.layout.activity_subject_menu);



        Button resume_button = (Button)findViewById(R.id.resume_button);
        resume_button.setTag(-1);
        resume_button.setOnClickListener(this);

        Button clear_button = (Button)findViewById(R.id.clear_button);
        clear_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(SubjectMenuActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle(getString(R.string.clear_progress))
                        .setMessage(R.string.sure_clear_progress)
                        .setPositiveButton(R.string.clear, new DialogInterface.OnClickListener()  {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                clearProgress();
                            }

                        })
                        .setNegativeButton(getString(R.string.cancel), null)
                        .show();


            }
        });
        checkStorageAccess();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //Log.d("subjectmenu", "onSaveInstanceState called! username=" + username);
        outState.putString("mSubject", mSubject);
        outState.putString("mSubjectName", mSubjectName);
        outState.putString("mLevelSetName", mLevelSetName);
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

    /**
     * Dispatch onPause() to fragments.
     */
    @Override
    protected void onPause() {
        SharedPreferences state = getSharedPreferences(this.getClass().getName(), MODE_PRIVATE);

        state.edit()
                .putString("mSubject", mSubject)
                .putString("mSubjectName", mSubjectName)
                .putString("mLevelSetName", mLevelSetName)
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

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {

        switch (menuItem.getItemId()) {

            case R.id.menu_about:
                Intent intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                break;

        }
        return (super.onOptionsItemSelected(menuItem));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);

        return true;
    }


    private void showLevelButtons() {
        int highest = mUserData.getSubjectForUser(mSubject).getHighestLevelNum();

        LinearLayout button_layout = (LinearLayout)findViewById(R.id.button_layout);

        button_layout.removeAllViews();

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER_VERTICAL;

        for( Level level: Levels.getLevels(mLevelSetName)) {

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

            boolean beenthere = level.getLevelNum()-1<=highest;
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
        LinearLayout gip_layout = (LinearLayout)findViewById(R.id.gip_layout);
        TextView score_overview = (TextView)findViewById(R.id.score_overview);
        if (mSubjectData.getLevelNum()==-1) {
            gip_layout.setVisibility(View.GONE);
            score_overview.setText(" ");
        } else {
            gip_layout.setVisibility(View.VISIBLE);
            int correct = mSubjectData.getTotalCorrect();
            int incorrect = mSubjectData.getTotalIncorrect();
            int highest = mSubjectData.getHighestLevelNum() + 1;
            int tscore = mSubjectData.getTodayPoints();
            if (correct+incorrect>0) {
                String score = getString(R.string.score_overview, highest, correct, (correct + incorrect), tscore);
                score_overview.setText(score);
            }
        }
    }


    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this, mTargetActivity);
        if ((int) view.getTag() != -1) {
            intent.putExtra(BaseActivity.START_AT_ZERO, true);
            intent.putExtra(BaseActivity.LEVELNUM, (int) view.getTag());
        }
        intent.putExtra(MainActivity.USERNAME, username);
        intent.putExtra(MainActivity.LEVELSET, mLevelSetName);
        intent.putExtra(MainActivity.SUBJECT, mSubject);
        intent.putExtra(MainActivity.SUBJECTNAME, mSubjectName);
        startActivity(intent);
    }



    private void checkStorageAccess() {
        boolean beendenied = getSharedPreferences(this.getClass().getName(),MODE_PRIVATE).getBoolean("denied", false);
        if (!beendenied && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_WRITE_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this, R.string.write_perms_granted, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, R.string.write_perms_denied, Toast.LENGTH_LONG).show();
                    getSharedPreferences(this.getClass().getName(),MODE_PRIVATE).edit().putBoolean("denied", true).apply();
                }
            }
        }
    }



}
