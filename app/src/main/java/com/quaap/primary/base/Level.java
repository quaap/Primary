package com.quaap.primary.base;

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
    protected static final Object nextlevelsync = new Object();
    protected static int nextlevelnum = 1;
    private final int mRounds;
    protected int mLevel;

    protected Level(int rounds) {
        mRounds = rounds;
    }

    public abstract String toString();

    public abstract String getDescription();

    public abstract String getShortDescription();

    public int getLevelNum() {
        return mLevel;
    }

    public int getRounds() {
        return mRounds;
    }
}
