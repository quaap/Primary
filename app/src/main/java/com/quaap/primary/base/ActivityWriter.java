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
public class ActivityWriter {

    private final SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    private final SimpleDateFormat mFileFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    private final FileWriter mFw;
    private final String mSubject;
    private final Context mContext;

    public ActivityWriter(Context context, String subject) throws IOException {
        mSubject = subject;
        mContext = context;

        String fname = mSubject + "_" + mFileFormat.format(new Date());
        fname = fname.replaceAll("[/\\\\.(){}$|?<>]","_");
        File f = new File(getAppDocumentsDir(mContext),  fname + ".csv");
        boolean newfile = !f.exists();
        mFw = new FileWriter(f, true);
        if (newfile) {
            writeRow("Time", "Level", "Problem", "Answer", "User answer", "Correct", "Time spent", "Running Percent");
        }
    }

    public synchronized void close() throws IOException {
        mFw.close();
    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public File getAppDocumentsDir(Context context) {
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS), context.getString(R.string.app_name));
        if (!file.exists() && !file.mkdirs()) {
            Log.e("Primary", "Directory could not be created");
        }
        return file;
    }

    public static String csvEscape(String value) {

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


    public synchronized void log(int level, String problem, String answer, String useranswer, boolean correct, long millis, float runningpercent) {
        writeRow(mDateFormat.format(new Date()), level, problem, answer, useranswer, correct,
                millis, String.format(Locale.getDefault(),"%4.1f",runningpercent) );
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
