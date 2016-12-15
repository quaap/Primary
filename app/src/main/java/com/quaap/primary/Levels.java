package com.quaap.primary;

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

    public static final Math1Level [] Math1Levels = new Math1Level [] {
            new Math1Level(MathOp.Plus, MathOp.Plus, 5,  10),
            new Math1Level(MathOp.Minus, MathOp.Minus, 5, 10),

            new Math1Level(MathOp.Plus, MathOp.Plus, 10,  20),
            new Math1Level(MathOp.Minus, MathOp.Minus, 10, 20),

            new Math1Level(MathOp.Plus, MathOp.Plus, 15,  20),
            new Math1Level(MathOp.Minus, MathOp.Minus, 15, 20),

            new Math1Level(MathOp.Plus, MathOp.Plus, 25,  10),
            new Math1Level(MathOp.Minus, MathOp.Minus, 25,  10),

            new Math1Level(MathOp.Times, MathOp.Times, 5, 10),
            new Math1Level(MathOp.Divide, MathOp.Divide, 5, 10),

            new Math1Level(MathOp.Times, MathOp.Times, 10, 10),
            new Math1Level(MathOp.Divide, MathOp.Divide, 10, 10),

            new Math1Level(MathOp.Times, MathOp.Times, 12, 10),
            new Math1Level(MathOp.Divide, MathOp.Divide, 12, 10),

            new Math1Level(MathOp.Divide, MathOp.Times, 12, 30),

            new Math1Level(MathOp.Divide, MathOp.Plus, 12, 200),

    };
}
