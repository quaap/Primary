package com.quaap.primary.base.component;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quaap.primary.R;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by tom on 12/20/16.
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
public abstract class HorzItemList {
    //This could be a custom view


    private final int normalColor = Color.argb(64, 200, 200, 200);
    private final Activity mParent;
    private final int mItemLayoutId;
    private final View mHorzList;
    private final LinearLayout mItemsListView;
    private final Map<String, ViewGroup> mListItems = new HashMap<>();
    private final Map<ViewGroup, String> mListItemsRev = new HashMap<>();
    private String selected;

    public HorzItemList(Activity parent, int includeID, int itemLayoutId) {
        this(parent, includeID, itemLayoutId, null);
    }

    public HorzItemList(Activity parent, int includeID, int itemLayoutId, String[] itemkeys) {
        mParent = parent;

        mItemLayoutId = itemLayoutId;

        mHorzList = parent.findViewById(includeID);
        mItemsListView = mHorzList.findViewById(R.id.items_list_area);
        ImageView newbutton = mHorzList.findViewById(R.id.add_list_item_button);
        newbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onNewItemClicked();
            }
        });
        populate(itemkeys);
    }

    public void populate(String[] itemkeys) {
        if (itemkeys != null) {
            for (int i = 0; i < itemkeys.length; i++) {
                addItem(i, itemkeys[i]);
            }
        }
    }

    public void showAddButton(boolean show) {
        mHorzList.findViewById(R.id.add_list_item_button).setVisibility(show ? View.VISIBLE : View.GONE);
    }

    public ViewGroup addItem(String key) {
        return addItem(-1, key);
    }

    private ViewGroup addItem(int pos, String key) {
        if (mListItems.containsKey(key)) {
            return mListItems.get(key);
        }
        ViewGroup item = (ViewGroup) LayoutInflater.from(mParent).inflate(mItemLayoutId, null);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(10, 10, 10, 10);
        item.setLayoutParams(lp);

        item.setBackgroundColor(normalColor);

        if (pos == -1) pos = mListItems.size();
        populateItem(key, item, pos);


        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSelected(view);
                onItemClicked(mListItemsRev.get(view), (LinearLayout) view);
            }
        });
        item.setTag(key);
        mItemsListView.addView(item);
        mListItems.put(key, item);
        mListItemsRev.put(item, key);

        return item;
    }

    private void setBackground(View item, int drawableId) {
        if (Build.VERSION.SDK_INT >= 21) {
            item.setBackground(mParent.getResources().getDrawable(android.R.drawable.btn_default, mParent.getTheme()));
        } else {
            item.setBackground(mParent.getResources().getDrawable(android.R.drawable.btn_default));
        }

    }

    public void removeItem(String key) {
        if (!mListItems.containsKey(key)) {
            return;
        }
        ViewGroup item = mListItems.get(key);
        mItemsListView.removeView(item);
        mListItemsRev.remove(item);
        mListItems.remove(key);
        if (key.equals(selected)) {
            setSelected((String) null);
        }

    }

    public void clear() {
        mItemsListView.removeAllViews();
        mListItems.clear();
        mListItemsRev.clear();
        selected = null;
    }

    public void setItemTextField(View item, int itemFieldId, String value) {
        TextView itemfield = item.findViewById(itemFieldId);
        itemfield.setText(value);
    }

    public void setItemBackground(View item, int itemFieldId, int color) {
        View itemfield = item.findViewById(itemFieldId);
        itemfield.setBackgroundColor(color);
    }

    public Collection<String> getKeys() {
        return Collections.unmodifiableSet(mListItems.keySet());
    }

    public ViewGroup getItem(String key) {
        return mListItems.get(key);
    }

    protected void onNewItemClicked() {

    }

    protected void onItemClicked(String key, ViewGroup item) {

    }
//    protected abstract void onNewItemClicked();
//
//    protected abstract void onItemClicked(String key, ViewGroup item);
//
//    protected abstract void populateItem(String key, ViewGroup item, int i);

    protected void populateItem(String key, ViewGroup item, int i) {

    }

    public boolean hasSelected() {
        return selected != null;
    }

    public String getSelected() {
        return selected;
    }

    public void setSelected(String key) {

        final int selectedColor = Color.CYAN;

        if (selected != null) {
            View old_selected = mListItems.get(selected);
            if (old_selected != null) {
                old_selected.setBackgroundColor(normalColor);
            }
        }
        selected = key;
        if (selected != null) {
            HorizontalScrollView hsv = mHorzList.findViewById(R.id.horz_list_scroller);
            View new_selected = mListItems.get(selected);
//            boolean nextone = false;
//            for (String sk: mListItems.keySet()) {
//                if (nextone) {
//                    hsv.requestChildFocus(mListItems.get(sk), mListItems.get(sk));
//                    break;
//                }
//                if (sk.equals(selected)) {
//                    nextone = true;
//                }
//            }
            if (new_selected != null) {
                new_selected.setBackgroundColor(selectedColor);
                hsv.requestChildFocus(new_selected, new_selected);
                // focusChild(hsv, new_selected);
            }
        }
    }

    private void setSelected(View item) {
        setSelected(mListItemsRev.get(item));
    }

//    private void focusChild(final HorizontalScrollView scroll, final View view) {
//        new Handler().post(new Runnable() {
//            @Override
//            public void run() {
//                int left = view.getLeft();
//                int right = view.getRight();
//                int width = scroll.getWidth();
//                scroll.smoothScrollTo(((left + right - width) / 2), 0);
//            }
//        });
//    }
}
