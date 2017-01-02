package com.quaap.primary.base.data;

import android.content.Context;

import com.quaap.primary.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tom on 12/25/16.
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

public class SubjectsOld {

    private static SubjectsOld inst;

    public synchronized static SubjectsOld getInstance(Context context) {
        if (inst == null) {
            inst = new SubjectsOld(context);
        }
        return inst;
    }


    private SubjectOld[] subjects;
    private Map<String, SubjectOld> subjectMap = new HashMap<>();

    public SubjectsOld(Context context) {
        subjects = loadSubjects(context);
        for (SubjectOld subject : subjects) {
            subjectMap.put(subject.getCode(), subject);
        }
    }

    public List<String> getCodes() {
        List<String> codes = new ArrayList<>();
        for (SubjectOld d : subjects) {
            codes.add(d.getCode());
        }
        return codes;
    }

    public int getCount() {
        return subjects.length;
    }

    private static String[] getArray(Context context, int arrayid) {
        return context.getResources().getStringArray(arrayid);
    }

    public SubjectOld get(String code) {
        return subjectMap.get(code);
    }

    public SubjectOld get(int num) {
        return subjects[num];
    }

    public static SubjectOld[] loadSubjects(Context context) {
        String[] codes = getArray(context, R.array.subjects);
        SubjectOld[] subjects = new SubjectOld[codes.length];
        for (int i = 0; i < codes.length; i++) {
            subjects[i] = new SubjectOld(context, i);
        }
        return subjects;
    }
}
