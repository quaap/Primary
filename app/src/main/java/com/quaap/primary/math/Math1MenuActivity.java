package com.quaap.primary.math;


import android.os.Bundle;

import com.quaap.primary.R;

import com.quaap.primary.base.SubjectMenuActivity;

public class Math1MenuActivity extends SubjectMenuActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int subjectResId = R.string.subject_math1;

        OnCreateCommon(subjectResId, Math1Activity.LevelSetName, Math1Activity.class);
    }



}
