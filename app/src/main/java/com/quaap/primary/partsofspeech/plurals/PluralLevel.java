package com.quaap.primary.partsofspeech.plurals;

import android.content.Context;

import com.quaap.primary.base.StdLevel;
import com.quaap.primary.base.component.InputMode;

import java.util.Map;

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
public class PluralLevel extends StdLevel {

    private int mMaxwordlength;
    private int mMinwordlength;

    private static final int wDiff = 4;

    public PluralLevel(String subjectkey, int maxwordlength, int rounds, InputMode inputMode) {
        this(subjectkey, maxwordlength>wDiff?maxwordlength-wDiff:1, maxwordlength, rounds, inputMode);
    }
    public PluralLevel(String subjectkey, int minwordlength, int maxwordlength, int rounds, InputMode inputMode) {
        super(subjectkey, rounds, inputMode);

        mMinwordlength = minwordlength;
        mMaxwordlength = maxwordlength;

    }
    protected PluralLevel(String subjectkey, Map<String,String> initMap) {
        super(subjectkey, initMap);
        mMinwordlength = Integer.parseInt(initMap.get("minwordlength"));
        mMaxwordlength = Integer.parseInt(initMap.get("maxwordlength"));
    }

        @Override
    public String getDescription(Context context) {
        return "Length: " + mMaxwordlength + ". " + (getInputMode()==InputMode.Buttons ? "Multiple choice" : "Keyboard");
    }

    @Override
    public String getShortDescription(Context context) {
        return "Wl: " + mMaxwordlength;
    }

    public int getMaxWordLength() {
        return mMaxwordlength;
    }

    public int getMinWordLength() {
        return mMinwordlength;
    }


}
