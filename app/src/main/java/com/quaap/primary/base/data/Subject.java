package com.quaap.primary.base.data;

import android.content.Context;
import android.content.res.XmlResourceParser;

import com.quaap.primary.base.Level;
import com.quaap.primary.base.SubjectBaseActivity;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
public class Subject {

    private String mCode;

    private String mName;
    private String mDesc;
    private Class mActivityClass;
    private Class mLevelClass;
    private List<Level> mLevels = new ArrayList<>();
    private List<Map<String,String>> mXmlLevels  = new ArrayList<>();
    

//    public Subject(String code, String desc, String name, Class activityClass, Class levelClass, List<Level> levels) {
//        mCode = code;
//        mName = name;
//        mDesc = desc;
//        mActivityClass = activityClass;
//        mLevelClass = levelClass;
//        mLevels = levels;
//    }

    public Subject(Context context, int xmlid) {
        loadFromXml(context, xmlid);
    }

    public <L> L getLevel(int l) {
        return (L)mLevels.get(l);
    }


    public String getCode() {
        return mCode;
    }

    public String getName() {
        return mName;
    }

    public String getDesc() {
        return mDesc;
    }

    public Class<SubjectBaseActivity> getActivityClass() {
        return mActivityClass;
    }

    public Class<Level> getLevelClass() {
        return mLevelClass;
    }




    private void loadFromXml(Context context, int xmlid) {
        try {
            XmlResourceParser parser = context.getResources().getXml(xmlid);

            parser.next(); // skip <xml.../>
            parser.nextTag();

            parser.require(XmlResourceParser.START_TAG, null, "subject");
            while (parser.next() != XmlResourceParser.END_DOCUMENT) {
                if (parser.getEventType() != XmlResourceParser.START_TAG) {
                    continue;
                }
                String name = parser.getName();
                System.out.println(name);
                if (name.equals("code")) {
                    parser.next();
                    this.mCode = parser.getText();
                }
                if (name.equals("nameKey")) {
                    parser.next();
                    String nameKey = parser.getText();
                    this.mName = context.getString(context.getResources().getIdentifier(nameKey, "string","com.quaap.primary"));
                }
                if (name.equals("descriptionKey")) {
                    parser.next();
                    String descriptionKey = parser.getText();
                    this.mDesc = context.getString(context.getResources().getIdentifier(descriptionKey, "string","com.quaap.primary"));
                }
                if (name.equals("activityClass")) {
                    parser.next();
                    this.mActivityClass = Class.forName(parser.getText());
                }
                if (name.equals("levelClass")) {
                    parser.next();
                    this.mLevelClass = Class.forName(parser.getText());
                }

                if (name.equals("levels")) {
                    parser.require(XmlResourceParser.START_TAG, null, "levels");
                    loadLevels(parser);
                }
            }

            for (Map<String,String> instMap: mXmlLevels) {
                getLevelClass().getDeclaredConstructor(String.class, Map.class).newInstance(this.getCode(), instMap);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadLevels(XmlResourceParser parser) throws IOException, XmlPullParserException {

        while (parser.next() != XmlResourceParser.END_DOCUMENT) {
            if (parser.getEventType() == XmlResourceParser.END_TAG && parser.getName().equals("levels")) {
                break;
            }

            if (parser.getEventType() == XmlResourceParser.START_TAG && parser.getName().equals("level")) {

                mXmlLevels.add(loadLevel(parser));
            }

        }

    }

    private Map<String,String> loadLevel(XmlResourceParser parser) throws IOException, XmlPullParserException {

        Map<String,String> levelparams = new HashMap<>();

        while (parser.next() != XmlResourceParser.END_DOCUMENT) {
            if (parser.getEventType() == XmlResourceParser.END_TAG && parser.getName().equals("level")) {
                break;
            }

            if (parser.getEventType() == XmlResourceParser.START_TAG) {
                String name = parser.getName();
                parser.next();
                String value = parser.getText();
                levelparams.put(name,value);
                System.out.println(name + " " + value);
            }

        }
        return levelparams;
    }


}
