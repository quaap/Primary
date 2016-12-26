package com.quaap.primary;

import com.quaap.primary.base.Level;
import com.quaap.primary.math.BasicMathLevel;
import com.quaap.primary.math.MathOp;
import com.quaap.primary.math.Negatives;
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

    public static Level[] getLevels(String name) {
        for (int i=0; i<LevelSetNames.length; i++ ) {
            if (LevelSetNames[i].equals(name)) {
                return LevelSets[i];
            }
        }
        throw new IllegalArgumentException("No such Level name " + name);
    }


    public static final String[] LevelSetNames = {
            "Math1Levels",
            "Math2Levels",
            "Math3Levels",
            "Math4Levels",
            "Math5Levels",

            "Spelling1Levels",
            "Spelling2Levels",
            "Spelling3Levels",

    };

    //could probably read all these from a xml file.
    public static final Level[][] LevelSets =
    {
            //Math1Levels
             {

                     new BasicMathLevel("m1", MathOp.Plus, 5, 10),
                     new BasicMathLevel("m1", MathOp.Minus, 5, 10),

                     new BasicMathLevel("m1", MathOp.Plus, 10, 20),
                     new BasicMathLevel("m1", MathOp.Minus, 10, 20),

                     new BasicMathLevel("m1", MathOp.Plus, 15, 20),
                     new BasicMathLevel("m1", MathOp.Minus, 15, 20),

                     new BasicMathLevel("m1", MathOp.Plus, 25, 10),
                     new BasicMathLevel("m1", MathOp.Minus, 25, 10),

                     new BasicMathLevel("m1", MathOp.Minus, MathOp.Plus, 25, 20),
             },

             //Math2Levels
             {
                    new BasicMathLevel("m2", MathOp.Times, 5, 10),
                    new BasicMathLevel("m2", MathOp.Divide, 5, 10),

                    new BasicMathLevel("m2", MathOp.Times, 10, 20),
                    new BasicMathLevel("m2", MathOp.Divide, 10, 20),

                    new BasicMathLevel("m2", MathOp.Times, 12, 10),
                    new BasicMathLevel("m2", MathOp.Divide, 12, 10),

                    new BasicMathLevel("m2", MathOp.Divide, MathOp.Times, 12, 20),
             },

             //Math3Levels
             {
//  Same levels, but now with negatives

                    new BasicMathLevel("m3", MathOp.Plus, 5, Negatives.Required, 10),
                    new BasicMathLevel("m3", MathOp.Minus, 5, Negatives.Required, 10),

                    new BasicMathLevel("m3", MathOp.Plus, 10, Negatives.Required, 20),
                    new BasicMathLevel("m3", MathOp.Minus, 10, Negatives.Required, 20),

                    new BasicMathLevel("m3", MathOp.Plus, 15, Negatives.Required, 20),
                    new BasicMathLevel("m3", MathOp.Minus, 15, Negatives.Required, 20),

                    new BasicMathLevel("m3", MathOp.Plus, 25, Negatives.Required, 10),
                    new BasicMathLevel("m3", MathOp.Minus, 25, Negatives.Required, 10),

                    new BasicMathLevel("m3", MathOp.Minus, MathOp.Plus, 25, Negatives.Allowed, 20),

             },

             //Math4Levels
             {

                    new BasicMathLevel("m4", MathOp.Times, 5, Negatives.Required, 10),
                    new BasicMathLevel("m4", MathOp.Divide, 5, Negatives.Required, 10),

                    new BasicMathLevel("m4", MathOp.Times, 10, Negatives.Required, 20),
                    new BasicMathLevel("m4", MathOp.Divide, 10, Negatives.Required, 20),

                    new BasicMathLevel("m4", MathOp.Times, 12, Negatives.Required, 10),
                    new BasicMathLevel("m4", MathOp.Divide, 12, Negatives.Required, 10),


                    new BasicMathLevel("m4", MathOp.Divide, MathOp.Times, 12, Negatives.Allowed, 20),


             },

             //Math5Levels
             {

                     new BasicMathLevel("m5",  MathOp.Divide, MathOp.Plus, 12, Negatives.Allowed, 100)
             },

            //Spelling1Levels
            {
                    new SpellingLevel("sp1",R.array.spelling_words_1a, 2, 10),
                    new SpellingLevel("sp1",R.array.spelling_words_1b, 3, 20),
                    new SpellingLevel("sp1",R.array.spelling_words_1c, 4, 20),
            },

            //Spelling2Levels
            {
                    new SpellingLevel("sp2",R.array.spelling_words_1d, 5, 20),
                    new SpellingLevel("sp2",R.array.spelling_words_1e, 6, 20),
                    new SpellingLevel("sp2",R.array.spelling_words_1f, 7, 20),
            },

            //Spelling3Levels
            {
                    new SpellingLevel("sp3",R.array.spelling_words_1g, 8, 20),
                    new SpellingLevel("sp3",R.array.spelling_words_1h, 9, 20),
                    new SpellingLevel("sp3",R.array.spelling_words_1i, 10, 20),
            },


    };
}
