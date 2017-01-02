package com.quaap.primary;

import android.content.Context;

import com.quaap.primary.base.component.InputMode;
import com.quaap.primary.base.Level;
import com.quaap.primary.base.data.Subjects;
import com.quaap.primary.math.BasicMathActivity;
import com.quaap.primary.math.BasicMathLevel;
import com.quaap.primary.math.MathOp;
import com.quaap.primary.math.Negatives;
import com.quaap.primary.math.SortingActivity;
import com.quaap.primary.math.SortingLevel;
import com.quaap.primary.partsofspeech.plurals.PluralActivity;
import com.quaap.primary.partsofspeech.plurals.PluralLevel;
import com.quaap.primary.spelling.SpellingActivity;
import com.quaap.primary.spelling.SpellingLevel;

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
public class Levels {

    //these get loaded by base.data.Subjects.


    public static Subjects.Desc [] getSubjectInstances(Context context) {
        Subjects.Desc [] subjects = new Subjects.Desc [] {
                new Subjects.Desc(context, R.string.subjectm1_code, R.string.subjectm1_name, R.string.subjectm1_desc,
                        BasicMathActivity.class,
                        new Level[] {

                                new BasicMathLevel("m1", MathOp.Plus, 5, 10, InputMode.Buttons),
                                new BasicMathLevel("m1", MathOp.Minus, 5, 10, InputMode.Buttons),

                                new BasicMathLevel("m1", MathOp.Plus, 10, 20, InputMode.Buttons),
                                new BasicMathLevel("m1", MathOp.Minus, 10, 20, InputMode.Buttons),

                                new BasicMathLevel("m1", MathOp.Plus, 10, 10, InputMode.Input),
                                new BasicMathLevel("m1", MathOp.Minus, 10, 10, InputMode.Input),

                                new BasicMathLevel("m1", MathOp.Plus, 15, 20, InputMode.Buttons),
                                new BasicMathLevel("m1", MathOp.Minus, 15, 20, InputMode.Buttons),

                                new BasicMathLevel("m1", MathOp.Plus, 25, 10, InputMode.Buttons),
                                new BasicMathLevel("m1", MathOp.Minus, 25, 10, InputMode.Buttons),

                                new BasicMathLevel("m1", MathOp.Plus, 25, 10, InputMode.Input),
                                new BasicMathLevel("m1", MathOp.Minus, 25, 10, InputMode.Input),

                                new BasicMathLevel("m1", MathOp.Minus, MathOp.Plus, 25, 20, InputMode.Buttons)
                        }),


                new Subjects.Desc(context, R.string.subjectm2_code, R.string.subjectm2_name, R.string.subjectm2_desc,
                        BasicMathActivity.class,
                        new Level[] {
                                new BasicMathLevel("m2", MathOp.Times, 5, 10, InputMode.Buttons),
                                new BasicMathLevel("m2", MathOp.Divide, 5, 10, InputMode.Buttons),

                                new BasicMathLevel("m2", MathOp.Times, 10, 20, InputMode.Buttons),
                                new BasicMathLevel("m2", MathOp.Divide, 10, 20, InputMode.Buttons),

                                new BasicMathLevel("m2", MathOp.Times, 10, 10, InputMode.Input),
                                new BasicMathLevel("m2", MathOp.Divide, 10, 10, InputMode.Input),

                                new BasicMathLevel("m2", MathOp.Times, 12, 20, InputMode.Buttons),
                                new BasicMathLevel("m2", MathOp.Divide, 12, 20, InputMode.Buttons),

                                new BasicMathLevel("m2", MathOp.Times, 12, 10, InputMode.Input),
                                new BasicMathLevel("m2", MathOp.Divide, 12, 10, InputMode.Input),

                                new BasicMathLevel("m2", MathOp.Divide, MathOp.Times, 12, 20, InputMode.Buttons),

                                new BasicMathLevel("m2", MathOp.Divide, MathOp.Times, 12, 20, InputMode.Input),
                        }),


                new Subjects.Desc(context, R.string.subjectm3_code, R.string.subjectm3_name, R.string.subjectm3_desc,
                        BasicMathActivity.class,
                        new Level[] {
                                //  Same levels, but now with negatives

                                new BasicMathLevel("m3", MathOp.Plus, 5, Negatives.Required, 10, InputMode.Buttons),
                                new BasicMathLevel("m3", MathOp.Minus, 5, Negatives.Required, 10, InputMode.Buttons),

                                new BasicMathLevel("m3", MathOp.Plus, 10, Negatives.Required, 20, InputMode.Buttons),
                                new BasicMathLevel("m3", MathOp.Minus, 10, Negatives.Required, 20, InputMode.Buttons),

                                new BasicMathLevel("m3", MathOp.Plus, 10, Negatives.Required, 10, InputMode.Input),
                                new BasicMathLevel("m3", MathOp.Minus, 10, Negatives.Required, 10, InputMode.Input),

                                new BasicMathLevel("m3", MathOp.Plus, 15, Negatives.Required, 20, InputMode.Buttons),
                                new BasicMathLevel("m3", MathOp.Minus, 15, Negatives.Required, 20, InputMode.Buttons),

                                new BasicMathLevel("m3", MathOp.Plus, 25, Negatives.Required, 10, InputMode.Buttons),
                                new BasicMathLevel("m3", MathOp.Minus, 25, Negatives.Required, 10, InputMode.Buttons),

                                new BasicMathLevel("m3", MathOp.Plus, 25, Negatives.Required, 10, InputMode.Input),
                                new BasicMathLevel("m3", MathOp.Minus, 25, Negatives.Required, 10, InputMode.Input),

                                new BasicMathLevel("m3", MathOp.Minus, MathOp.Plus, 25, Negatives.Allowed, 20, InputMode.Buttons),

                                new BasicMathLevel("m3", MathOp.Minus, MathOp.Plus, 25, Negatives.Allowed, 20, InputMode.Input),
                        }),


                new Subjects.Desc(context, R.string.subjectm4_code, R.string.subjectm4_name, R.string.subjectm4_desc,
                        BasicMathActivity.class,
                        new Level[] {
                                new BasicMathLevel("m4", MathOp.Times, 5, Negatives.Required, 10, InputMode.Buttons),
                                new BasicMathLevel("m4", MathOp.Divide, 5, Negatives.Required, 10, InputMode.Buttons),

                                new BasicMathLevel("m4", MathOp.Times, 10, Negatives.Required, 20, InputMode.Buttons),
                                new BasicMathLevel("m4", MathOp.Divide, 10, Negatives.Required, 20, InputMode.Buttons),

                                new BasicMathLevel("m4", MathOp.Times, 10, Negatives.Required, 10, InputMode.Input),
                                new BasicMathLevel("m4", MathOp.Divide, 10, Negatives.Required, 10, InputMode.Input),

                                new BasicMathLevel("m4", MathOp.Times, 12, Negatives.Required, 10, InputMode.Buttons),
                                new BasicMathLevel("m4", MathOp.Divide, 12, Negatives.Required, 10, InputMode.Buttons),

                                new BasicMathLevel("m4", MathOp.Times, 12, Negatives.Required, 10, InputMode.Input),
                                new BasicMathLevel("m4", MathOp.Divide, 12, Negatives.Required, 10, InputMode.Input),


                                new BasicMathLevel("m4", MathOp.Divide, MathOp.Times, 12, Negatives.Allowed, 20, InputMode.Buttons),

                                new BasicMathLevel("m4", MathOp.Divide, MathOp.Times, 12, Negatives.Allowed, 20, InputMode.Input),
                        }),

                new Subjects.Desc(context, R.string.subjectm5_code, R.string.subjectm5_name, R.string.subjectm5_desc,
                        BasicMathActivity.class,
                        new Level[] {
                                new BasicMathLevel("m5",  MathOp.Divide, MathOp.Plus, 12, Negatives.Allowed, 100, InputMode.Buttons),
                                new BasicMathLevel("m5",  MathOp.Divide, MathOp.Plus, 12, Negatives.Allowed, 100, InputMode.Input)
                        }),



                new Subjects.Desc(context, R.string.subjectm6_code, R.string.subjectm6_name, R.string.subjectm6_desc,
                        SortingActivity.class,
                        new Level[] {
                                new SortingLevel("sr1", 3, 9, 10),
                                new SortingLevel("sr1", 4, 9, 10),
                                new SortingLevel("sr1", 5, 9, 10),
                                new SortingLevel("sr1", 6, 9, 10),
                                new SortingLevel("sr1", 7, 9, 10),
                                new SortingLevel("sr1", 8, 9, 10),
                                new SortingLevel("sr1", 9, 9, 10),
                        }),


                new Subjects.Desc(context, R.string.subjectm7_code, R.string.subjectm7_name, R.string.subjectm7_desc,
                        SortingActivity.class,
                        new Level[] {
                                new SortingLevel("sr2", 3, 99, 10),
                                new SortingLevel("sr2", 4, 99, 10),
                                new SortingLevel("sr2", 5, 99, 10),
                                new SortingLevel("sr2", 6, 99, 10),
                                new SortingLevel("sr2", 7, 99, 10),
                                new SortingLevel("sr2", 8, 99, 10),
                                new SortingLevel("sr2", 9, 99, 10),
                        }),

                new Subjects.Desc(context, R.string.subjectm8_code, R.string.subjectm8_name, R.string.subjectm8_desc,
                        SortingActivity.class,
                        new Level[] {
                                new SortingLevel("sr3", 3, 999, 10),
                                new SortingLevel("sr3", 4, 999, 10),
                                new SortingLevel("sr3", 5, 999, 10),
                                new SortingLevel("sr3", 6, 999, 10),
                                new SortingLevel("sr3", 7, 999, 10),
                                new SortingLevel("sr3", 8, 999, 10),
                                new SortingLevel("sr3", 9, 999, 10),
                        }),



                new Subjects.Desc(context, R.string.subjectsp1_code, R.string.subjectsp1_name, R.string.subjectsp1_desc,
                        SpellingActivity.class,
                        new Level[] {
                                new SpellingLevel("sp1",R.array.spelling_words_1b, 3, 20, InputMode.Buttons),
                                new SpellingLevel("sp1",R.array.spelling_words_1b, 3, 10, InputMode.Input),
                                new SpellingLevel("sp1",R.array.spelling_words_1c, 4, 20, InputMode.Buttons),
                                new SpellingLevel("sp1",R.array.spelling_words_1c, 4, 10, InputMode.Input),
                                new SpellingLevel("sp1",R.array.spelling_words_1d, 5, 20, InputMode.Buttons),
                                new SpellingLevel("sp1",R.array.spelling_words_1d, 5, 10, InputMode.Input),
                        }),


                new Subjects.Desc(context, R.string.subjectsp2_code, R.string.subjectsp2_name, R.string.subjectsp2_desc,
                        SpellingActivity.class,
                        new Level[] {
                                new SpellingLevel("sp2",R.array.spelling_words_1e, 6, 20, InputMode.Buttons),
                                new SpellingLevel("sp2",R.array.spelling_words_1e, 6, 10, InputMode.Input),
                                new SpellingLevel("sp2",R.array.spelling_words_1f, 7, 20, InputMode.Buttons),
                                new SpellingLevel("sp2",R.array.spelling_words_1f, 7, 10, InputMode.Input),
                                new SpellingLevel("sp2",R.array.spelling_words_1g, 8, 20, InputMode.Buttons),
                                new SpellingLevel("sp2",R.array.spelling_words_1g, 8, 10, InputMode.Input),
                        }),


                new Subjects.Desc(context, R.string.subjectsp3_code, R.string.subjectsp3_name, R.string.subjectsp3_desc,
                        SpellingActivity.class,
                        new Level[] {
                                new SpellingLevel("sp3",R.array.spelling_words_1h, 9, 20, InputMode.Buttons),
                                new SpellingLevel("sp3",R.array.spelling_words_1h, 9, 10, InputMode.Input),
                                new SpellingLevel("sp3",R.array.spelling_words_1i, 10, 20, InputMode.Buttons),
                                new SpellingLevel("sp3",R.array.spelling_words_1i, 10, 10, InputMode.Input),
                                new SpellingLevel("sp3",R.array.spelling_words_1j, 12, 20, InputMode.Buttons),
                                new SpellingLevel("sp3",R.array.spelling_words_1j, 12, 10, InputMode.Input),

                                new SpellingLevel("sp3",R.array.spelling_words_1j, 18, 30, InputMode.Buttons),
                        }),

                new Subjects.Desc(context, R.string.subjecteng1_code, R.string.subjecteng1_name, R.string.subjecteng1_desc,
                        PluralActivity.class,
                        new Level[] {
                                new PluralLevel("pl1", 4, 20, InputMode.Buttons),
                                new PluralLevel("pl1", 4, 10, InputMode.Input),
                                new PluralLevel("pl1", 6, 20, InputMode.Buttons),
                                new PluralLevel("pl1", 6, 10, InputMode.Input),
                                new PluralLevel("pl1", 8, 20, InputMode.Buttons),
                                new PluralLevel("pl1", 8, 10, InputMode.Input),
                        }),

                new Subjects.Desc(context, R.string.subjecteng2_code, R.string.subjecteng2_name, R.string.subjecteng2_desc,
                        PluralActivity.class,
                        new Level[] {
                                new PluralLevel("pl2", 10, 20, InputMode.Buttons),
                                new PluralLevel("pl2", 10, 10, InputMode.Input),
                                new PluralLevel("pl2", 12, 20, InputMode.Buttons),
                                new PluralLevel("pl2", 12, 10, InputMode.Input),
                                new PluralLevel("pl2", 18, 20, InputMode.Buttons),
                                new PluralLevel("pl2", 18, 10, InputMode.Input),
                        }),



        };


        return  subjects;
    }

}
