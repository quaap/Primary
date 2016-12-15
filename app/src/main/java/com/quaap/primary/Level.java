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

    private static int nextlevelnum = 1;
    private static final Object nextlevelsync = new Object();


    public Level(MathOp maxMathOp, int maxNum, int rounds) {
        this(maxMathOp, MathOp.Plus, maxNum, rounds);
    }

    public Level(MathOp maxMathOp, MathOp minMathOp, int maxNum, int rounds) {
        synchronized (nextlevelsync) {
            mLevel = nextlevelnum++;
        }
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
        return "Level " + mLevel + ": " + ops + " / Max " + mMaxNum ;
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

//    public int getMaxNum(int prevCorrect) {
//        return (int)(mMaxNum * ((double)Math.max(prevCorrect, mRounds/5.0)/mRounds));
//    }

    public int getRounds() {
        return mRounds;
    }
}
