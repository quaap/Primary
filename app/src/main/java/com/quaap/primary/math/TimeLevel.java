package com.quaap.primary.math;

import android.content.Context;

import com.quaap.primary.base.StdLevel;
import com.quaap.primary.base.component.InputMode;

/**
 * Created by tom on 1/4/17.
 * <p>
 * Copyright (C) 2017  tom
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
public class TimeLevel extends StdLevel {

    public enum MinuteGranularity {Hour, Half, Quarter, Five, One}
    //public enum SecondGranularity {Minute, Half, Quarter, Five, One}

    private MinuteGranularity mMinuteGranularity;

    public TimeLevel(int subjectkey, MinuteGranularity minGran, int rounds, InputMode mInputMode) {
        super(subjectkey, rounds, mInputMode);
        mMinuteGranularity = minGran;
    }

    public MinuteGranularity getMinuteGranularity() {
        return mMinuteGranularity;
    }

    @Override
    public String getDescription(Context context) {
        return mMinuteGranularity.toString();
    }

    @Override
    public String getShortDescription(Context context) {
        return mMinuteGranularity.toString();
    }
}
