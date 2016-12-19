package com.quaap.primary.math;

import com.quaap.primary.base.Level;

public class BasicMathLevel extends Level {

    private final MathOp mMaxMathOp;
    private final MathOp mMinMathOp;
    private final int mMaxNum;
    private final Negatives mNegatives;


    public BasicMathLevel(String subjectkey, MathOp maxMathOp, int maxNum, int rounds) {
        this(subjectkey, maxMathOp, maxMathOp, maxNum, rounds);
    }
    public BasicMathLevel(String subjectkey, MathOp maxMathOp, MathOp minMathOp, int maxNum, int rounds) {
        this(subjectkey, maxMathOp, minMathOp, maxNum, Negatives.None, rounds);
    }
    public BasicMathLevel(String subjectkey, MathOp maxMathOp, int maxNum, Negatives negatives, int rounds) {
        this(subjectkey, maxMathOp, maxMathOp, maxNum, negatives, rounds);
    }
    public BasicMathLevel(String subjectkey, MathOp maxMathOp, MathOp minMathOp, int maxNum, Negatives negatives, int rounds) {
        super(subjectkey, rounds);

        mMaxMathOp = maxMathOp;
        mMinMathOp = minMathOp;
        mMaxNum = maxNum;
        mNegatives = negatives;
    }

    @Override
    public String getDescription() {
        String ops = getOpsStr();
        String max = (mNegatives!=Negatives.None?"\u00B1":"") + mMaxNum;
        return "Level " + mLevel + ": " + ops + " / Max " + max ;
    }

    @Override
    public String getShortDescription() {
        String ops = getOpsSymStr();
        if (mMaxMathOp == mMinMathOp) {
            ops = mMaxMathOp.name();
        }
        String max = (mNegatives!=Negatives.None?"\u00B1":"") + mMaxNum;
        return "Mx" + max + ". " + ops;
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

    private String getOpsSymStr() {
        String ops = "";
        for (MathOp m: MathOp.values()) {
            if (m.ordinal()>=mMinMathOp.ordinal() && m.ordinal()<=mMaxMathOp.ordinal()) {
                ops += m.toString();
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

    public Negatives getNegatives() { return mNegatives; }

//    public int getMaxNum(int prevCorrect) {
//        return (int)(mMaxNum * ((double)Math.max(prevCorrect, mRounds/5.0)/mRounds));
//    }

}
