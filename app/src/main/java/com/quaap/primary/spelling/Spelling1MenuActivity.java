package com.quaap.primary.spelling;

import android.os.Bundle;

import com.quaap.primary.R;
import com.quaap.primary.base.SubjectMenuActivity;
import com.quaap.primary.math.Math1Activity;

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
public class Spelling1MenuActivity extends SubjectMenuActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int subjectResId = R.string.subject_spelling1;

        OnCreateCommon(subjectResId, Spelling1Activity.LevelSetName, Spelling1Activity.class);
    }

}
