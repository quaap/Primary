package com.quaap.primary.math;

import android.content.ClipData;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quaap.primary.R;
import com.quaap.primary.base.StdGameActivity;
import com.quaap.primary.base.SubjectBaseActivity;

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
public class SortingActivity extends StdGameActivity implements SubjectBaseActivity.AnswerGivenListener<Integer>,SubjectBaseActivity.AnswerTypedListener {

    public SortingActivity() {
        super(R.layout.std_sorting_prob);
    }

    @Override
    protected void showProbImpl() {
        LinearLayout sortArea = (LinearLayout)findViewById(R.id.sort_area);


        for (int i=0; i<3; i++) {
            TextView item = new TextView(this);
            item.setTextSize(36);

            int num = getRand(10);
            final String numstr = num + "";
            item.setText(numstr);
            item.setTag(num);
            item.setPadding(24, 2, 24, 2);
            item.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        ClipData data = ClipData.newPlainText(numstr, numstr);
                        View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                        view.startDrag(data, shadowBuilder, view, 0);
                        //view.setVisibility(View.INVISIBLE);
                        return true;
                    } else {
                        return false;
                    }
                }
            });
            sortArea.addView(item);

            item.setOnDragListener(new View.OnDragListener() {
                @Override
                public boolean onDrag(View view, DragEvent event) {

                    switch (event.getAction()) {
                        case DragEvent.ACTION_DRAG_STARTED:
                            // do nothing
                            break;
                        case DragEvent.ACTION_DRAG_ENTERED:
                            //view.setBackgroundDrawable(enterShape);
                            break;
                        case DragEvent.ACTION_DRAG_EXITED:
                            //view.setBackgroundDrawable(normalShape);
                            break;
                        case DragEvent.ACTION_DROP:
                            // Dropped, reassign View to ViewGroup
                            View view2 = (View) event.getLocalState();
                            ViewGroup owner = (ViewGroup) view2.getParent();

                            LinearLayout container = (LinearLayout) view.getParent();
                            int index = 0;
                            for (int i=0;i<container.getChildCount(); i++) {
                                if (container.getChildAt(i)==view) {
                                    index = i;
                                }
                            }

                            owner.removeView(view2);
                            container.addView(view2,index);
                            view.setVisibility(View.VISIBLE);
                            break;
                        case DragEvent.ACTION_DRAG_ENDED:
                            // view.setBackgroundDrawable(normalShape);
                        default:
                            break;
                    }
                    return true;
                }
            });
        }
    }

    @Override
    public boolean answerGiven(Integer answer) {
        return false;
    }

    @Override
    public boolean answerTyped(String answer) {
        return false;
    }
}
