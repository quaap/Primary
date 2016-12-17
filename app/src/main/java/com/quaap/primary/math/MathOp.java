package com.quaap.primary.math;

import java.security.SecureRandom;

/**
 * Created by tom on 12/14/16.
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
public enum MathOp {
    Plus("+"),
    Minus("-"),
    Times("\u00D7"),
    Divide("\u00F7");

    private final String mDisplay;
    MathOp(String display) {
        mDisplay = display;
    }

    @Override
    public String toString() {
        return mDisplay;
    }



    public static MathOp random(MathOp upto) {
        return randomEnum(MathOp.class, MathOp.Plus, upto);
    }

    public static MathOp random(MathOp start, MathOp upto) {
        return randomEnum(MathOp.class, start, upto);
    }

    private static <T extends Enum<?>> T randomEnum(Class<T> clazz, T start, T upto){

        int max = upto.ordinal();
        int min = start.ordinal();

        int x = random.nextInt(max - min +1 ) + min;
        return clazz.getEnumConstants()[x];
    }
    private static final SecureRandom random = new SecureRandom();
}
