package com.quaap.primary.math;

import com.quaap.primary.base.Level;

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
public class Math1Level extends Level {

    private final MathOp mMaxMathOp;
    private final MathOp mMinMathOp;
    private final int mMaxNum;


    public Math1Level(MathOp maxMathOp, int maxNum, int rounds) {
        this(maxMathOp, MathOp.Plus, maxNum, rounds);
    }

    public Math1Level(MathOp maxMathOp, MathOp minMathOp, int maxNum, int rounds) {
        super(rounds);
        synchronized (nextlevelsync) {
            mLevel = nextlevelnum++;
        }
        mMaxMathOp = maxMathOp;
        mMinMathOp = minMathOp;
        mMaxNum = maxNum;
    }

    @Override
    public String toString() {
        String ops = getOpsStr();
        return "Level " + mLevel + "\nMax " + mMaxNum + "\n" + ops;
    }

    @Override
    public String getDescription() {
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

}
