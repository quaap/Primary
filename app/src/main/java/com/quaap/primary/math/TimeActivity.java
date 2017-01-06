package com.quaap.primary.math;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.quaap.primary.R;
import com.quaap.primary.base.StdGameActivity;
import com.quaap.primary.base.SubjectBaseActivity;
import com.quaap.primary.base.component.InputMode;

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
public class TimeActivity extends StdGameActivity implements SubjectBaseActivity.AnswerGivenListener, SubjectBaseActivity.AnswerTypedListener {

    private int mClockwidth = 300;
    private int mHour;
    private int mMinute;

    public TimeActivity() {
        super(R.layout.std_time_prob);

    }

    @Override
    protected void onPause() {
        super.onPause();
        saveLevelValue("mHour", mHour);
        saveLevelValue("mMinute", mMinute);
    }

    @Override
    protected void onResume() {
        Point ss = getScreenSize();
        mClockwidth = Math.min(ss.x, ss.y)/2;
        addToNumpadKeyMap(".",":");
        setMaxSeenSize(12);
        super.onResume();

    }


    @Override
    protected void onShowLevel() {
        super.onShowLevel();
        clearSeenProblem();
        TimeLevel level = (TimeLevel)getLevel();
        if (level.useFuzzy() && level.getMinuteGranularity()== TimeLevel.MinuteGranularity.One) {
            TextView t = (TextView)findViewById(R.id.txt_time_header);
            t.setText(R.string.fuzzy_closest_message);
        }
        if (level.getInputMode() == InputMode.Buttons) {
            setFasttimes(800, 1600, 3000);
        } else {
            setFasttimes(1500, 2200, 5000);
        }

    }

    @Override
    protected void onShowProbImpl() {
        mHour = getSavedLevelValue("mHour", -1);
        mMinute = getSavedLevelValue("mMinute", -1);
        if (mMinute ==-1 || mHour ==-1 ) {
            int tries = 0;
            do {
                mHour = getRand(1, 12);
                mMinute = getMinutes();
            } while (tries++ < 100 && seenProblem(mHour, mMinute));
        } else {
            deleteSavedLevelValue("mHour");
            deleteSavedLevelValue("mMinute");
        }

        TimeLevel level = (TimeLevel)getLevel();
        Bitmap bitmap = getClockBitmap(mHour, mMinute);

        ImageView timeimage = (ImageView) findViewById(R.id.timeimage);
        timeimage.setImageBitmap(bitmap);

        List<String> answers = getAnswers(level);

        Collections.shuffle(answers);

        if (level.getInputMode()== InputMode.Buttons) {
            makeChoiceButtons(getAnswerArea(), answers, this);
        } else if (level.getInputMode()== InputMode.Input) {
            makeInputBox(getAnswerArea(), getKeysArea(), this, INPUTTYPE_TIME, 3, 0);
            startHint(level.getLevelNum()+1);
        }
        TextView timeHint = (TextView) findViewById(R.id.timeHint);
        timeHint.setText("");

    }

    @NonNull
    private List<String> getAnswers(TimeLevel level) {
        Set<String> answerset = new TreeSet<>();
        answerset.add(formatTime(mHour,mMinute));

        int mindiff = 8;
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
                    thour = (mMinute+5) / 5;
                }
                if (level.getMinuteGranularity()!= TimeLevel.MinuteGranularity.Hour) {
                    tminute = (t-1) * 5;
                }
            } else if (getRand(10)>6 && (
                       level.getMinuteGranularity()== TimeLevel.MinuteGranularity.Five
                    || level.getMinuteGranularity()== TimeLevel.MinuteGranularity.One)) {
                thour = mHour;
                if (mMinute<60-mindiff*2 && getRand(10)>5) {
                    tminute = mMinute + getRand(mindiff, mindiff*2);

                } else if (mMinute>mindiff*2 && getRand(10)>5) {
                    tminute = mMinute - getRand(mindiff, mindiff*2);
                }
            }
            if (!(thour == mHour && Math.abs(tminute - mMinute)<mindiff) ) {
                answerset.add(formatTime(thour,tminute));
            }
        } while (answerset.size()<4);

        return new ArrayList<>(answerset);
    }

    private String makeFuzzyTime(String time) {

        String fuzzytime = "";
        String [] tparts = time.split(":");
        int hour = Integer.parseInt(tparts[0]);
        int min = Integer.parseInt(tparts[1]);

        int disphour = hour;
        int dispmin = min;

        boolean noMins = false;
        String befaft = "";
        if (min <= 27) {
            befaft = getString(R.string.fuzzy_after);
            disphour = hour;
            dispmin = min;
        } else if (min >= 33) {
            befaft = getString(R.string.fuzzy_till);
            disphour = hour==12 ? 1 : hour+1;
            dispmin = 60 - min;

        } else {
            befaft = getString(R.string.fuzzy_half_past);
            noMins = true;
        }
        if (min >3 && min<57) {
            for (int t = 5; t < 31; t += 5) {
                if (Math.abs(dispmin - t) <= 2) {
                    if (!noMins) {
                        if (t==15 || t==45) {
                            fuzzytime = getString(R.string.fuzzy_quarter);
                        } else {
                            fuzzytime = t + "";
                        }
                    }
                    fuzzytime += befaft + disphour;
                    break;
                }
            }
        } else {
            fuzzytime = disphour + getString(R.string.fuzzy_o_clock);
        }

        if (fuzzytime.length() == 0) {
            Log.d("Timeact", "zero length for " + time);
            fuzzytime = "0";
        }
        return fuzzytime;
    }

    @Override
    protected void onPerformHint(int hintTick) {
        String time = formatTime(mHour,mMinute);
        if (hintTick <time.length()) {
            TextView timeHint = (TextView) findViewById(R.id.timeHint);
            timeHint.setText(time.substring(0, hintTick+1));
        }
        super.onPerformHint(hintTick);
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
                tminute = getRand(1,3)*15;
                break;
            case Five:
                tminute = getRand(1,11)*5;
                break;
            case One:
            default:
                tminute = getRand(1,59);
                break;
        }
        return tminute;
    }


    @Override
    public boolean onAnswerTyped(String answer) {
        return onAnswerGiven(answer);
    }

    @Override
    public boolean onAnswerGiven(Object answer) {


        String ranswer = formatTime(mHour,mMinute);
        String uanswer = ((String)answer);

        //Allow user to just type 9 instead of 9:00
        //I might add seconds later, the double replace doesn't hurt now.
        String ranswer2 = ranswer.replaceFirst(":00$","").replaceFirst(":00$","");
        String uanswer2 = uanswer.replaceFirst(":00$","").replaceFirst(":00$","");

        boolean isright = ranswer2.equals(uanswer2);

        answerDone(isright, ranswer, ranswer, uanswer);
        return isright;
    }

    /**
     *  Calculate the points from the current problem
     *  By default this is based on simply the level number.
     *
     *  Override this in each activity.
     *
     *  Range, in general:
     *    difficulty 1:   1 - 50
     *    difficulty 2:   up to 100
     *    difficulty 3:   up to 200
     *    difficulty 4:   up to 500
     *    difficulty 5:   up to 1000
     *
     * @return the points for the current problem
     */
    @Override
    protected int onCalculatePoints() {

        TimeLevel level = (TimeLevel)getLevel();
        return super.onCalculatePoints() * 50 * (level.getMinuteGranularity().ordinal()+1);
    }

    public String formatTime(int hour, int minute) {
        String time = String.format(Locale.getDefault(),"%d:%02d", hour, minute);
        TimeLevel level = (TimeLevel)getLevel();
        if (level.useFuzzy()) {
            time = makeFuzzyTime(time);
        }
        return time;
    }

    private Bitmap getClockBitmap(int hour, int minute) {
        return getClockBitmap(hour, minute, -1);
    }

    private Bitmap getClockBitmap(int hour, int minute, int second) {
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
        minuteColorThin.setStrokeWidth(3);


        Paint secondColor = new Paint();
        secondColor.setARGB(255,180,0,64);
        secondColor.setStrokeWidth(2);
        secondColor.setStyle(Paint.Style.STROKE);

        int centerX = mClockwidth /2;
        int centerY = mClockwidth /2;
        int radius = mClockwidth /2 - 10;

        double hourRadian = 2 * Math.PI / 12;
        double minuteRadian = 2 * Math.PI / 60;
        double secondRadian = 2 * Math.PI / 60;

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

        double minuteAdj = 0;
        if (second!=-1) {
            minuteAdj = second/60.0;
        }

        float mX = (float)(Math.cos(minuteRadian * (minute+minuteAdj) - Math.PI/2) * (radius*.8));
        float mY = (float)(Math.sin(minuteRadian * (minute+minuteAdj) - Math.PI/2) * (radius*.8));
        canvas.drawLine(centerX, centerY, centerX+mX, centerY+mY, minuteColor);

        if (second!=-1) {
            float sX = (float) (Math.cos(secondRadian * second - Math.PI / 2) * (radius * .95));
            float sY = (float) (Math.sin(secondRadian * second - Math.PI / 2) * (radius * .95));
            canvas.drawLine(centerX, centerY, centerX + sX, centerY + sY, secondColor);
        }

        canvas.drawCircle(centerX, centerY, 4, black);
        canvas.drawCircle(centerX, centerY, radius, black);
        return bitmap;
    }


}
