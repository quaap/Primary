package com.quaap.primary;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.quaap.primary.base.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class MainActivity extends AppCompatActivity {

    private static final int[] AVATARHEX = {
            0x263A, 0x2615, 0x26BE, 0x26F5, 0x26BD, 0x1F680, 0x1F308,
            0x1F332, 0x1F333, 0x1F34B, 0x1F350, 0x1F37C, 0x1F3C7, 0x1F3C9, 0x1F3E4,
            0x1F400, 0x1F401, 0x1F402, 0x1F403, 0x1F404, 0x1F405, 0x1F406, 0x1F407,
            0x1F408, 0x1F409, 0x1F40A, 0x1F40B, 0x1F40F, 0x1F410, 0x1F413, 0x1F415,
            0x1F416, 0x1F42A, 0x1F600, 0x1F607, 0x1F608, 0x1F60E, 0x1F525, 0x1F526,
            0x1F527, 0x1F528, 0x1F529, 0x1F52A, 0x1F52B, 0x1F52E, 0x1F52F, 0x1F530,
            0x1F531, 0x1F4DA, 0x1F498, 0x1F482, 0x1F483, 0x1F484, 0x1F485, 0x1F4F7,
            0x1F4F9, 0x1F4FA, 0x1F4FB, 0x1F30D, 0x1F30E, 0x1F310, 0x1F312, 0x1F316,
            0x1F317, 0x1F318, 0x1F31A, 0x1F31C, 0x1F31D, 0x1F31E, 0x1F681, 0x1F682,
            0x1F686, 0x1F688,
            0x1F334, 0x1F335, 0x1F337, 0x1F338, 0x1F339, 0x1F33A, 0x1F33B, 0x1F33C, 0x1F33D,
            0x1F33E, 0x1F33F, 0x1F340, 0x1F341, 0x1F342, 0x1F343, 0x1F344, 0x1F345, 0x1F346,
            0x1F347, 0x1F348, 0x1F349, 0x1F34A, 0x1F34C, 0x1F34D, 0x1F34E, 0x1F34F, 0x1F351,
            0x1F352, 0x1F353, 0x1F354, 0x1F355, 0x1F356, 0x1F357, 0x1F358, 0x1F359, 0x1F35A,
            0x1F35B, 0x1F35C, 0x1F35D, 0x1F35E, 0x1F35F, 0x1F360, 0x1F361, 0x1F362, 0x1F363,
            0x1F364, 0x1F365, 0x1F366, 0x1F367, 0x1F368, 0x1F369, 0x1F36A, 0x1F36B, 0x1F36C,
            0x1F36D, 0x1F36E, 0x1F36F, 0x1F370, 0x1F371, 0x1F372, 0x1F373, 0x1F374, 0x1F375,
            0x1F376, 0x1F377, 0x1F378, 0x1F379, 0x1F37A, 0x1F37B, 0x1F380, 0x1F381, 0x1F382,
            0x1F383, 0x1F384, 0x1F385, 0x1F386, 0x1F387, 0x1F388, 0x1F389, 0x1F38A, 0x1F38B,
            0x1F38C, 0x1F38D, 0x1F38E, 0x1F38F, 0x1F390, 0x1F391, 0x1F392, 0x1F393
    };
    public static final String LASTSELECTEDUSER = "lastselecteduser";
    public static final String AVATAR_PRE = "avatar:";
    public static final String AVATAR_POST = ":avatar";
    public static final String USERS_KEY = "users";
    public static final String USERNAME = "username";

    private final String [] avatars;

    private final Map<String,View> userlist = new HashMap<>();
    private String selected_user;
    private SharedPreferences prefs;
    private boolean new_user_shown = false;
    private String defaultusername;

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
        prefs = getSharedPreferences("app", MODE_PRIVATE);

        defaultusername = getString(R.string.defaultUserName);
        addUser(defaultusername, avatars[0]);

        createUserList();

        selectUser(prefs.getString(LASTSELECTEDUSER, defaultusername));

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
                                deleteUser();
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

                if (selected_user!=null) {
                    Spinner subjectspinner = (Spinner)findViewById(R.id.subject_spinner);
                    String subject = (String)subjectspinner.getSelectedItem();
                    int subjectId = subjectspinner.getSelectedItemPosition();

                    String [] classes = getResources().getStringArray(R.array.subjectsActivity);
                    try {
                        Intent intent = new Intent(MainActivity.this, Class.forName(classes[subjectId]));
                        intent.putExtra(USERNAME, selected_user);
                        startActivity(intent);
                    } catch (ClassNotFoundException e) {
                        Log.e("Primary", "Can't load " + subject + " " + subjectId);
                    }
                }

            }
        });


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
                    User user = addUser(newname, (String)avatarspinner.getSelectedItem());
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

    private List<User> getUserList() {
        Set<String> usernames = new TreeSet<>();
        usernames = prefs.getStringSet(USERS_KEY, usernames);

        List<User> users = new ArrayList<>();
        for (String username: usernames) {
            User user = getUser(username);
            if (user!=null) {
                users.add(user);
            }
        }
        return users;
    }

    private void populateAvatarSpinner() {
        populateAvatarSpinner(null);
    }
    private void populateAvatarSpinner(String additional) {
        avatarlist = new ArrayList<>();
        for (String avatar: avatars) {
            if (!prefs.getBoolean(AVATAR_PRE + avatar, false)) {
                avatarlist.add(avatar);
            }
        }
        if (additional!=null) avatarlist.add(additional);
        Collections.sort(avatarlist);

        Spinner avatarspinner = (Spinner)findViewById(R.id.user_avatar_spinner);
        avatarspinner.setAdapter(new ArrayAdapter<>(this, R.layout.spinner_item, avatarlist));

    }

    private void setUserAvatar(String username, String avatar) {
        String oldavatar = prefs.getString(username+ AVATAR_POST, null);
        SharedPreferences.Editor ed = prefs.edit();
        if (oldavatar!=null) {
            ed.remove(AVATAR_PRE + oldavatar);
        }
        ed.putString(username+ AVATAR_POST, avatar);
        ed.putBoolean(AVATAR_PRE + avatar, true);
        ed.apply();
        populateAvatarSpinner();
    }

    private User addUser(String username, String avatar) {
        Set<String> usernames = new TreeSet<>();

        usernames = prefs.getStringSet(USERS_KEY, usernames);
        User user = null;
        if (!usernames.contains(username)){
            usernames.add(username);
            SharedPreferences.Editor ed = prefs.edit();
            ed.putStringSet(USERS_KEY, usernames);
            ed.apply();
            setUserAvatar(username, avatar);
            user = new User();
            user.username = username;
            user.avatar = avatar;
            populateAvatarSpinner();
        }

        return user;
    }


    private void deleteUser() {
        String username = selected_user;
        User user = getUser(username);

        if (user!=null) {
            Set<String> usernames = new TreeSet<>();
            usernames = prefs.getStringSet(USERS_KEY, usernames);

            if (usernames.contains(username)){
                usernames.remove(username);
                SharedPreferences.Editor ed = prefs.edit();
                ed.putStringSet(USERS_KEY, usernames);
                ed.remove(user.username+ AVATAR_POST);
                ed.remove(AVATAR_PRE + user.avatar);
                ed.apply();
            }
            removeUserFromUserList(username);
            selectUser(null);
            populateAvatarSpinner();
        }
    }

    private User getUser(String username) {
        String avatar = prefs.getString(username+ AVATAR_POST, null);
        User user = null;
        if (avatar!=null) {
            user = new User();
            user.username = username;
            user.avatar = avatar;
        }
        return user;
    }

    private void createUserList() {

        List<User> users = getUserList();

        for (User user: users) {
            addUserToUserList(user);

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

                SharedPreferences.Editor ed = prefs.edit();
                ed.putString(LASTSELECTEDUSER, selected_user);
                ed.apply();
                goButton.setEnabled(true);
            }
        } else {
            showSteps2and3(false);

            SharedPreferences.Editor ed = prefs.edit();
            ed.remove(LASTSELECTEDUSER);
            ed.apply();
            user_controls_area.setVisibility(View.GONE);
            goButton.setEnabled(false);
        }
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
            User user = getUser(selected_user);
            if (user!=null) {
                populateAvatarSpinner(user.avatar);
                nametxt.setText(user.username);
                int aindex = avatarlist.indexOf(user.avatar);
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

    private void addUserToUserList(User user) {
        LinearLayout userview = (LinearLayout)findViewById(R.id.user_avatar_list_view);
        View child = LayoutInflater.from(this).inflate(R.layout.user_avatar, null);

        final TextView user_name = (TextView)child.findViewById(R.id.username_avatar);
        TextView user_image = (TextView)child.findViewById(R.id.userimage_avatar);

        user_name.setText(user.username);
        user_image.setText(user.avatar);

        child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNewUserArea(false);
                selectUser((String)view.getTag());
            }
        });
        userview.addView(child);
        userlist.put(user.username,child);
        child.setTag(user.username);

    }
}
