package com.quaap.primary;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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

    private LinearLayout mBaseLayout;

    private Activity mParent;


    private int mItemLayoutId;
    private View mHorzList;
    private LinearLayout mItemsListView;

    private Map<String, LinearLayout> mListItems = new HashMap<>();
    private Map<LinearLayout, String> mListItemsRev = new HashMap<>();

    private String selected;

    public HorzItemList(Activity parent, int includeID, int itemLayoutId) {
        mParent = parent;

        mItemLayoutId = itemLayoutId;
//        mBaseLayout = (LinearLayout)LayoutInflater.from(context).inflate(R.layout.horz_list_view, (ViewGroup)parent);


        mHorzList = parent.findViewById(includeID);
        mItemsListView = (LinearLayout)mHorzList.findViewById(R.id.items_list_area);
        ImageView newbutton = (ImageView)mHorzList.findViewById(R.id.add_list_item_button);
        newbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onNewItemClicked();
            }
        });
    }

    public LinearLayout addItem(String name, int itemNameId) {
        if (mListItems.containsKey(name)) {
            return mListItems.get(name);
        }
        LinearLayout item = (LinearLayout)LayoutInflater.from(mParent).inflate(mItemLayoutId, (ViewGroup)null);
        setItemTextField(item, itemNameId, name);
        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSelected(view);
                onItemClicked(mListItemsRev.get(view), (LinearLayout)view);
            }
        });
        item.setTag(name);
        mItemsListView.addView(item);
        mListItems.put(name, item);
        mListItemsRev.put(item, name);

        return item;
    }

    public void removeItem(String name) {
        if (!mListItems.containsKey(name)) {
            return;
        }
        LinearLayout item = mListItems.get(name);
        mItemsListView.removeView(item);
        mListItemsRev.remove(item);
        mListItems.remove(name);

    }

    public void setItemTextField(View item, int itemFieldId, String value) {
        TextView itemfield = (TextView)item.findViewById(itemFieldId);
        itemfield.setText(value);

    }

    public Collection<String> getNames() {
        return Collections.unmodifiableSet(mListItems.keySet());
    }

    public LinearLayout getItem(String name) {
        return mListItems.get(name);
    }

    protected abstract void onNewItemClicked();

    protected abstract void onItemClicked(String name, LinearLayout item);

    public String getSelected() {
        return selected;
    }

    private void setSelected(View item) {
        setSelected(mListItemsRev.get(item));
    }

    public void setSelected(String name) {

        final int normalColor = Color.TRANSPARENT;
        final int selectedColor = Color.CYAN;

        if (selected!=null) {
            View old_selected = mListItems.get(selected);
            if (old_selected!=null) {
                old_selected.setBackgroundColor(normalColor);
            }
        }
        selected = name;
        if (selected!=null) {
            View new_selected = mListItems.get(selected);
            if (new_selected!=null) {
                new_selected.setBackgroundColor(selectedColor);
            }
        }
    }
}
