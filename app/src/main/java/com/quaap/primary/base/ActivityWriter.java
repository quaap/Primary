package com.quaap.primary.base;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.quaap.primary.R;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by tom on 12/15/16.
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
public class ActivityWriter {

    private final SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    private final FileWriter mFw;


    public ActivityWriter(Context context, String username, String subject) throws IOException {

        username = username.replaceAll("\\W","_");


        SimpleDateFormat fileFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String fname = username + " " + subject + " " + fileFormat.format(new Date());
        fname = fname.replaceAll("[/\\\\.(){}$*|?<>\\[\\]]"," ");
        File f = new File(getAppDocumentsDir(context),  fname + ".csv");
        boolean newfile = !f.exists();
        mFw = new FileWriter(f, true);
        if (newfile) {
            writeRow(context.getString(R.string.csv_time), context.getString(R.string.csv_level), 
                    context.getString(R.string.csv_problem), context.getString(R.string.csv_answer), 
                    context.getString(R.string.csv_useranswer), context.getString(R.string.csv_correct), 
                    context.getString(R.string.csv_tspent), context.getString(R.string.csv_run_percent), 
                    context.getString(R.string.csv_points));
        }
    }

    public synchronized void close() throws IOException {
        mFw.close();
    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    private File getAppDocumentsDir(Context context) {
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS), context.getString(R.string.app_name));
        if (!file.exists() && !file.mkdirs()) {
            Log.e("Primary", "Directory could not be created");
        }
        return file;
    }

    private static String csvEscape(String value) {

        if (value==null) {
            return "";
        }
        if (value.length() ==0) {
            return "\"\"";
        }
        if (value.matches("^-?(\\d+(\\.\\d+)?|\\.\\d+)$")) {
            return value;
        }
        return "\"" + value.replaceAll("\"", "\"\"") + "\"";

    }


    public synchronized void log(int level, String problem, String answer, String useranswer, boolean correct, long millis, float runningpercent, int points) {
        writeRow(mDateFormat.format(new Date()), level, problem, answer, useranswer, correct,
                millis, String.format(Locale.getDefault(),"%4.1f",runningpercent), points );
    }


    private synchronized void writeRow(Object ... items) {
        try {
            for (int i=0; i<items.length; i++) {
                mFw.write(csvEscape(items[i].toString()));
                if (i<items.length-1) {
                    mFw.write(",");
                }
            }
            mFw.write("\n");
            mFw.flush();
        } catch (IOException e) {
            Log.e("Primary", "Could not write row.", e);
        }
    }
}
