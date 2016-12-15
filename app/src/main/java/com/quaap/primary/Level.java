package com.quaap.primary;

/**
 * Created by tom on 12/14/16.
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
public class Level {

    private MathOp mMaxMathOp;
    private MathOp mMinMathOp;
    private int mMaxNum;
    private int mRounds;

    private int mLevel;

    Level(int levelnum, MathOp maxMathOp, int maxNum, int rounds) {
        this(levelnum, maxMathOp, MathOp.Plus, maxNum, rounds);
    }

    Level(int levelnum, MathOp maxMathOp, MathOp minMathOp, int maxNum, int rounds) {
        mLevel = levelnum;
        mMaxMathOp = maxMathOp;
        mMinMathOp = minMathOp;
        mMaxNum = maxNum;
        mRounds = rounds;
    }

    public String toString() {
        String ops = getOpsStr();
        return "Level " + mLevel + "\nMax " + mMaxNum + "\n" + ops;
    }

    public String getName() {
        String ops = getOpsStr();
        return "Level " + mLevel + " Max " + mMaxNum + " " + ops;
    }

    private String getOpsStr() {
        String ops = "";
        for (MathOp m: MathOp.values()) {
            if (m.ordinal()>=mMinMathOp.ordinal() && m.ordinal()<=mMaxMathOp.ordinal()) {
                ops += m.name();
                if (m.ordinal()<mMaxMathOp.ordinal()) {
                    ops += ", ";
                }
            }
        }
        return ops;
    }

    public int getLevelNum() {
        return mLevel;
    }

    public MathOp getMaxMathOp() {
        return mMaxMathOp;
    }

    public MathOp getMinMathOp() {
        return mMinMathOp;
    }

    public int getMaxNum() {
        return mMaxNum;
    }

    public int getMaxNum(int prevCorrect) {
        return (int)(mMaxNum * ((double)Math.max(prevCorrect, mRounds/5)/mRounds));
    }

    public int getRounds() {
        return mRounds;
    }
}
