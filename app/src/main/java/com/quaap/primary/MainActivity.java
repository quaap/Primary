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

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.quaap.primary.base.data.AppData;
import com.quaap.primary.base.data.UserData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private static final int[] AVATARHEX = {
            0x263A, 0x2615, 0x26BE, 0x26F5, 0x26BD, 0x1F680, 0x1F308, 0x1F332, 0x1F333, 0x1F34B,
            0x1F350, 0x1F37C, 0x1F3C7, 0x1F3C9, 0x1F3E4, 0x1F400, 0x1F401, 0x1F402, 0x1F403,
            0x1F404, 0x1F405, 0x1F406, 0x1F407, 0x1F408, 0x1F409, 0x1F40A, 0x1F40B, 0x1F40F,
            0x1F410, 0x1F413, 0x1F415, 0x1F416, 0x1F42A, 0x1F600, 0x1F607, 0x1F608, 0x1F60E,
            0x1F525, 0x1F526, 0x1F527, 0x1F528, 0x1F529, 0x1F52E, 0x1F530, 0x1F531, 0x1F4DA,
            0x1F498, 0x1F482, 0x1F483, 0x1F484, 0x1F485, 0x1F4F7, 0x1F4F9, 0x1F4FA, 0x1F4FB,
            0x1F30D, 0x1F30E, 0x1F310, 0x1F312, 0x1F316, 0x1F317, 0x1F318, 0x1F31A, 0x1F31C,
            0x1F31D, 0x1F31E, 0x1F681, 0x1F682, 0x1F686, 0x1F688, 0x1F334, 0x1F335, 0x1F337,
            0x1F338, 0x1F339, 0x1F33A, 0x1F33B, 0x1F33C, 0x1F33D, 0x1F33E, 0x1F33F, 0x1F340,
            0x1F341, 0x1F342, 0x1F343, 0x1F344, 0x1F345, 0x1F346, 0x1F347, 0x1F348, 0x1F349,
            0x1F34A, 0x1F34C, 0x1F34D, 0x1F34E, 0x1F34F, 0x1F351, 0x1F352, 0x1F353, 0x1F354,
            0x1F355, 0x1F356, 0x1F357, 0x1F35A, 0x1F35B, 0x1F35C, 0x1F35D, 0x1F35E, 0x1F35F,
            0x1F360, 0x1F361, 0x1F362, 0x1F363, 0x1F364, 0x1F365, 0x1F366, 0x1F367, 0x1F368,
            0x1F369, 0x1F36A, 0x1F36B, 0x1F36C, 0x1F36D, 0x1F36E, 0x1F36F, 0x1F370, 0x1F371,
            0x1F372, 0x1F373, 0x1F374, 0x1F375, 0x1F376, 0x1F377, 0x1F378, 0x1F379, 0x1F37A,
            0x1F37B, 0x1F380, 0x1F381, 0x1F382, 0x1F383, 0x1F384, 0x1F385, 0x1F386, 0x1F387,
            0x1F388, 0x1F389, 0x1F38A, 0x1F38B, 0x1F38C, 0x1F38D, 0x1F38E, 0x1F38F, 0x1F390,
            0x1F391, 0x1F392, 0x1F393
    };

    public static final String LASTSELECTEDUSER = "lastselecteduser";
    public static final String AVATAR_PRE = "avatar:";
    public static final String AVATAR_POST = ":avatar";
    public static final String USERS_KEY = "users";
    public static final String USERNAME = "username";
    public static final String SUBJECT = "subject";
    public static final String LEVELSET = "levelset";

    private final String [] avatars;

    private final Map<String,View> userlist = new HashMap<>();
    private String selected_user;

    private boolean new_user_shown = false;
    private String defaultusername;

    private AppData appdata;

    public MainActivity() {
        avatars = new String[AVATARHEX.length];
        for (int i = 0; i< AVATARHEX.length; i++) {
            avatars[i] = new String(Character.toChars(AVATARHEX[i]));
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        appdata = AppData.getAppData(this);

        defaultusername = getString(R.string.defaultUserName);
        addUser(defaultusername, avatars[0]);

        createUserList();

        selectUser(appdata.getLastSelectedUser(defaultusername));

        createNewUserArea();

        View delete_user_l = findViewById(R.id.delete_user_link);
        delete_user_l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Delete")
                        .setMessage("Are you sure you want to delete the user?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener()  {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteUser(selected_user);
                            }

                        })
                        .setNegativeButton("No", null)
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

        Spinner subjectspinner = (Spinner)findViewById(R.id.subject_spinner);
        int sub = appdata.getUser(selected_user).getLatestSubject();
        subjectspinner.setSelection(sub);
        setSubjectDesc(sub);
        subjectspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                setSubjectDesc(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private static final int REQUESTCODE = 120;
    public static final int RESULTCODE_SETDONE = 134;
    public static final int RESULTCODE_NOTDONE = 0;

    private void startSelectedSubject() {
        if (selected_user!=null) {
            Spinner subjectspinner = (Spinner)findViewById(R.id.subject_spinner);
            String subject = (String)subjectspinner.getSelectedItem();
            int subjectId = subjectspinner.getSelectedItemPosition();

            appdata.getUser(selected_user).setLatestSubject(subjectId);
            String [] classes = getResources().getStringArray(R.array.subjectsActivity);
            String [] levelsets = getResources().getStringArray(R.array.subjectsLevelset);
            try {
                Intent intent = new Intent(MainActivity.this, Class.forName(classes[subjectId]));
                intent.putExtra(LEVELSET, levelsets[subjectId]);
                intent.putExtra(SUBJECT, subject);
                intent.putExtra(USERNAME, selected_user);

                startActivityForResult(intent, REQUESTCODE);
            } catch (ClassNotFoundException e) {
                Log.e("Primary", "Can't load " + subject + " " + subjectId);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQUESTCODE==requestCode && resultCode==RESULTCODE_SETDONE) {
            int sub = appdata.getUser(selected_user).getLatestSubject();
            TextView latest_completed_txt = (TextView)findViewById(R.id.latest_completed_txt);
            latest_completed_txt.setText("Completed "+ (getResources().getStringArray(R.array.subjects)[sub]));
            Spinner subjectspinner = (Spinner)findViewById(R.id.subject_spinner);
            if (sub < subjectspinner.getAdapter().getCount()-1) {
                appdata.getUser(selected_user).setLatestSubject(sub+1);

                subjectspinner.setSelection(sub+1);
            }
        }
    }

    private void setSubjectDesc(int i) {
        TextView subject_desc = (TextView)findViewById(R.id.subject_desc);
        subject_desc.setText(getResources().getStringArray(R.array.subjectDescs)[i]);
    }


    List<String> avatarlist = new ArrayList<>();
    private void createNewUserArea() {

        populateAvatarSpinner();

        ImageView newuserbutton = (ImageView)findViewById(R.id.add_user_button);

        newuserbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNewUserArea(true);
            }
        });

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
                        addUserToUserList(user);
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
                View userview = userlist.get(username);
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

            removeUserFromUserList(username);
            selectUser(null);
            populateAvatarSpinner();
        }

    }



    private void createUserList() {

        Set<String> usernames = appdata.listUsers();

        for (String username: usernames) {
            addUserToUserList(appdata.getUser(username));

        }
    }

    private void selectUser(String username) {
        final int normalColor = Color.TRANSPARENT;
        final int selectedColor = Color.CYAN;

        if (selected_user!=null) {
            View old_selected = userlist.get(selected_user);
            if (old_selected!=null) {
                old_selected.setBackgroundColor(normalColor);
            }
        }
        selected_user = username;
        View user_controls_area = findViewById(R.id.user_controls_area);
        Button goButton = (Button)findViewById(R.id.login_button);
        if (selected_user!=null) {
            showSteps2and3(true);

            View new_selected = userlist.get(selected_user);
            if (new_selected!=null) {
                new_selected.setBackgroundColor(selectedColor);
                if (selected_user.equals(defaultusername)) {
                    user_controls_area.setVisibility(View.GONE);
                } else {
                    user_controls_area.setVisibility(View.VISIBLE);
                }

                goButton.setEnabled(true);
            }
        } else {
            showSteps2and3(false);

            user_controls_area.setVisibility(View.GONE);
            goButton.setEnabled(false);
        }
        appdata.setLastSelectedUser(selected_user);
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
            UserData user = appdata.getUser(selected_user);
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


    private void removeUserFromUserList(String username) {
        LinearLayout userview = (LinearLayout)findViewById(R.id.user_avatar_list_view);
        View old_selected = userlist.get(username);
        userview.removeView(old_selected);
    }

    private void addUserToUserList(UserData user) {
        LinearLayout userview = (LinearLayout)findViewById(R.id.user_avatar_list_view);
        View child = LayoutInflater.from(this).inflate(R.layout.user_avatar, null);

        final TextView user_name = (TextView)child.findViewById(R.id.username_avatar);
        TextView user_image = (TextView)child.findViewById(R.id.userimage_avatar);

        user_name.setText(user.getUsername());
        user_image.setText(user.getAvatar());

        child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNewUserArea(false);
                selectUser((String)view.getTag());
            }
        });
        userview.addView(child);
        userlist.put(user.getUsername(),child);
        child.setTag(user.getUsername());

    }
}
