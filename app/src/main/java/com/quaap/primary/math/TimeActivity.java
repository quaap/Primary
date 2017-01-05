package com.quaap.primary.math;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.quaap.primary.R;
import com.quaap.primary.base.StdGameActivity;
import com.quaap.primary.base.SubjectBaseActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by tom on 1/4/17.
 * <p>
 * Copyright (C) 2017  tom
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
public class TimeActivity extends StdGameActivity implements SubjectBaseActivity.AnswerGivenListener {

    private int mClockwidth = 300;
    private int mHour;
    private int mMinute;

    public TimeActivity() {
        super(R.layout.std_time_prob);
        setFasttimes(900, 1800, 3000);
    }


    @Override
    protected void onResume() {
        Point ss = getScreenSize();
        mClockwidth = Math.min(ss.x, ss.y)/2;
        super.onResume();

    }

    @Override
    protected void showProbImpl() {
        mHour = getRand(1,12);
        mMinute = getMinutes();

        Bitmap bitmap = getClockBitmap(mHour, mMinute);

        ImageView timeimage = (ImageView) findViewById(R.id.timeimage);
        timeimage.setImageBitmap(bitmap);

        Set<String> answerset = new TreeSet<>();
        answerset.add(formatTime(mHour,mMinute));

        do {
            int thour = getRand(1,12);
            int tminute = getMinutes();
            if (getRand(10)>8 && mHour<12) {
                thour = mHour + 1;
                tminute = mMinute;
            } else if (getRand(10)>7 && mHour>1) {
                thour = mHour - 1;
                tminute = mMinute;
            } else if (getRand(10)>5) {
                int t = mHour;
                if (mMinute==0) {
                    thour = 12;
                } else {
                    thour = (mMinute) / 5;
                }
                if (((TimeLevel)getLevel()).getMinuteGranularity()!= TimeLevel.MinuteGranularity.Hour) {
                    tminute = t * 5;
                }
            }
            if (!(thour == mHour && Math.abs(tminute - mMinute)<10)) {
                answerset.add(formatTime(thour,tminute));
            }
        } while (answerset.size()<4);

        List<String> answers = new ArrayList<>(answerset);
        Collections.shuffle(answers);
        makeChoiceButtons(getAnswerArea(), answers, this);

    }


    private int getMinutes() {
        int tminute;
        switch ( ((TimeLevel)getLevel()).getMinuteGranularity() ) {
            case Hour:
                tminute = 0;
                break;
            case Half:
                tminute = getRand(1)*30;
                break;
            case Quarter:
                tminute = getRand(3)*15;
                break;
            case Five:
                tminute = getRand(11)*5;
                break;
            case One:
            default:
                tminute = getRand(59);
                break;
        }
        return tminute;
    }

    @Override
    public boolean answerGiven(Object answer) {
        String ranswer = formatTime(mHour,mMinute);
        boolean isright = ranswer.equals((String)answer);
        answerDone(isright,20,ranswer,ranswer,(String)answer);
        return isright;
    }


    public static String formatTime(int hour, int minute) {
        return String.format(Locale.getDefault(),"%d:%02d", hour, minute);
    }
    @NonNull
    private Bitmap getClockBitmap(int hour, int minute) {
        Paint black = new Paint();
        black.setARGB(255,0,0,0);
        black.setStrokeWidth(mClockwidth /60);
        black.setStyle(Paint.Style.STROKE);

        Paint hourColor = new Paint();
        hourColor.setARGB(255,0,0,200);
        hourColor.setStrokeWidth(mClockwidth /40);
        hourColor.setStyle(Paint.Style.STROKE);

        Paint hourColorThin = new Paint(hourColor);
        hourColorThin.setStrokeWidth(3);
        hourColorThin.setTextSize(28);
        hourColorThin.setTextAlign(Paint.Align.CENTER);

        Paint minuteColor = new Paint();
        minuteColor.setARGB(255,0,200,0);
        minuteColor.setStrokeWidth(mClockwidth /52);
        minuteColor.setStyle(Paint.Style.STROKE);

        Paint minuteColorThin = new Paint(minuteColor);
        minuteColorThin.setStrokeWidth(2);

        int centerX = mClockwidth /2;
        int centerY = mClockwidth /2;
        int radius = mClockwidth /2 - 10;

        double hourRadian = 2 * Math.PI / 12;
        double minuteRadian = 2 * Math.PI / 60;

        Bitmap bitmap = Bitmap.createBitmap(mClockwidth, mClockwidth, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        for (int mintick=1; mintick<=60; mintick++) {

            float dX = (float)(Math.cos(minuteRadian * mintick - Math.PI/2));
            float dY = (float)(Math.sin(minuteRadian * mintick- Math.PI/2));

            canvas.drawLine(centerX + dX*radius, centerY + dY*radius, centerX + dX*(radius-10), centerY + dY*(radius-10), minuteColorThin);
            if (mintick % 5 == 0) {
                float iX = centerX + dX * (radius - 15);
                float iY = centerY + dY * (radius - 15);
                float itX = centerX + dX * (radius - 30);
                float itY = centerY + dY * (radius - 30) + 10;
                canvas.drawLine(centerX + dX * radius, centerY + dY * radius, iX, iY, hourColorThin);
                canvas.drawText((mintick/5)+"", itX, itY, hourColorThin);

            }
        }

        double hourAdj = minute/60.0;

        float hX = (float)(Math.cos(hourRadian * (hour+hourAdj) - Math.PI/2) * (radius*.5));
        float hY = (float)(Math.sin(hourRadian * (hour+hourAdj) - Math.PI/2) * (radius*.5));
        canvas.drawLine(centerX, centerY, centerX+hX, centerY+hY, hourColor);

        float mX = (float)(Math.cos(minuteRadian * minute - Math.PI/2) * (radius*.7));
        float mY = (float)(Math.sin(minuteRadian * minute - Math.PI/2) * (radius*.7));
        canvas.drawLine(centerX, centerY, centerX+mX, centerY+mY, minuteColor);

        canvas.drawCircle(centerX, centerY, 4, black);
        canvas.drawCircle(centerX, centerY, radius, black);
        return bitmap;
    }


}
