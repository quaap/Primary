package com.quaap.primary.spelling;

import android.content.Context;

import com.quaap.primary.R;
import com.quaap.primary.base.StdLevel;
import com.quaap.primary.base.component.InputMode;

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
public class SpellingLevel extends StdLevel {

    private int mWordlistId;
    private int mMaxwordlength;


    public SpellingLevel(int subjectkey, int wordlistid, int maxwordlength, int rounds, InputMode inputMode) {
        super(subjectkey, rounds, inputMode);
        mWordlistId = wordlistid;
        mMaxwordlength = maxwordlength;

    }


    @Override
    public String getDescription(Context context) {
        return context.getString(R.string.length, mMaxwordlength) + " " + getInputModeString(context);
    }

    @Override
    public String getShortDescription(Context context) {
        return "Wl: " + mMaxwordlength;
    }

    public int getmWordlistId() {
        return mWordlistId;
    }

    public int getMaxwordlength() {
        return mMaxwordlength;
    }
}
