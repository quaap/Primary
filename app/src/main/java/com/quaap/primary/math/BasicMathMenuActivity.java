package com.quaap.primary.math;


import android.os.Bundle;

import com.quaap.primary.base.SubjectMenuActivity;

public class BasicMathMenuActivity extends SubjectMenuActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTargetActivity(BasicMathActivity.class);

    }



}
