package com.quaap.primary.base.component;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;

import com.quaap.primary.R;

import java.lang.reflect.Method;

/**
 * Created by tom on 12/29/16.
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
public class Keyboard {

    //private static Keyboard keyboardinst;
    //private static Keyboard keypadinst;

    public synchronized static void showKeyboard(Context context, final EditText editText, ViewGroup parentlayout) {
        new Keyboard(context).showKeyboard(editText, parentlayout);
    }

    public synchronized static void showNumberpad(Context context, final EditText editText, ViewGroup parentlayout) {
        new Keyboard(context).showNumberpad(editText, parentlayout);
    }


    public synchronized static void hideKeys(ViewGroup parentlayout) {
        parentlayout.removeAllViews();

    }

    private Context mContext;

    public Keyboard(Context context) {
        mContext = context;
    }

    private static String KEY_BACKSP = "\u0008";
    private static String KEY_DONE = "\n";

    protected void showKeyboard(final EditText editText, ViewGroup parentlayout) {
        String [] keys = mContext.getResources().getStringArray(R.array.keyboard_keys);
        int rows = mContext.getResources().getInteger(R.integer.keyboard_rows);

        showKeys(editText, parentlayout, keys, rows);
    }


    protected void showNumberpad(final EditText editText, ViewGroup parentlayout) {
        String [] keys = mContext.getResources().getStringArray(R.array.keypad_keys);
        int rows = mContext.getResources().getInteger(R.integer.keypad_rows);

        showKeys(editText, parentlayout, keys, rows);
    }

    protected void showKeys(final EditText editText, ViewGroup parentlayout, String[] keys, int rows) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            editText.setShowSoftInputOnFocus(false);
        } else {
            try {
                final Method method = EditText.class.getMethod(
                        "setShowSoftInputOnFocus"
                        , new Class[]{boolean.class});
                method.setAccessible(true);
                method.invoke(editText, false);
            } catch (Exception e) {
                // ignore
            }
        }

        parentlayout.removeAllViews();
        GridLayout glayout = new GridLayout(mContext);
        glayout.setBackgroundColor(Color.WHITE);
        glayout.setPadding(2,2,2,2);

        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        int cols = keys.length/rows;
        if (keys.length%2!=0) cols+=1;
        glayout.setColumnCount(cols);

        float xfac = .95f;
        int orientation = mContext.getResources().getConfiguration().orientation;
        if (orientation== Configuration.ORIENTATION_LANDSCAPE) {
            xfac = .83f;
        }

        int keywidth = (int)(size.x/cols * xfac);
        int keyheight = (int)(keywidth*1.5);

        if (keyheight>100) keyheight=100;

        System.out.println("size: " + size.x + ", " + size.y);

        for (String k: keys) {
            TextView key = new TextView(mContext);
            key.setClickable(true);
            key.setPadding(4, 4 ,4, 4);
            key.setGravity(Gravity.CENTER);
            key.setBackgroundResource(android.R.drawable.btn_default_small);
            key.setTextSize((int)(keyheight/3.5));
            key.setMinimumWidth(0);
            key.setMinimumHeight(0);
            key.setHeight(keyheight);
            key.setWidth(keywidth);

            if (k.equals(KEY_BACKSP)) {
                key.setText("\u2190");
                //key.setWidth(keywidth+5);
            } else if (k.equals(KEY_DONE)) {
                key.setText("\u2713");
                key.setTextColor(Color.rgb(0,160,0));
                //key.setWidth(keywidth+5);
            } else if (k.equals(" ")) {
                key.setText("\u2423");
            } else {
                key.setText(k);
            }
            key.setTag(k);
            key.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String k = (String)view.getTag();
                    if (k.equals(KEY_BACKSP)) {
                        editText.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
                    } else if (k.equals(KEY_DONE)) {
                        editText.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                    } else {
                        editText.getText().insert(editText.getSelectionStart(),k);


                    }

                }
            });
            glayout.addView(key);
        }

        parentlayout.addView(glayout);
    }

}
