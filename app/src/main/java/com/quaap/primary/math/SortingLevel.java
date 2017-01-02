package com.quaap.primary.math;

import android.content.Context;

import com.quaap.primary.base.StdLevel;
import com.quaap.primary.base.component.InputMode;

/**
 * Created by tom on 1/1/17.
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
public class SortingLevel extends StdLevel{
    public SortingLevel(String subjectkey, int rounds) {
        super(subjectkey, rounds, InputMode.Buttons);
    }

    @Override
    public String getDescription(Context context) {
        return null;
    }

    @Override
    public String getShortDescription(Context context) {
        return null;
    }
}
