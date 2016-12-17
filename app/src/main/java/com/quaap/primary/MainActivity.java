package com.quaap.primary;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;

import android.support.annotation.NonNull;
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

    private final String [] avatars;

    private final Map<String,View> userlist = new HashMap<>();
    private String selected_user;
    private SharedPreferences prefs;
    private boolean new_user_shown = false;

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

        String defaultusername = getString(R.string.defaultUserName);
        addUser(defaultusername, avatars[0]);

        createUserList();

        selectUser(prefs.getString("lastselecteduser", defaultusername));

        createNewUserArea();



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
                        intent.putExtra("username", selected_user);
                        startActivity(intent);
                    } catch (ClassNotFoundException e) {
                        Log.e("Primary", "Can't load " + subject + " " + subjectId);
                    }
                }

            }
        });


    }



    private void createNewUserArea() {
        List<String> avatarlist = new ArrayList<>();
        for (String avatar: avatars) {
            if (!prefs.getBoolean("avatar:" + avatar, false)) {
                avatarlist.add(avatar);
            }
        }
        Collections.sort(avatarlist);

        Spinner avatarspinner = (Spinner)findViewById(R.id.user_avatar_spinner);
        avatarspinner.setAdapter(new ArrayAdapter<>(this, R.layout.spinner_item, avatarlist));

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
                    if (newname.length()<2) {
                        Toast.makeText(MainActivity.this,"Name too short",Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (newname.length()>12) {
                        Toast.makeText(MainActivity.this,"Name must be less than 12 characters long",Toast.LENGTH_LONG).show();
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
                        Toast.makeText(MainActivity.this,"Name already in use!",Toast.LENGTH_LONG).show();
                        return;
                    }
                }
            }
        });



    }

    private List<User> getUserList() {
        Set<String> usernames = new TreeSet<>();
        usernames = prefs.getStringSet("users", usernames);

        List<User> users = new ArrayList<>();
        for (String username: usernames) {
            User user = getUser(username);
            if (user!=null) {
                users.add(user);
            }
        }
        return users;
    }



    private User addUser(String username, String avatar) {
        Set<String> usernames = new TreeSet<>();

        usernames = prefs.getStringSet("users", usernames);
        User user = null;
        if (!usernames.contains(username)){
            usernames.add(username);
            SharedPreferences.Editor ed = prefs.edit();
            ed.putStringSet("users", usernames);
            ed.putString(username+":avatar", avatar);
            ed.putBoolean("avatar:" + avatar, true);
            ed.apply();
            user = new User();
            user.username = username;
            user.avatar = avatar;

        }

        return user;
    }


    private User getUser(String username) {
        String avatar = prefs.getString(username+":avatar", null);
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
            old_selected.setBackgroundColor(normalColor);
        }
        selected_user = username;
        if (selected_user!=null) {
            View new_selected = userlist.get(selected_user);
            new_selected.setBackgroundColor(selectedColor);

            SharedPreferences.Editor ed = prefs.edit();
            ed.putString("lastselecteduser", selected_user);
            ed.apply();
        }
    }

    private void showNewUserArea(boolean show) {
        LinearLayout new_user_area = (LinearLayout)findViewById(R.id.login_new_user_area);
        if (show) {
            new_user_area.setVisibility(View.VISIBLE);
            new_user_shown = true;
            selectUser(null);
        } else {
            new_user_area.setVisibility(View.GONE);
            new_user_shown = false;
        }
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
