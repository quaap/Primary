package com.quaap.primary.base.data;

import android.content.Context;
import android.os.Build;

import com.quaap.primary.R;

/**
 * Created by tom on 1/2/17.
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
public enum SubjectGroup {
    Math("M", R.string.group_m, R.color.group_color_m),
    LanguageArts("LA", R.string.group_la, R.color.group_color_la),
    TimeMoney("TM", R.string.group_tm, R.color.group_color_tm),
    Science("Sc", R.string.group_sc, R.color.group_color_sc),
    Music("Mu", R.string.group_mu, R.color.group_color_mu),
    Art("Ar", R.string.group_ar, R.color.group_color_ar);


    private String mCode;
    private int mResId;
    private int mColorResId;

    SubjectGroup(String code, int resId, int colorResId) {
        mCode = code;
        mResId = resId;
        mColorResId = colorResId;
    }

    public String getCode() {
        return mCode;
    }

    public int getColor(Context context) {
        if (Build.VERSION.SDK_INT >= 23) {
            return context.getColor(mColorResId);
        } else {
            return context.getResources().getColor(mColorResId);
        }
    }

    public String getText(Context context) {
        return context.getString(mResId);
    }


}
