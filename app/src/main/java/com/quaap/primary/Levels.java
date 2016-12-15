package com.quaap.primary;

import com.quaap.primary.base.Level;
import com.quaap.primary.math.Math1Level;
import com.quaap.primary.math.MathOp;

/**
 * Created by tom on 12/15/16.
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
            "Math1Levels"
    };


    public static final Level[][] LevelSets =
     {
            //Math1Levels
            {
                    new Math1Level(MathOp.Plus, MathOp.Plus, 5, 10),
                    new Math1Level(MathOp.Minus, MathOp.Minus, 5, 10),

                    new Math1Level(MathOp.Plus, MathOp.Plus, 10, 20),
                    new Math1Level(MathOp.Minus, MathOp.Minus, 10, 20),

                    new Math1Level(MathOp.Plus, MathOp.Plus, 15, 20),
                    new Math1Level(MathOp.Minus, MathOp.Minus, 15, 20),

                    new Math1Level(MathOp.Plus, MathOp.Plus, 25, 10),
                    new Math1Level(MathOp.Minus, MathOp.Minus, 25, 10),

                    new Math1Level(MathOp.Times, MathOp.Times, 5, 10),
                    new Math1Level(MathOp.Divide, MathOp.Divide, 5, 10),

                    new Math1Level(MathOp.Times, MathOp.Times, 10, 10),
                    new Math1Level(MathOp.Divide, MathOp.Divide, 10, 10),

                    new Math1Level(MathOp.Times, MathOp.Times, 12, 10),
                    new Math1Level(MathOp.Divide, MathOp.Divide, 12, 10),

                    new Math1Level(MathOp.Divide, MathOp.Times, 12, 30),

                    new Math1Level(MathOp.Divide, MathOp.Plus, 12, 200)
            }
    };
}
