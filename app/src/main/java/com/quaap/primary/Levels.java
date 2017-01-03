package com.quaap.primary;

import android.content.Context;

import com.quaap.primary.base.Level;
import com.quaap.primary.base.component.InputMode;
import com.quaap.primary.base.data.SubjectGroup;
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


    public static Subjects.Desc[] getSubjectInstances(Context context) {
        Subjects.Desc[] subject = new Subjects.Desc[]{
                new Subjects.Desc(context, SubjectGroup.Math, R.string.subject_m1_code, R.string.subject_m1_name, R.string.subject_m1_desc,
                        BasicMathActivity.class,
                        new Level[]{

                                new BasicMathLevel(R.string.subject_m1_code, MathOp.Plus, 5, 10, InputMode.Buttons),
                                new BasicMathLevel(R.string.subject_m1_code, MathOp.Minus, 5, 10, InputMode.Buttons),

                                new BasicMathLevel(R.string.subject_m1_code, MathOp.Plus, 10, 20, InputMode.Buttons),
                                new BasicMathLevel(R.string.subject_m1_code, MathOp.Minus, 10, 20, InputMode.Buttons),

                                new BasicMathLevel(R.string.subject_m1_code, MathOp.Plus, 10, 10, InputMode.Input),
                                new BasicMathLevel(R.string.subject_m1_code, MathOp.Minus, 10, 10, InputMode.Input),

                                new BasicMathLevel(R.string.subject_m1_code, MathOp.Plus, 15, 20, InputMode.Buttons),
                                new BasicMathLevel(R.string.subject_m1_code, MathOp.Minus, 15, 20, InputMode.Buttons),

                                new BasicMathLevel(R.string.subject_m1_code, MathOp.Plus, 25, 10, InputMode.Buttons),
                                new BasicMathLevel(R.string.subject_m1_code, MathOp.Minus, 25, 10, InputMode.Buttons),

                                new BasicMathLevel(R.string.subject_m1_code, MathOp.Plus, 25, 10, InputMode.Input),
                                new BasicMathLevel(R.string.subject_m1_code, MathOp.Minus, 25, 10, InputMode.Input),

                                new BasicMathLevel(R.string.subject_m1_code, MathOp.Minus, MathOp.Plus, 25, 20, InputMode.Buttons)
                        }),


                new Subjects.Desc(context, SubjectGroup.Math, R.string.subject_m2_code, R.string.subject_m2_name, R.string.subject_m2_desc,
                        BasicMathActivity.class,
                        new Level[]{
                                new BasicMathLevel(R.string.subject_m2_code, MathOp.Times, 5, 10, InputMode.Buttons),
                                new BasicMathLevel(R.string.subject_m2_code, MathOp.Divide, 5, 10, InputMode.Buttons),

                                new BasicMathLevel(R.string.subject_m2_code, MathOp.Times, 10, 20, InputMode.Buttons),
                                new BasicMathLevel(R.string.subject_m2_code, MathOp.Divide, 10, 20, InputMode.Buttons),

                                new BasicMathLevel(R.string.subject_m2_code, MathOp.Times, 10, 10, InputMode.Input),
                                new BasicMathLevel(R.string.subject_m2_code, MathOp.Divide, 10, 10, InputMode.Input),

                                new BasicMathLevel(R.string.subject_m2_code, MathOp.Times, 12, 20, InputMode.Buttons),
                                new BasicMathLevel(R.string.subject_m2_code, MathOp.Divide, 12, 20, InputMode.Buttons),

                                new BasicMathLevel(R.string.subject_m2_code, MathOp.Times, 12, 10, InputMode.Input),
                                new BasicMathLevel(R.string.subject_m2_code, MathOp.Divide, 12, 10, InputMode.Input),

                                new BasicMathLevel(R.string.subject_m2_code, MathOp.Divide, MathOp.Times, 12, 20, InputMode.Buttons),

                                new BasicMathLevel(R.string.subject_m2_code, MathOp.Divide, MathOp.Times, 12, 20, InputMode.Input),
                        }),


                new Subjects.Desc(context, SubjectGroup.Math, R.string.subject_m3_code, R.string.subject_m3_name, R.string.subject_m3_desc,
                        BasicMathActivity.class,
                        new Level[]{
                                //  Same levels, but now with negatives

                                new BasicMathLevel(R.string.subject_m3_code, MathOp.Plus, 5, Negatives.Required, 10, InputMode.Buttons),
                                new BasicMathLevel(R.string.subject_m3_code, MathOp.Minus, 5, Negatives.Required, 10, InputMode.Buttons),

                                new BasicMathLevel(R.string.subject_m3_code, MathOp.Plus, 10, Negatives.Required, 20, InputMode.Buttons),
                                new BasicMathLevel(R.string.subject_m3_code, MathOp.Minus, 10, Negatives.Required, 20, InputMode.Buttons),

                                new BasicMathLevel(R.string.subject_m3_code, MathOp.Plus, 10, Negatives.Required, 10, InputMode.Input),
                                new BasicMathLevel(R.string.subject_m3_code, MathOp.Minus, 10, Negatives.Required, 10, InputMode.Input),

                                new BasicMathLevel(R.string.subject_m3_code, MathOp.Plus, 15, Negatives.Required, 20, InputMode.Buttons),
                                new BasicMathLevel(R.string.subject_m3_code, MathOp.Minus, 15, Negatives.Required, 20, InputMode.Buttons),

                                new BasicMathLevel(R.string.subject_m3_code, MathOp.Plus, 25, Negatives.Required, 10, InputMode.Buttons),
                                new BasicMathLevel(R.string.subject_m3_code, MathOp.Minus, 25, Negatives.Required, 10, InputMode.Buttons),

                                new BasicMathLevel(R.string.subject_m3_code, MathOp.Plus, 25, Negatives.Required, 10, InputMode.Input),
                                new BasicMathLevel(R.string.subject_m3_code, MathOp.Minus, 25, Negatives.Required, 10, InputMode.Input),

                                new BasicMathLevel(R.string.subject_m3_code, MathOp.Minus, MathOp.Plus, 25, Negatives.Allowed, 20, InputMode.Buttons),

                                new BasicMathLevel(R.string.subject_m3_code, MathOp.Minus, MathOp.Plus, 25, Negatives.Allowed, 20, InputMode.Input),
                        }),


                new Subjects.Desc(context, SubjectGroup.Math, R.string.subject_m4_code, R.string.subject_m4_name, R.string.subject_m4_desc,
                        BasicMathActivity.class,
                        new Level[]{
                                new BasicMathLevel(R.string.subject_m4_code, MathOp.Times, 5, Negatives.Required, 10, InputMode.Buttons),
                                new BasicMathLevel(R.string.subject_m4_code, MathOp.Divide, 5, Negatives.Required, 10, InputMode.Buttons),

                                new BasicMathLevel(R.string.subject_m4_code, MathOp.Times, 10, Negatives.Required, 20, InputMode.Buttons),
                                new BasicMathLevel(R.string.subject_m4_code, MathOp.Divide, 10, Negatives.Required, 20, InputMode.Buttons),

                                new BasicMathLevel(R.string.subject_m4_code, MathOp.Times, 10, Negatives.Required, 10, InputMode.Input),
                                new BasicMathLevel(R.string.subject_m4_code, MathOp.Divide, 10, Negatives.Required, 10, InputMode.Input),

                                new BasicMathLevel(R.string.subject_m4_code, MathOp.Times, 12, Negatives.Required, 10, InputMode.Buttons),
                                new BasicMathLevel(R.string.subject_m4_code, MathOp.Divide, 12, Negatives.Required, 10, InputMode.Buttons),

                                new BasicMathLevel(R.string.subject_m4_code, MathOp.Times, 12, Negatives.Required, 10, InputMode.Input),
                                new BasicMathLevel(R.string.subject_m4_code, MathOp.Divide, 12, Negatives.Required, 10, InputMode.Input),


                                new BasicMathLevel(R.string.subject_m4_code, MathOp.Divide, MathOp.Times, 12, Negatives.Allowed, 20, InputMode.Buttons),

                                new BasicMathLevel(R.string.subject_m4_code, MathOp.Divide, MathOp.Times, 12, Negatives.Allowed, 20, InputMode.Input),
                        }),

                new Subjects.Desc(context, SubjectGroup.Math, R.string.subject_m5_code, R.string.subject_m5_name, R.string.subject_m5_desc,
                        BasicMathActivity.class,
                        new Level[]{
                                new BasicMathLevel(R.string.subject_m5_code, MathOp.Divide, MathOp.Plus, 12, Negatives.Allowed, 100, InputMode.Buttons),
                                new BasicMathLevel(R.string.subject_m5_code, MathOp.Divide, MathOp.Plus, 12, Negatives.Allowed, 100, InputMode.Input)
                        }),


                new Subjects.Desc(context, SubjectGroup.Math, R.string.subject_m6_code, R.string.subject_m6_name, R.string.subject_m6_desc,
                        SortingActivity.class,
                        new Level[]{
                                new SortingLevel(R.string.subject_m6_code, 3, 9, 10),
                                new SortingLevel(R.string.subject_m6_code, 4, 9, 10),
                                new SortingLevel(R.string.subject_m6_code, 5, 9, 10),
                                new SortingLevel(R.string.subject_m6_code, 6, 9, 10),
                                new SortingLevel(R.string.subject_m6_code, 7, 9, 10),
                                new SortingLevel(R.string.subject_m6_code, 8, 9, 10),
                                new SortingLevel(R.string.subject_m6_code, 9, 9, 10),
                        }),


                new Subjects.Desc(context, SubjectGroup.Math, R.string.subject_m7_code, R.string.subject_m7_name, R.string.subject_m7_desc,
                        SortingActivity.class,
                        new Level[]{
                                new SortingLevel(R.string.subject_m7_code, 3, 99, 10),
                                new SortingLevel(R.string.subject_m7_code, 4, 99, 10),
                                new SortingLevel(R.string.subject_m7_code, 5, 99, 10),
                                new SortingLevel(R.string.subject_m7_code, 6, 99, 10),
                                new SortingLevel(R.string.subject_m7_code, 7, 99, 10),
                                new SortingLevel(R.string.subject_m7_code, 8, 99, 10),
                                new SortingLevel(R.string.subject_m7_code, 9, 99, 10),
                        }),

                new Subjects.Desc(context, SubjectGroup.Math, R.string.subject_m8_code, R.string.subject_m8_name, R.string.subject_m8_desc,
                        SortingActivity.class,
                        new Level[]{
                                new SortingLevel(R.string.subject_m8_code, 3, 999, 10),
                                new SortingLevel(R.string.subject_m8_code, 4, 999, 10),
                                new SortingLevel(R.string.subject_m8_code, 5, 999, 10),
                                new SortingLevel(R.string.subject_m8_code, 6, 999, 10),
                                new SortingLevel(R.string.subject_m8_code, 7, 999, 10),
                                new SortingLevel(R.string.subject_m8_code, 8, 999, 10),
                                new SortingLevel(R.string.subject_m8_code, 9, 999, 10),
                        }),


                new Subjects.Desc(context, SubjectGroup.Math, R.string.subject_m9_code, R.string.subject_m9_name, R.string.subject_m9_desc,
                        SortingActivity.class,
                        new Level[]{
                                new SortingLevel(R.string.subject_m9_code, 9, 999, 20),
                                new SortingLevel(R.string.subject_m9_code, 12, 999, 20),
                                new SortingLevel(R.string.subject_m9_code, 16, 999, 20),
                        }),


                new Subjects.Desc(context, SubjectGroup.LanguageArts, R.string.subject_sp1_code, R.string.subject_sp1_name, R.string.subject_sp1_desc,
                        SpellingActivity.class,
                        new Level[]{
                                new SpellingLevel(R.string.subject_sp1_code, R.array.spelling_words_1b, 3, 20, InputMode.Buttons),
                                new SpellingLevel(R.string.subject_sp1_code, R.array.spelling_words_1b, 3, 10, InputMode.Input),
                                new SpellingLevel(R.string.subject_sp1_code, R.array.spelling_words_1c, 4, 20, InputMode.Buttons),
                                new SpellingLevel(R.string.subject_sp1_code, R.array.spelling_words_1c, 4, 10, InputMode.Input),
                                new SpellingLevel(R.string.subject_sp1_code, R.array.spelling_words_1d, 5, 20, InputMode.Buttons),
                                new SpellingLevel(R.string.subject_sp1_code, R.array.spelling_words_1d, 5, 10, InputMode.Input),
                        }),


                new Subjects.Desc(context, SubjectGroup.LanguageArts, R.string.subject_sp2_code, R.string.subject_sp2_name, R.string.subject_sp2_desc,
                        SpellingActivity.class,
                        new Level[]{
                                new SpellingLevel(R.string.subject_sp2_code, R.array.spelling_words_1e, 6, 20, InputMode.Buttons),
                                new SpellingLevel(R.string.subject_sp2_code, R.array.spelling_words_1e, 6, 10, InputMode.Input),
                                new SpellingLevel(R.string.subject_sp2_code, R.array.spelling_words_1f, 7, 20, InputMode.Buttons),
                                new SpellingLevel(R.string.subject_sp2_code, R.array.spelling_words_1f, 7, 10, InputMode.Input),
                                new SpellingLevel(R.string.subject_sp2_code, R.array.spelling_words_1g, 8, 20, InputMode.Buttons),
                                new SpellingLevel(R.string.subject_sp2_code, R.array.spelling_words_1g, 8, 10, InputMode.Input),
                        }),


                new Subjects.Desc(context, SubjectGroup.LanguageArts, R.string.subject_sp3_code, R.string.subject_sp3_name, R.string.subject_sp3_desc,
                        SpellingActivity.class,
                        new Level[]{
                                new SpellingLevel(R.string.subject_sp3_code, R.array.spelling_words_1h, 9, 20, InputMode.Buttons),
                                new SpellingLevel(R.string.subject_sp3_code, R.array.spelling_words_1h, 9, 10, InputMode.Input),
                                new SpellingLevel(R.string.subject_sp3_code, R.array.spelling_words_1i, 10, 20, InputMode.Buttons),
                                new SpellingLevel(R.string.subject_sp3_code, R.array.spelling_words_1i, 10, 10, InputMode.Input),
                                new SpellingLevel(R.string.subject_sp3_code, R.array.spelling_words_1j, 12, 20, InputMode.Buttons),
                                new SpellingLevel(R.string.subject_sp3_code, R.array.spelling_words_1j, 12, 10, InputMode.Input),

                                new SpellingLevel(R.string.subject_sp3_code, R.array.spelling_words_1j, 18, 30, InputMode.Buttons),
                        }),

                new Subjects.Desc(context, SubjectGroup.LanguageArts, R.string.subject_eng1_code, R.string.subject_eng1_name, R.string.subject_eng1_desc,
                        PluralActivity.class,
                        new Level[]{
                                new PluralLevel(R.string.subject_eng1_code, 4, 20, InputMode.Buttons),
                                new PluralLevel(R.string.subject_eng1_code, 4, 10, InputMode.Input),
                                new PluralLevel(R.string.subject_eng1_code, 6, 20, InputMode.Buttons),
                                new PluralLevel(R.string.subject_eng1_code, 6, 10, InputMode.Input),
                                new PluralLevel(R.string.subject_eng1_code, 8, 20, InputMode.Buttons),
                                new PluralLevel(R.string.subject_eng1_code, 8, 10, InputMode.Input),
                        }),

                new Subjects.Desc(context, SubjectGroup.LanguageArts, R.string.subject_eng2_code, R.string.subject_eng2_name, R.string.subject_eng2_desc,
                        PluralActivity.class,
                        new Level[]{
                                new PluralLevel(R.string.subject_eng2_code, 10, 20, InputMode.Buttons),
                                new PluralLevel(R.string.subject_eng2_code, 10, 10, InputMode.Input),
                                new PluralLevel(R.string.subject_eng2_code, 12, 20, InputMode.Buttons),
                                new PluralLevel(R.string.subject_eng2_code, 12, 10, InputMode.Input),
                                new PluralLevel(R.string.subject_eng2_code, 18, 20, InputMode.Buttons),
                                new PluralLevel(R.string.subject_eng2_code, 18, 10, InputMode.Input),
                        }),


        };


        return subject;
    }

}
