package com.quaap.primary.base;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

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
public abstract class Level {

    private final static Map<String,Integer> nextlevelnum = new HashMap<>();
    private String mSubjectkey;

    private final int mRounds;
    protected int mLevel;

    protected Level(String subjectkey, int rounds) {
        mSubjectkey = subjectkey;
        mRounds = rounds;
        synchronized (nextlevelnum) {
            Integer lnum = nextlevelnum.get(mSubjectkey);
            if (lnum==null) lnum = 0;
            lnum++;
            nextlevelnum.put(mSubjectkey, lnum);
            mLevel = lnum;
        }
    }

    public abstract String getDescription(Context context);

    public abstract String getShortDescription(Context context);

    public int getLevelNum() {
        return mLevel;
    }

    public int getRounds() {
        return mRounds;
    }
}
