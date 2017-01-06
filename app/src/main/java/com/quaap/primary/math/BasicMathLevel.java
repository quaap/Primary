package com.quaap.primary.math;

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
import android.content.Context;

import com.quaap.primary.R;
import com.quaap.primary.base.StdLevel;
import com.quaap.primary.base.component.InputMode;

public class BasicMathLevel extends StdLevel {

    private final MathOp mMaxMathOp;
    private final MathOp mMinMathOp;
    private final int mMaxNum;
    private final Negatives mNegatives;

    public BasicMathLevel(int subjectkey, MathOp maxMathOp, int maxNum, int rounds, InputMode inputMode) {
        this(subjectkey, maxMathOp, maxMathOp, maxNum, rounds, inputMode);
    }

    public BasicMathLevel(int subjectkey, MathOp maxMathOp, MathOp minMathOp, int maxNum, int rounds, InputMode inputMode) {
        this(subjectkey, maxMathOp, minMathOp, maxNum, Negatives.None, rounds, inputMode);
    }

    public BasicMathLevel(int subjectkey, MathOp maxMathOp, int maxNum, Negatives negatives, int rounds, InputMode inputMode) {
        this(subjectkey, maxMathOp, maxMathOp, maxNum, negatives, rounds, inputMode);
    }

    public BasicMathLevel(int subjectkey, MathOp maxMathOp, MathOp minMathOp, int maxNum, Negatives negatives, int rounds, InputMode inputMode) {
        super(subjectkey, rounds, inputMode);

        mMaxMathOp = maxMathOp;
        mMinMathOp = minMathOp;
        mMaxNum = maxNum;
        mNegatives = negatives;

    }

    @Override
    public String getDescription(Context context) {
        String ops = getOpsStr(context);
        String max = (mNegatives != Negatives.None ? "\u00B1" : "") + mMaxNum;
        return ops + " / " + context.getString(R.string.max, max) + ". " + getInputModeString(context);

    }

    @Override
    public String getShortDescription(Context context) {
        String ops = getOpsSymStr();
        if (mMaxMathOp == mMinMathOp) {
            ops = mMaxMathOp.name();
        }
        String max = (mNegatives != Negatives.None ? "\u00B1" : "") + mMaxNum;
        return context.getString(R.string.max, max) + ". " + ops;
    }

    private String getOpsStr(Context context) {
        String ops = "";
        for (MathOp m : MathOp.values()) {
            if (m.ordinal() >= mMinMathOp.ordinal() && m.ordinal() <= mMaxMathOp.ordinal()) {
                ops += context.getString(m.getResId());
                if (m.ordinal() < mMaxMathOp.ordinal()) {
                    ops += ", ";
                }
            }
        }
        return ops;
    }

    private String getOpsSymStr() {
        String ops = "";
        for (MathOp m : MathOp.values()) {
            if (m.ordinal() >= mMinMathOp.ordinal() && m.ordinal() <= mMaxMathOp.ordinal()) {
                ops += m.toString();
                if (m.ordinal() < mMaxMathOp.ordinal()) {
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

    public Negatives getNegatives() {
        return mNegatives;
    }


//    public int getMaxNum(int prevCorrect) {
//        return (int)(mMaxNum * ((double)Math.max(prevCorrect, mRounds/5.0)/mRounds));
//    }

}
