package com.quaap.primary.base.data;

import android.content.Context;

import com.quaap.primary.Levels;
import com.quaap.primary.base.Level;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tom on 12/25/16.
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

public class Subjects {

    private static Subjects inst;

    public synchronized static Subjects getInstance(Context context) {
        if (inst == null) {
            inst = new Subjects(context);
        }
        return inst;
    }


    private Desc[] subjects;
    private Map<String, Desc> subjectMap = new HashMap<>();
    private List<String> codes = new ArrayList<>();

    public Subjects(Context context) {
        subjects = loadSubjects(context);
        for(Desc subject: subjects) {
            subjectMap.put(subject.getCode(), subject);
            codes.add(subject.getCode());
        }
    }

    public List<String> getCodes() {

        return codes;
    }

    public String getNextCode(String code) {
        int pos= codes.indexOf(code);
        if (pos+1<codes.size()) {
            return codes.get(pos+1);
        }
        return null;
    }

    public int getCount() {
        return subjects.length;
    }

//    private static String[] getArray(Context context, int arrayid) {
//        return context.getResources().getStringArray(arrayid);
//    }

    public Desc get(String code) {
        return subjectMap.get(code);
    }

    public Desc get(int num) {
        return subjects[num];
    }

    public static Desc[] loadSubjects(Context context) {
//        String[] codes = getArray(context, R.array.subjects);
//        Desc[] subjects = new Desc[codes.length];
//        for (int i = 0; i < codes.length; i++) {
//           // subjects[i] = new Desc(context, i);
//        }
        return Levels.getSubjectInstances(context);
    }

    public static class Desc {
      //  private int pos;
        private String code;
        private String name;
        private String desc;
        private Class activityclass;
       // private String levelset;

        private Level[] levels;

        public Desc(Context context, int code, int name, int desc, Class activityClass, Level[] levels) {
            this.setCode(context.getString(code));
            this.setName(context.getString(name));
            this.setDesc(context.getString(desc));
            this.setActivityclass(activityClass);
            this.levels = levels;

        }

//        public Desc(Context context, int pos) {
//            this.setPos(pos);
//            this.setCode(getString(context, R.array.subjects, pos));
//            this.setName(getString(context, R.array.subjectsName, pos));
//            this.setDesc(getString(context, R.array.subjectDescs, pos));
//
//            this.setActivityclass(Levels.ActivityClasses[pos]);
//            this.setLevelset(Levels.LevelSetNames[pos]);
//        }

//        private String getString(Context context, int id, int pos) {
//            return getArray(context, id)[pos];
//        }
//
//
//        public int getPos() {
//            return pos;
//        }
//
//        public void setPos(int pos) {
//            this.pos = pos;
//        }

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

        public Level[] getLevels() {
            return levels;
        }

//        public String getLevelset() {
//            return levelset;
//        }
//
//        public void setLevelset(String levelset) {
//            this.levelset = levelset;
//        }
    }
}