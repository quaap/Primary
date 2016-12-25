package com.quaap.primary.base.data;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

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
public class AppData {

    public static AppData getAppData(Context context) {
        return new AppData(context);
    }

    private SharedPreferences mPrefs;

    private Context mContext;

    private AppData(Context context) {
        mContext = context;
        mPrefs = mContext.getSharedPreferences("app", MODE_PRIVATE);
    }

    Context getContext() {
        return mContext;
    }

    public static UserData.Subject getSubjectForUser(Context context, String username, String subject) {
        return AppData.getAppData(context).getUser(username).getSubjectForUser(subject);
    }

    public void setLastSelectedUser(String username) {
        mPrefs.edit().putString("lastselecteduser", username).apply();
    }

    public String getLastSelectedUser(String defaultname) {
        return mPrefs.getString("lastselecteduser", defaultname);
    }

    private static final String USERS_KEY = "users";

    public List<String> listUsers() {
        return sort(getUsersSet());
    }

    private Set<String> getUsersSet() {

        Set<String> usernames = new TreeSet<>();
        usernames = mPrefs.getStringSet(USERS_KEY, usernames);
        return usernames;
    }

    public UserData addUser(String username, String avatar) {

        Set<String> usernames = getUsersSet();
        if (usernames.contains(username)) {
            return null;
        }
        usernames.add(username);
        mPrefs.edit().putStringSet(USERS_KEY, usernames).apply();

        UserData user = getUser(username);
        user.setAvatar(avatar);

        return user;
    }

    public boolean avatarInUse(String avatar) {
        return mPrefs.getBoolean("avatar:" + avatar, false);
    }

    public void setAvatarInUse(String avatar, boolean inuse) {
        if (avatar!=null) {
            mPrefs.edit().putBoolean("avatar:" + avatar, inuse).apply();
        }
    }


    private Map<String, UserData> users = new HashMap<>();

    public UserData getUser(String username) {
        if (username==null) {
            return null;
        }
        UserData data = users.get(username);
        if (data==null) {
            data = new UserData(this, username);
            users.put(username, data);
        }
        return data;
    }

    public boolean deleteUser(String username) {
        if (username==null) {
            return false;
        }
        users.remove(username);

        Set<String> usernames = getUsersSet();
        if (usernames.contains(username)) {
            usernames.remove(username);
            mPrefs.edit().putStringSet(USERS_KEY, usernames).apply();
            return true;
        }
        return false;
    }


    public List<String> getUnusedAvatars() {
        return getUnusedAvatars(null);
    }
    public List<String> getUnusedAvatars(String additional) {
        List<String> avatarlist = new ArrayList<>();
        for (String avatar : UserData.avatars) {
            if (!avatarInUse(avatar)) {
                avatarlist.add(avatar);
            }
        }
        if (additional != null) avatarlist.add(additional);
        Collections.sort(avatarlist);
        return avatarlist;
    }

    public static <T extends Comparable> List<T> sort(Collection<T> collection) {
        List<T> list = new ArrayList<>(collection);

        Collections.sort(list);
        return list;
    }
}
