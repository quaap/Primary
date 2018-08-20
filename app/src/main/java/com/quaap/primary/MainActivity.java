package com.quaap.primary;

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

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.quaap.primary.base.CommonBaseActivity;
import com.quaap.primary.base.SubjectMenuActivity;
import com.quaap.primary.base.component.HorzItemList;
import com.quaap.primary.base.data.AppData;
import com.quaap.primary.base.data.Subjects;
import com.quaap.primary.base.data.UserData;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends CommonBaseActivity {

    public static final String USERNAME = "username";
    public static final String SUBJECTCODE = "subjectcode";
    public static final String LEVELSETDONE = "levelsetdone";
    private List<String> avatarlist = new ArrayList<>();
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
                        .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
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

        Button goButton = findViewById(R.id.login_button);

        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSelectedSubject();
            }
        });
        Button cleanplurals = findViewById(R.id.cleanplurals);

        cleanplurals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NounCleanActivity.class);
                startActivity(intent);
            }
        });


    }

    public void onCleanPluralsClick(View view) {

    }

    private void checkFirstRun() {
        final SharedPreferences appPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final String csvkey = "use_csv_preference";

        if (!appPreferences.contains(csvkey)) {
            new AlertDialog.Builder(MainActivity.this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle(R.string.write_csv_alert)
                    .setMessage(R.string.do_csv_alert)
                    .setPositiveButton(R.string.record, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            appPreferences.edit().putBoolean(csvkey, true).apply();
                            checkStorageAccess();
                           // Toast.makeText(MainActivity.this, R.string.can_change_csv,Toast.LENGTH_LONG).show();
                        }

                    })
                    .setNegativeButton(R.string.no_record, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            appPreferences.edit().putBoolean(csvkey, false).apply();
                            Toast.makeText(MainActivity.this, R.string.can_change_csv,Toast.LENGTH_LONG).show();
                        }
                    })
                    .show();

        } else if (appPreferences.getBoolean(csvkey, true)) {
            checkStorageAccess();
        }


    }

    @Override
    protected void onResume() {
        super.onResume();

        updateUserList();
        selectUser(appdata.getLastSelectedUser(defaultusername));

        updateSubjectList();

        Button goButton = findViewById(R.id.login_button);

        goButton.postDelayed(new Runnable() {
            @Override
            public void run() {
                checkFirstRun();
            }
        }, 1000);


    }

    private void updateSubjectList() {
        final String[] subcodes = subjectDescs.getCodes().toArray(new String[0]);
        if (subjectlist != null) {
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

                    setItemTextField(item, R.id.subjectview_name, subjectDescs.get(i).getName());

                    setItemBackground(item, R.id.subjectview_code, subjectDescs.get(i).getGroup().getColor(MainActivity.this));


                    String username = userlist.getSelected();
                    if (username != null) {
                        UserData.Subject subject = AppData.getSubjectForUser(MainActivity.this, username, key);
                        if (subject.getSubjectCompleted()) {
                            setItemTextField(item, R.id.subjectview_status, getString(R.string.setcompleted));
                        } else if (subject.getTotalPoints() != 0) {
                            setItemTextField(item, R.id.subjectview_status, getString(R.string.level, subject.getHighestLevelNum() + 1));
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
                String subnext = subjectDescs.getNextCode(sub);
                if (subnext != null) {
                    sub = subnext;
                }

//                getIntent().removeExtra(LEVELSETDONE);

//                int pos = subjectDescs.get(sub);
//                if (pos + 1 < subjectDescs.getCount()) {
//                    sub = subjectDescs.get(pos + 1).getCode();
//                }
            }
            subjectlist.setSelected(sub);
            setSubjectDesc(sub);
        }
    }

    private void startSelectedSubject() {
        if (userlist.hasSelected()) {

            String code = subjectlist.getSelected();
            if (code != null) {
                appdata.getUser(userlist.getSelected()).setLatestSubject(code);


                Subjects.Desc subject = subjectDescs.get(code);
                try {
                    Intent intent = new Intent(MainActivity.this, SubjectMenuActivity.class);
                    intent.putExtra(SUBJECTCODE, code);
                    intent.putExtra(USERNAME, userlist.getSelected());

                    startActivity(intent);
                } catch (Exception e) {
                    Log.e("Primary", "Can't load " + code + " " + subject.getName(), e);
                }
            }
        }
    }

    private void setSubjectDesc(String code) {

        if (subjectDescs.get(code) != null) {
            TextView subject_desc = findViewById(R.id.subject_desc);
            subject_desc.setText(subjectDescs.get(code).getDesc());
        }

    }

    private void createNewUserArea() {

        populateAvatarSpinner();


        Button user_added = findViewById(R.id.user_added_button);

        user_added.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Spinner avatarspinner = findViewById(R.id.user_avatar_spinner);
                EditText newnamebox = findViewById(R.id.username_input);

                String newname = newnamebox.getText().toString();

                newname = newname.replaceAll("[/\\\\$:;|\"'?{}\\[\\]<>]", "_");

                if (new_user_shown) {
                    newname = newname.trim();
                    if (newname.length() < 1) {
                        Toast.makeText(MainActivity.this, R.string.name_short, Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (newname.length() > 10) {
                        Toast.makeText(MainActivity.this, R.string.name_long, Toast.LENGTH_LONG).show();
                        return;
                    }
                    UserData user = addUser(newname, (String) avatarspinner.getSelectedItem());
                    if (user != null) {
                        goAwayKeys(newnamebox);
                        userlist.addItem(newname);
                        //addUserToUserList(user);
                        selectUser(newname);
                        new_user_shown = false;
                        LinearLayout new_user_area = findViewById(R.id.login_new_user_area);
                        new_user_area.setVisibility(View.GONE);
                    } else {
                        Toast.makeText(MainActivity.this, R.string.name_in_use, Toast.LENGTH_LONG).show();
                        return;
                    }
                }
            }
        });

        Button user_changed = findViewById(R.id.user_change_button);
        user_changed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText namebox = findViewById(R.id.username_input);

                goAwayKeys(namebox);

                String username = namebox.getText().toString();
                Spinner avatarspinner = findViewById(R.id.user_avatar_spinner);

                String avatar = (String) avatarspinner.getSelectedItem();
                setUserAvatar(username, (String) avatarspinner.getSelectedItem());
                View userview = userlist.getItem(username);
                TextView txtavatar = userview.findViewById(R.id.userimage_avatar);
                txtavatar.setText(avatar);
                showNewUserArea(false);
                selectUser(username);

            }
        });


    }


    private void goAwayKeys(EditText input) {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        if(imm!=null && imm.isAcceptingText()) { // verify if the soft keyboard is open
            if (input!=null) {
                imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
            } else if (getCurrentFocus()!=null){
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        }
    }

    private void populateAvatarSpinner() {
        populateAvatarSpinner(null);
    }

    private void populateAvatarSpinner(String additional) {
        avatarlist = appdata.getUnusedAvatars(additional);

        Spinner avatarspinner = findViewById(R.id.user_avatar_spinner);
        avatarspinner.setAdapter(new ArrayAdapter<>(this, R.layout.spinner_item, avatarlist));

    }


    private void setUserAvatar(String username, String avatar) {

        appdata.getUser(username).setAvatar(avatar);
        populateAvatarSpinner();
    }

    private UserData addUser(String username, String avatar) {

        UserData user = appdata.addUser(username, avatar);
        if (user != null) {
            populateAvatarSpinner();
            //addUserToUserList(user);
        }
        return user;
    }


    private void deleteUser(String username) {

        if (username != null) {
            appdata.deleteUser(username);
            userlist.removeItem(username);
            selectUser(null);
            populateAvatarSpinner();
            updateSubjectList();
        }

    }


    private void updateUserList() {
        List<String> usernames = appdata.listUsers();

        if (userlist != null) {
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
                        uscore += getString(R.string.total_score, utot);
                    }


                    setItemTextField(item, R.id.userscore_avatar, uscore);

                }
            };
        }


    }

    private void selectUser(String username) {

        userlist.setSelected(username);

        View user_controls_area = findViewById(R.id.user_controls_area);
        Button goButton = findViewById(R.id.login_button);
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
        LinearLayout steps2and3 = findViewById(R.id.steps2and3);
        steps2and3.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void showNewUserArea(boolean show) {
        showNewUserArea(show, false);
    }

    private void showNewUserArea(boolean show, boolean edit) {
        LinearLayout new_user_area = findViewById(R.id.login_new_user_area);
        TextView nametxt = findViewById(R.id.username_input);
        Spinner avatarspinner = findViewById(R.id.user_avatar_spinner);
        Button user_change_button = findViewById(R.id.user_change_button);
        Button user_added_button = findViewById(R.id.user_added_button);


        nametxt.setEnabled(!edit);
        if (edit) {
            user_added_button.setVisibility(View.GONE);
            user_change_button.setVisibility(View.VISIBLE);
            UserData user = appdata.getUser(userlist.getSelected());
            if (user != null) {
                populateAvatarSpinner(user.getAvatar());
                nametxt.setText(user.getUsername());
                int aindex = avatarlist.indexOf(user.getAvatar());
                avatarspinner.setSelection(aindex);
            }

        } else {
            avatarspinner.setSelection((int) (Math.random() * avatarlist.size()));
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

