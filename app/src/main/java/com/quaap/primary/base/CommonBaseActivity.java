package com.quaap.primary.base;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.quaap.primary.AboutActivity;
import com.quaap.primary.R;
import com.quaap.primary.ScoresActivity;
import com.quaap.primary.SettingsActivity;

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
public class CommonBaseActivity extends AppCompatActivity {

    // Things here are common to ALL activities.

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        Intent intent = null;
        switch (id) {
            case R.id.menu_about:
                intent = new Intent(this, AboutActivity.class);
                break;
            case R.id.menu_scores:
                intent = new Intent(this, ScoresActivity.class);
                break;
            case R.id.menu_settings:
                intent = new Intent(this, SettingsActivity.class);
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
