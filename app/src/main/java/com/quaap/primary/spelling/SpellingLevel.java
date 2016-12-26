package com.quaap.primary.spelling;

import android.content.Context;

import com.quaap.primary.R;
import com.quaap.primary.base.Level;

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
public class SpellingLevel extends Level {

    private int mWordlistId;
    private int mMaxwordlength;

    public SpellingLevel(String subjectkey, int wordlistid, int maxwordlength, int rounds) {
        super(subjectkey, rounds);
        mWordlistId = wordlistid;
        mMaxwordlength = maxwordlength;
    }

    @Override
    public String getDescription(Context context) {
        return context.getString(R.string.level, mLevel) + ": " + "Length: " + mMaxwordlength;
    }

    @Override
    public String getShortDescription(Context context) {
        return "Wl: " + mMaxwordlength;
    }

    public int getmWordlistId() {
        return mWordlistId;
    }
}
