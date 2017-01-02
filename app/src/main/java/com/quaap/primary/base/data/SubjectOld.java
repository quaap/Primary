package com.quaap.primary.base.data;

import android.content.Context;

import com.quaap.primary.Levels;
import com.quaap.primary.R;


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
public class SubjectOld {
    private int pos;
    private String code;
    private String name;
    private String desc;
    private Class activityclass;
    private String levelset;

    public SubjectOld(Context context, int pos) {
        this.setPos(pos);
        this.setCode(getString(context, R.array.subjects, pos));
        this.setName(getString(context, R.array.subjectsName, pos));
        this.setDesc(getString(context, R.array.subjectDescs, pos));
        //this.setActivityclass(getString(context, R.array.subjectsActivity, pos));
        //this.setLevelset(getString(context, R.array.subjectsLevelset, pos));
        this.setActivityclass(Levels.ActivityClasses[pos]);
        this.setLevelset(Levels.LevelSetNames[pos]);
    }

    private String getString(Context context, int arrayid, int pos) {
        return context.getResources().getStringArray(arrayid)[pos];
    }


    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Class getActivityclass() {
        return activityclass;
    }

    public void setActivityclass(Class activityclass) {
        this.activityclass = activityclass;
    }

    public String getLevelset() {
        return levelset;
    }

    public void setLevelset(String levelset) {
        this.levelset = levelset;
    }
}
