package com.quaap.primary;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.quaap.primary.base.User;
import com.quaap.primary.math.Math1MenuActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class MainActivity extends AppCompatActivity {

    private final int[] avatarhex = {
            0x1F335,
            0x1F332, 0x1F333, 0x1F34B, 0x1F350, 0x1F37C, 0x1F3C7, 0x1F3C9, 0x1F3E4,
            0x1F400, 0x1F401, 0x1F402, 0x1F403, 0x1F404, 0x1F405, 0x1F406, 0x1F407,
            0x1F408, 0x1F409, 0x1F40A, 0x1F40B, 0x1F40F, 0x1F410, 0x1F413, 0x1F415,
            0x1F416, 0x1F42A, 0x1F600, 0x1F607, 0x1F608, 0x1F60E, 0x1F525, 0x1F526,
            0x1F527, 0x1F528, 0x1F529, 0x1F52A, 0x1F52B, 0x1F52E, 0x1F52F, 0x1F530,
            0x1F531, 0x1F4DA, 0x1F498, 0x1F482, 0x1F483, 0x1F484, 0x1F485, 0x1F4F7,
            0x1F4F9, 0x1F4FA, 0x1F4FB, 0x1F30D, 0x1F30E, 0x1F310, 0x1F312, 0x1F316,
            0x1F317, 0x1F318, 0x1F31A, 0x1F31C, 0x1F31D, 0x1F31E, 0x1F681, 0x1F682,
            0x1F686, 0x1F688
    };

    private final String [] avatars;

    private Map<String,View> userlist = new HashMap<>();
    private String selected_user;
    private SharedPreferences prefs;

    public MainActivity() {
        avatars = new String[avatarhex.length];
        for (int i=0; i<avatarhex.length; i++) {
            avatars[i] = new String(Character.toChars(avatarhex[i]));
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        prefs = getSharedPreferences("app", MODE_PRIVATE);

        addUser("default", avatars[0]);
        createUserList();

        createNewUserArea();
       //


        Button goButton = (Button)findViewById(R.id.login_button);

        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBox new_user = (CheckBox)findViewById(R.id.new_user_checkBox);
                Spinner avatarspinner = (Spinner)findViewById(R.id.user_avatar_spinner);
                EditText newnamebox = (EditText)findViewById(R.id.username_input);
                String newname = newnamebox.getText().toString();
                if (new_user.isChecked()) {

                    if (addUser(newname, (String)avatarspinner.getSelectedItem())){

                        selectUser(newname);

                    } else {
                        Toast.makeText(MainActivity.this,"Name already in use!",Toast.LENGTH_LONG);
                        return;
                    }
                }

                if (selected_user!=null) {
                    Spinner subjectspinner = (Spinner)findViewById(R.id.subject_spinner);
                    String subject = (String)subjectspinner.getSelectedItem();
                    int subjectId = subjectspinner.getSelectedItemPosition();

                    String [] classes = getResources().getStringArray(R.array.subjectsActivity);
                    try {
                        Intent intent = new Intent(MainActivity.this, Class.forName(classes[subjectId]));
                        intent.putExtra("user", selected_user);
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

        Spinner avatarspinner = (Spinner)findViewById(R.id.user_avatar_spinner);
        avatarspinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, avatarlist));

        final LinearLayout userview = (LinearLayout)findViewById(R.id.user_avatar_list_view);
        final LinearLayout new_user_area = (LinearLayout)findViewById(R.id.login_new_user_area);
        final CheckBox new_user = (CheckBox)findViewById(R.id.new_user_checkBox);
        new_user.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                new_user_area.setVisibility(b? View.VISIBLE:View.GONE);
                userview.setVisibility(b?View.GONE:View.VISIBLE);
            }
        });
    }

    public List<User> getUserList() {
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



    public boolean addUser(String username, String avatar) {
        Set<String> usernames = new TreeSet<>();

        usernames = prefs.getStringSet("users", usernames);

        if (!usernames.contains(username)){
            usernames.add(username);
            SharedPreferences.Editor ed = prefs.edit();
            ed.putStringSet("users", usernames);
            ed.putString(username+":avatar", avatar);
            ed.putBoolean("avatar:" + avatar, true);
            ed.apply();
        } else {
            return false;
        }

        return true;
    }


    public User getUser(String username) {
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
            View child = addUserToUserList(user);

            final TextView user_name = (TextView)child.findViewById(R.id.username_avatar);
            TextView user_image = (TextView)child.findViewById(R.id.userimage_avatar);

            user_name.setText(user.username);

            user_image.setText(user.avatar);

            child.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    selectUser((String)view.getTag());
                }
            });
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
        View new_selected = userlist.get(selected_user);
        new_selected.setBackgroundColor(selectedColor);
    }

    @NonNull
    private View addUserToUserList(User user) {
        LinearLayout userview = (LinearLayout)findViewById(R.id.user_avatar_list_view);
        View child = LayoutInflater.from(this).inflate(R.layout.user_avatar, null);

        final TextView user_name = (TextView)child.findViewById(R.id.username_avatar);
        TextView user_image = (TextView)child.findViewById(R.id.userimage_avatar);

        user_name.setText(user.username);
        user_image.setText(user.avatar);

        child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectUser((String)view.getTag());
            }
        });
        userview.addView(child);
        userlist.put(user.username,child);
        child.setTag(user.username);
        return child;
    }
}
