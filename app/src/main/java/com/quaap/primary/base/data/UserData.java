package com.quaap.primary.base.data;

import android.content.SharedPreferences;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by tom on 12/20/16.
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
public class UserData {
    //Will probably convert this to sqlite someday.


    public final static String[] avatars;
    private static final int[] AVATARHEX = {
            0x263A, 0x2615, 0x26BE, 0x26F5, 0x26BD, 0x1F680, 0x1F308, 0x1F332, 0x1F333, 0x1F34B,
            0x1F350, 0x1F37C, 0x1F3C7, 0x1F3C9, 0x1F3E4, 0x1F400, 0x1F401, 0x1F402, 0x1F403,
            0x1F404, 0x1F405, 0x1F406, 0x1F407, 0x1F408, 0x1F409, 0x1F40A, 0x1F40B, 0x1F40F,
            0x1F410, 0x1F413, 0x1F415, 0x1F416, 0x1F42A, 0x1F600, 0x1F607, 0x1F608, 0x1F60E,
            0x1F525, 0x1F526, 0x1F527, 0x1F528, 0x1F529, 0x1F52E, 0x1F530, 0x1F531, 0x1F4DA,
            0x1F498, 0x1F482, 0x1F483, 0x1F484, 0x1F485, 0x1F4F7, 0x1F4F9, 0x1F4FA, 0x1F4FB,
            0x1F30D, 0x1F30E, 0x1F310, 0x1F312, 0x1F316, 0x1F317, 0x1F318, 0x1F31A, 0x1F31C,
            0x1F31D, 0x1F31E, 0x1F681, 0x1F682, 0x1F686, 0x1F688, 0x1F334, 0x1F335, 0x1F337,
            0x1F338, 0x1F339, 0x1F33A, 0x1F33B, 0x1F33C, 0x1F33D, 0x1F33E, 0x1F33F, 0x1F340,
            0x1F341, 0x1F342, 0x1F343, 0x1F344, 0x1F345, 0x1F346, 0x1F347, 0x1F348, 0x1F349,
            0x1F34A, 0x1F34C, 0x1F34D, 0x1F34E, 0x1F34F, 0x1F351, 0x1F352, 0x1F353, 0x1F354,
            0x1F355, 0x1F356, 0x1F357, 0x1F35A, 0x1F35B, 0x1F35C, 0x1F35D, 0x1F35E, 0x1F35F,
            0x1F360, 0x1F361, 0x1F362, 0x1F363, 0x1F364, 0x1F365, 0x1F366, 0x1F367, 0x1F368,
            0x1F369, 0x1F36A, 0x1F36B, 0x1F36C, 0x1F36D, 0x1F36E, 0x1F36F, 0x1F370, 0x1F371,
            0x1F372, 0x1F373, 0x1F374, 0x1F375, 0x1F376, 0x1F377, 0x1F378, 0x1F379, 0x1F37A,
            0x1F37B, 0x1F380, 0x1F381, 0x1F382, 0x1F383, 0x1F384, 0x1F385, 0x1F386, 0x1F387,
            0x1F388, 0x1F389, 0x1F38A, 0x1F38B, 0x1F38C, 0x1F38D, 0x1F38E, 0x1F38F, 0x1F390,
            0x1F391, 0x1F392, 0x1F393
    };

    static {
        avatars = new String[AVATARHEX.length];
        for (int i = 0; i < AVATARHEX.length; i++) {
            avatars[i] = new String(Character.toChars(AVATARHEX[i]));
        }
    }

    private SharedPreferences mUserPrefs;
    private String mUsername;
    private AppData mAppdata;

    UserData(AppData appdata, String username) {
        mAppdata = appdata;
        mUsername = username;
        mUserPrefs = mAppdata.getContext().getSharedPreferences("user:" + mUsername, MODE_PRIVATE);

    }

    public void delete() {
        //mUserPrefs.edit().clear().apply();
        mAppdata.setAvatarInUse(getAvatar(), false);
    }

    public String getAvatar() {
        return mUserPrefs.getString("avatar", avatars[0]);
    }

    public void setAvatar(String avatar) {
        mUserPrefs.edit().putString("avatar", avatar).apply();
        mAppdata.setAvatarInUse(getAvatar(), false);
        mAppdata.setAvatarInUse(avatar, true);
    }

    public String getUsername() {
        return mUsername;
    }

    private void addSubjectStarted(String subject) {
        Set<String> subjects = new TreeSet<>();
        subjects = mUserPrefs.getStringSet("subjects", subjects);
        subjects.add(subject);
        mUserPrefs.edit().putStringSet("subjects", subjects).apply();
    }

    public List<String> getSubjectsStarted() {
        Set<String> subjects = new TreeSet<>();
        subjects = mUserPrefs.getStringSet("subjects", subjects);
        return AppData.sort(subjects);
    }

    public int getTotalPoints() {
        int totalpoints = 0;
        for (String sub : getSubjectsStarted()) {
            totalpoints += getSubjectForUser(sub).getTotalPoints();
        }
        return totalpoints;
    }

    public int getTodayPoints() {
        int totalpoints = 0;
        for (String sub : getSubjectsStarted()) {
            totalpoints += getSubjectForUser(sub).getTodayPoints();
        }
        return totalpoints;
    }

    public String getLatestSubject() {
        return mUserPrefs.getString("latestSubject", null);
    }

    public void setLatestSubject(String subject) {
        addSubjectStarted(subject);
        mUserPrefs.edit().putString("latestSubject", subject).apply();
    }

    public Subject getSubjectForUser(String subject) {
        return new Subject(subject);
    }

    public class Subject {

        private final static String PREFIX = "_extra_";
        private final SharedPreferences mSubjectPrefs;
        private final String mSubject;

        private Subject(String subject) {
            mSubject = subject;
            mSubjectPrefs = mAppdata.getContext().getSharedPreferences("user:" + mUsername + ":subject:" + mSubject, MODE_PRIVATE);
        }

        public boolean getSubjectCompleted() {
            return getIntField("subjectCompleted", 0) == 1;
        }

        public void setSubjectCompleted(boolean subjectCompleted) {
            setIntField("subjectCompleted", subjectCompleted ? 1 : 0);
        }

        public int getLevelNum() {
            return getIntField("levelnum", -1);
        }

        public void setLevelNum(int levelNum) {
            setIntField("levelnum", levelNum);
        }

        public int getCorrect() {
            return getIntField("correct", 0);
        }

        public void setCorrect(int correct) {
            setIntField("correct", correct);
        }

        public int getIncorrect() {
            return getIntField("incorrect", 0);
        }

        public void setIncorrect(int incorrect) {
            setIntField("incorrect", incorrect);
        }

        public int getTotalCorrect() {
            return getIntField("totalCorrect", 0);
        }

        public void setTotalCorrect(int totalCorrect) {
            setIntField("totalCorrect", totalCorrect);
        }

        public int getTotalIncorrect() {
            return getIntField("totalIncorrect", 0);
        }

        public void setTotalIncorrect(int totalIncorrect) {
            setIntField("totalIncorrect", totalIncorrect);
        }

        public int getHighestLevelNum() {
            return getIntField("highestLevelNum", 0);
        }

        public void setHighestLevelNum(int highestLevelNum) {
            setIntField("highestLevelNum", highestLevelNum);
        }

        public int getTotalPoints() {
            return getIntField("totalPoints", 0);
        }

        public void setTotalPoints(int totalPoints) {
            setIntField("totalPoints", totalPoints);
        }

        public void addTotalPoints(int totalPoints) {
            setTotalPoints(totalPoints + getTotalPoints());
        }

        public Map<String, Integer> getTodayPointHistory() {
            Map<String, Integer> res = new TreeMap<>();
            Pattern treg = Pattern.compile("^day(\\d{4}-\\d{2}-\\d{2})");
            for (String entry : mSubjectPrefs.getAll().keySet()) {
                Matcher m = treg.matcher(entry);
                if (m.find()) {
                    res.put(m.group(1), mSubjectPrefs.getInt(entry, 0));
                }
            }
            return res;
        }

        private String getToday() {
            return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        }

        public int getTodayPoints() {
            return getIntField("day" + getToday(), 0);
        }

        public void setTodayPoints(int todayPoints) {
            setIntField("day" + getToday(), todayPoints);
        }

        public void addTodayPoints(int todayPoints) {
            setTodayPoints(todayPoints + getTodayPoints());
        }

        public int getCorrectInARow() {
            return getIntField("correctInArow", 0);
        }

        public void setCorrectInARow(int correctInArow) {
            setIntField("correctInArow", correctInArow);
        }

        public boolean getPopUpShown() {
            return getIntField("popUpShown", 0) == 1;
        }

        public void setPopUpShown(boolean popUpShown) {
            setIntField("popUpShown", popUpShown ? 1 : 0);

        }

        public void saveValue(String name, int value) {
            mSubjectPrefs.edit().putInt(PREFIX + name, value).apply();
        }

        public void saveValue(String name, String value) {
            mSubjectPrefs.edit().putString(PREFIX + name, value).apply();
        }

        public int getValue(String name, int value) {
            return mSubjectPrefs.getInt(PREFIX + name, value);
        }

        public String getValue(String name, String value) {
            return mSubjectPrefs.getString(PREFIX + name, value);
        }

        public void deleteValue(String name) {
            mSubjectPrefs.edit().remove(PREFIX + name).apply();
        }

        public void saveValue(String name, Set<String> stringset) {
            mSubjectPrefs.edit().putStringSet(PREFIX + name, stringset).apply();
        }

        public void saveValue(String name, List<String> stringlist) {
            saveValue(name, new TreeSet<>(stringlist));
        }

        public Set<String> getValue(String name, Set<String> stringset) {
            return mSubjectPrefs.getStringSet(PREFIX + name, stringset);
        }

        public List<String> getValue(String name, List<String> stringlist) {
            return new ArrayList<>(getValue(name, new TreeSet<>(stringlist)));
        }

        public Set<String> getKeys(String matching) {
            Set<String> ret = new LinkedHashSet<>();
            for (String key: mSubjectPrefs.getAll().keySet()) {
                if (key!=null && key.matches(matching)) {
                    ret.add(key);
                }
            }
            return ret;
        }

        public void clearProgress() {
            mSubjectPrefs.edit().clear().apply();
        }

        private void setIntField(String field, int value) {
            mSubjectPrefs.edit().putInt(field, value).apply();
        }

        private int getIntField(String field, int defaultvalue) {
            return mSubjectPrefs.getInt(field, defaultvalue);
        }

    }

}
