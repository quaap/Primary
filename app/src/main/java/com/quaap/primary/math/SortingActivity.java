package com.quaap.primary.math;

import android.content.ClipData;
import android.graphics.Color;

import android.graphics.Typeface;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quaap.primary.R;
import com.quaap.primary.base.StdGameActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by tom on 1/1/17.
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
public class SortingActivity extends StdGameActivity implements
        View.OnTouchListener, View.OnDragListener{

    public static final int BACKGROUND_COLOR = Color.rgb(200, 200, 200);

    private List<Integer> numlist;
    public SortingActivity() {
        super(R.layout.std_sorting_prob);
        setUseInARow(false);
    }

    private boolean problemDone = false;

    private int moves = 0;

    private int numcolumns = 3;


    @Override
    protected void onResume() {
        super.onResume();
        GridLayout sortArea = (GridLayout)findViewById(R.id.sort_area);
        if (isLandscape()){
            sortArea.setColumnCount(6);
            numcolumns = 6;
        } else {
            sortArea.setColumnCount(3);
            numcolumns = 3;
        }

        //override super class
        LinearLayout centercol = (LinearLayout)findViewById(R.id.centercol);
        centercol.setOrientation(LinearLayout.VERTICAL);
    }

    @Override
    protected void showProbImpl() {
        GridLayout sortArea = (GridLayout)findViewById(R.id.sort_area);
        sortArea.removeAllViews();

        sortArea.setOnDragListener(this);

        Set<Integer> nums = new TreeSet<>();

        SortingLevel level = ((SortingLevel)getLevel());
        int tries = 0;
        do {
            nums.add(getRand(level.getMaxNum()));
        } while (tries++<100 && nums.size()<level.getNumItems());

        numlist = new ArrayList<>(nums);

        do {
            Collections.shuffle(numlist);
        } while (isSorted(numlist));

        int mlen = (level.getMaxNum()+"").length();
//        int xsize = getScreenSize().x;
//
//        int tsize = xsize*2/3 / numcolumns / 4 - 5;
//        if (tsize>30) tsize = 30;
        int tsize = 30 - mlen;

        for(int num: numlist){
            String spaces = "";
            for (int i=0; i<Math.max(3,mlen)-(num+"").length();i++) {
                spaces += " ";
            }

            final String numstr = spaces + num;

            TextView item = new TextView(this);
            item.setText(numstr);
            item.setTag(num);
            item.setOnTouchListener(this);
            item.setOnDragListener(this);


            item.setMaxLines(1);
            item.setTextSize(tsize);
            item.setTypeface(Typeface.MONOSPACE);
            //item.setWidth(xsize*2/3 / numcolumns -10);
            //item.setEms(mlen);

            item.setGravity(Gravity.END);

            GridLayout.LayoutParams lp = new GridLayout.LayoutParams();

            lp.setMargins(1,1,1,1);
            lp.setGravity(Gravity.CENTER);
            item.setLayoutParams(lp);
            item.setBackgroundColor(BACKGROUND_COLOR);
            item.setPadding(16, 4, 16, 4);

            sortArea.addView(item);
        }
        problemDone = false;
        moves = 0;
        int n = level.getNumItems()*250;
        setFasttimes(300 + n, 1000 + n, 2000 + n);
    }



    @Override
    public boolean onDrag(View view, DragEvent event) {

        boolean islayout = view instanceof GridLayout;
        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:
                // do nothing
                break;
            case DragEvent.ACTION_DRAG_ENTERED:
                if (!islayout) {
                    view.setBackgroundColor(Color.BLUE);
                    //view.setBackgroundResource(R.drawable.sort_drop_target);
                }
                break;
            case DragEvent.ACTION_DRAG_EXITED:

                if (!islayout) view.setBackgroundColor(BACKGROUND_COLOR);
                break;
            case DragEvent.ACTION_DROP:
                if (!islayout) view.setBackgroundColor(BACKGROUND_COLOR);
                // Dropped, reassign View to ViewGroup
                View view2 = (View) event.getLocalState();
                if (view2 == view) {
                   // Log.d("sort", "self drop");
                    break;
                }

                ViewGroup owner = (ViewGroup) view2.getParent();

                GridLayout container;
                if (view instanceof GridLayout) {
                    container = (GridLayout)view;

                } else {
                    container = (GridLayout) view.getParent();

                }

                int index = -1;
                for (int i=0;i<container.getChildCount(); i++) {
                    if (container.getChildAt(i)==view) {
                        index = i;
                    }
                }

                owner.removeView(view2);
                if (index==-1) {
                    container.addView(view2);
                } else {
                    container.addView(view2, index);
                }
                moves++;
                checkDone();

                break;
            case DragEvent.ACTION_DRAG_ENDED:
                if (!islayout) view.setBackgroundColor(BACKGROUND_COLOR);
                break;
            default:
                break;
        }
        return true;
    }


    private void checkDone() {
        if (!problemDone) {
            final GridLayout sortArea = (GridLayout) findViewById(R.id.sort_area);

            boolean sorted = true;

            for (int i = 0; i < sortArea.getChildCount()-1; i++) {
                int num1 = (int) sortArea.getChildAt(i).getTag();
                int num2 = (int) sortArea.getChildAt(i+1).getTag();
                if (num1 > num2) {
                    sorted = false;
                    break;
                }

            }
            if (sorted) {
                problemDone = true;
                stopTimer();

                for (int i = 0; i < sortArea.getChildCount(); i++) {
                    final int c=i;
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            sortArea.getChildAt(c).setBackgroundColor(Color.GREEN);
                        }
                    }, (c+1)*1000 / sortArea.getChildCount());
                }


                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finishDone();
                    }
                }, 1200);
            }
        }

    }

    private void finishDone() {
        String prob = "";
        for (int p : numlist) {
            prob += p + ";";
        }

        SortingLevel level = ((SortingLevel)getLevel());

        answerDone(true, (int)(Math.sqrt(level.getMaxNum())*level.getNumItems()/moves), prob, "sorted", "sorted");

    }

    private boolean isSorted(List<Integer> list) {
        boolean sorted = true;

        for (int i = 0; i < list.size()-1; i++) {
            if (list.get(i) > list.get(i+1)) {
                sorted = false;
                break;
            }
        }
        return sorted;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (!problemDone && motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            ClipData data = ClipData.newPlainText(view.getTag().toString(), view.getTag().toString());
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
            view.startDrag(data, shadowBuilder, view, 0);
            //view.setVisibility(View.INVISIBLE);
            return true;
        } else {
            return false;
        }
    }
}
