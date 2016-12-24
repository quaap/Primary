package com.quaap.primary;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quaap.primary.base.data.AppData;
import com.quaap.primary.base.data.UserData;

import java.util.Map;

public class ScoresActivity extends AppCompatActivity {

    private AppData mAppdata;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);

        //ViewGroup scroll = (ViewGroup) findViewById(R.id.scores_scroll);

        LinearLayout list = (LinearLayout) findViewById(R.id.scores_list);



        mAppdata = AppData.getAppData(this);

        for(String username: mAppdata.listUsers()) {
            showUserData(list, username);
        }

    }

    private void showUserData(ViewGroup list, String username) {
        UserData user = mAppdata.getUser(username);

        TextView uname = new TextView(this);
        String avname = user.getAvatar() + " " + user.getUsername() + ": " + user.getTotalPoints();
        uname.setText(avname);
        uname.setTextSize(18);
        list.addView(uname);

        for (String sub: user.getSubjectsStarted()) {
            TextView subname = new TextView(this);
            UserData.Subject subject = user.getSubjectForUser(sub);
            String sctext = sub + ": " + subject.getTotalPoints();
            subname.setText(sctext);
            list.addView(subname);

            for (Map.Entry<String,Integer> entry: subject.getTodayPointHistory().entrySet()) {
                TextView ent = new TextView(this);
                String text = entry.getKey() + ": " +entry.getValue();
                ent.setText(text);
                ent.setPadding(16,2,2,2);
                list.addView(ent);

            }

        }

    }
}
