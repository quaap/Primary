package com.quaap.primary;

/**
 * Created by tom on 12/15/16.
 *
 * Copyright (C) 2016   Tom Kliethermes
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.quaap.primary.base.CommonBaseActivity;
import com.quaap.primary.base.component.HorzItemList;
import com.quaap.primary.base.data.AppData;
import com.quaap.primary.base.data.Subjects;
import com.quaap.primary.base.data.UserData;

import java.util.ArrayList;

import java.util.List;


public class MainActivity extends CommonBaseActivity {

    public static final String USERNAME = "username";
    public static final String SUBJECT = "subject";
    public static final String SUBJECTNAME = "subjectname";
    public static final String LEVELSET = "levelset";
    public static final String LEVELSETDONE = "levelsetdone";

    private HorzItemList userlist;
    private HorzItemList subjectlist;


    private boolean new_user_shown = false;
    private String defaultusername;

    private AppData appdata;
    private Subjects subjectDescs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        appdata = AppData.getAppData(this);
        subjectDescs = Subjects.getInstance(this);

        defaultusername = getString(R.string.defaultUserName);
        addUser(defaultusername, UserData.avatars[0]);


        createNewUserArea();

        View delete_user_l = findViewById(R.id.delete_user_link);
        delete_user_l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle(getString(R.string.delete_user))
                        .setMessage(R.string.sure_delete_user)
                        .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener()  {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteUser(userlist.getSelected());
                            }

                        })
                        .setNegativeButton(R.string.cancel, null)
                        .show();
            }
        });

        View edit_user_l = findViewById(R.id.edit_user_link);
        edit_user_l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNewUserArea(true, true);
            }
        });

        Button goButton = (Button)findViewById(R.id.login_button);

        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSelectedSubject();
            }
        });
        //Log.d("spell", Locale.getDefault().getLanguage());
    }


    @Override
    protected void onResume() {
        super.onResume();

        updateUserList();
        selectUser(appdata.getLastSelectedUser(defaultusername));

        updateSubjectList();

    }

    private void updateSubjectList() {
        String [] subcodes = subjectDescs.getCodes().toArray(new String[0]);
        if (subjectlist!=null) {
            subjectlist.clear();
            subjectlist.populate(subcodes);
        } else {

            subjectlist = new HorzItemList(this, R.id.subject_horz_list, R.layout.subject_view, subcodes) {

                @Override
                protected void onItemClicked(String key, ViewGroup item) {
                    setSubjectDesc(key);
                }

                @Override
                protected void populateItem(String key, ViewGroup item, int i) {
                    setItemTextField(item, R.id.subjectview_code, key);

                    setItemTextField(item, R.id.subjectview_name, getResources().getStringArray(R.array.subjectsName)[i]);

                    String username = userlist.getSelected();
                    if (username!=null) {
                        UserData.Subject subject = AppData.getSubjectForUser(MainActivity.this, username, key);
                        if (subject.getSubjectCompleted()) {
                            setItemTextField(item, R.id.subjectview_status, getString(R.string.setcompleted));
                        } else if (subject.getTotalPoints()!=0) {
                            setItemTextField(item, R.id.subjectview_status, getString(R.string.level,subject.getLevelNum()+1));
                        }
                    }
                }
            };
            subjectlist.showAddButton(false);
        }
        if (userlist.hasSelected()) {
            String sub = appdata.getUser(userlist.getSelected()).getLatestSubject();
            if (sub == null) {
                sub = subjectDescs.get(0).getCode();
            } else if (getIntent().getBooleanExtra(LEVELSETDONE, false)) {
                getIntent().removeExtra(LEVELSETDONE);
                int pos = subjectDescs.get(sub).getPos();
                if (pos + 1 < subjectDescs.getCount()) {
                    sub = subjectDescs.get(pos + 1).getCode();
                }
            }
            subjectlist.setSelected(sub);
            setSubjectDesc(sub);
        }
    }


    private void startSelectedSubject() {
        if (userlist.hasSelected()) {

            String code = subjectlist.getSelected();
            if (code!=null) {
                appdata.getUser(userlist.getSelected()).setLatestSubject(code);


                Subjects.Desc subject = subjectDescs.get(code);
                try {
                    Intent intent = new Intent(MainActivity.this, Class.forName(subject.getActivityclass()));
                    intent.putExtra(LEVELSET, subject.getLevelset());
                    intent.putExtra(SUBJECT, code);
                    intent.putExtra(SUBJECTNAME, subject.getName());
                    intent.putExtra(USERNAME, userlist.getSelected());

                    startActivity(intent);
                } catch (ClassNotFoundException e) {
                    Log.e("Primary", "Can't load " + code + " " + subject.getName(), e);
                }
            }
        }
    }



    private void setSubjectDesc(String code) {

        if (subjectDescs.get(code)!=null) {
            TextView subject_desc = (TextView) findViewById(R.id.subject_desc);
            subject_desc.setText(subjectDescs.get(code).getDesc());
        }

    }


    List<String> avatarlist = new ArrayList<>();

    private void createNewUserArea() {

        populateAvatarSpinner();


        Button user_added = (Button)findViewById(R.id.user_added_button);

        user_added.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Spinner avatarspinner = (Spinner)findViewById(R.id.user_avatar_spinner);
                EditText newnamebox = (EditText)findViewById(R.id.username_input);
                String newname = newnamebox.getText().toString();
                if (new_user_shown) {
                    newname = newname.trim();
                    if (newname.length()<1) {
                        Toast.makeText(MainActivity.this, R.string.name_short,Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (newname.length()>10) {
                        Toast.makeText(MainActivity.this, R.string.name_long,Toast.LENGTH_LONG).show();
                        return;
                    }
                    UserData user = addUser(newname, (String)avatarspinner.getSelectedItem());
                    if (user!=null){
                        userlist.addItem(newname);
                        //addUserToUserList(user);
                        selectUser(newname);
                        new_user_shown = false;
                        LinearLayout new_user_area = (LinearLayout)findViewById(R.id.login_new_user_area);
                        new_user_area.setVisibility(View.GONE);
                    } else {
                        Toast.makeText(MainActivity.this, R.string.name_in_use,Toast.LENGTH_LONG).show();
                        return;
                    }
                }
            }
        });

        Button user_changed = (Button)findViewById(R.id.user_change_button);
        user_changed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText namebox = (EditText)findViewById(R.id.username_input);
                String username = namebox.getText().toString();
                Spinner avatarspinner = (Spinner)findViewById(R.id.user_avatar_spinner);

                String avatar = (String)avatarspinner.getSelectedItem();
                setUserAvatar(username, (String)avatarspinner.getSelectedItem());
                View userview = userlist.getItem(username);
                TextView txtavatar = (TextView)userview.findViewById(R.id.userimage_avatar);
                txtavatar.setText(avatar);
                showNewUserArea(false);
                selectUser(username);
            }
        });



    }


    private void populateAvatarSpinner() {
        populateAvatarSpinner(null);
    }
    private void populateAvatarSpinner(String additional) {
        avatarlist = appdata.getUnusedAvatars(additional);

        Spinner avatarspinner = (Spinner)findViewById(R.id.user_avatar_spinner);
        avatarspinner.setAdapter(new ArrayAdapter<>(this, R.layout.spinner_item, avatarlist));

    }



    private void setUserAvatar(String username, String avatar) {

        appdata.getUser(username).setAvatar(avatar);
        populateAvatarSpinner();
    }

    private UserData addUser(String username, String avatar) {

        UserData user = appdata.addUser(username, avatar);
        if (user!=null) {
            populateAvatarSpinner();
            //addUserToUserList(user);
        }
        return user;
    }


    private void deleteUser(String username) {

        if (username!=null) {
            appdata.deleteUser(username);

            userlist.removeItem(username);
            populateAvatarSpinner();
            updateSubjectList();
        }

    }



    private void updateUserList() {
        List<String> usernames = appdata.listUsers();

        if (userlist!=null) {
            userlist.clear();
            userlist.populate(usernames.toArray(new String[0]));
        } else {

            userlist = new HorzItemList(this, R.id.user_horz_list, R.layout.user_avatar, usernames.toArray(new String[0])) {
                @Override
                protected void onNewItemClicked() {
                    showNewUserArea(true);
                }

                @Override
                protected void onItemClicked(String key, ViewGroup item) {
                    showNewUserArea(false);
                    selectUser(key);
                }

                @Override
                protected void populateItem(String key, ViewGroup item, int i) {
                    UserData user = appdata.getUser(key);
                    setItemTextField(item, R.id.username_avatar, user.getUsername());
                    setItemTextField(item, R.id.userimage_avatar, user.getAvatar());

                    int utot = user.getTotalPoints();
                    int utod = user.getTodayPoints();

                    String uscore;
                    if (utod != 0) {
                        uscore = getString(R.string.today_score, utod);
                    } else {
                        uscore = "";
                    }
                    uscore += "\n";

                    if (utot != 0) {
                        uscore +=getString(R.string.total_score, utot);
                    }


                    setItemTextField(item, R.id.userscore_avatar, uscore);

                }
            };
        }


    }

    private void selectUser(String username) {

        userlist.setSelected(username);

        View user_controls_area = findViewById(R.id.user_controls_area);
        Button goButton = (Button)findViewById(R.id.login_button);
        if (userlist.hasSelected()) {
            showSteps2and3(true);


            if (userlist.getSelected().equals(defaultusername)) {
                user_controls_area.setVisibility(View.GONE);
            } else {
                user_controls_area.setVisibility(View.VISIBLE);
            }

            updateSubjectList();
            if (subjectlist.hasSelected()) {
                goButton.setEnabled(true);
            }
        } else {
            showSteps2and3(false);

            user_controls_area.setVisibility(View.GONE);
            goButton.setEnabled(false);
        }
        appdata.setLastSelectedUser(userlist.getSelected());
    }

    private void showSteps2and3(boolean show) {
        LinearLayout steps2and3 = (LinearLayout)findViewById(R.id.steps2and3);
        steps2and3.setVisibility(show?View.VISIBLE:View.GONE);
    }

    private void showNewUserArea(boolean show) {
        showNewUserArea(show, false);
    }

    private void showNewUserArea(boolean show, boolean edit) {
        LinearLayout new_user_area = (LinearLayout)findViewById(R.id.login_new_user_area);
        TextView nametxt = (TextView)findViewById(R.id.username_input);
        Spinner avatarspinner = (Spinner)findViewById(R.id.user_avatar_spinner);
        Button user_change_button = (Button)findViewById(R.id.user_change_button);
        Button user_added_button = (Button)findViewById(R.id.user_added_button);



        nametxt.setEnabled(!edit);
        if (edit) {
            user_added_button.setVisibility(View.GONE);
            user_change_button.setVisibility(View.VISIBLE);
            UserData user = appdata.getUser(userlist.getSelected());
            if (user!=null) {
                populateAvatarSpinner(user.getAvatar());
                nametxt.setText(user.getUsername());
                int aindex = avatarlist.indexOf(user.getAvatar());
                avatarspinner.setSelection(aindex);
            }

        } else {
            avatarspinner.setSelection((int)(Math.random()*avatarlist.size()));
            nametxt.setText("");
            user_added_button.setVisibility(View.VISIBLE);
            user_change_button.setVisibility(View.GONE);

        }

        if (show) {
            new_user_area.setVisibility(View.VISIBLE);
            new_user_shown = true;
            selectUser(null);
        } else {
            new_user_area.setVisibility(View.GONE);
            new_user_shown = false;
        }
        showSteps2and3(false);
    }



}

