package com.quaap.primary;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quaap.primary.base.CommonBaseActivity;
import com.quaap.primary.base.data.AppData;
import com.quaap.primary.base.data.Subjects;
import com.quaap.primary.base.data.UserData;

import java.util.Map;

public class ScoresActivity extends CommonBaseActivity {

    private AppData mAppdata;

    private Subjects subjects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);

        //ViewGroup scroll = (ViewGroup) findViewById(R.id.scores_scroll);

        LinearLayout list = (LinearLayout) findViewById(R.id.scores_list);

        subjects = Subjects.getInstance(this);

        mAppdata = AppData.getAppData(this);

        for(String username: mAppdata.listUsers()) {
            showUserData(list, username);
        }

    }

    private void showUserData(ViewGroup list, String username) {
        UserData user = mAppdata.getUser(username);

        TextView uname = new TextView(this);
        String avname = user.getAvatar() + " " + user.getUsername() + ": " + user.getTotalPoints();
        uname.setPadding(0,23,0,2);
        uname.setText(avname);
        uname.setTextSize(24);
        list.addView(uname);

        GridLayout userlayout = new GridLayout(this);
        //userlayout.setOrientation(GridLayout.VERTICAL);
        userlayout.setColumnCount(2);
        userlayout.setPadding(24,8,4,16);
        list.addView(userlayout);

        for (String sub: user.getSubjectsStarted()) {

            UserData.Subject subject = user.getSubjectForUser(sub);

            addTextView(userlayout, sub + " (" + subjects.get(sub).getName() + "): ", 20, 0);
            addTextView(userlayout, subject.getTotalPoints()+"");

            Map<String,Integer> thist= subject.getTodayPointHistory();

            for (String day: AppData.sort(thist.keySet())) {
                addTextView(userlayout, day, 18, 32);
                addTextView(userlayout, thist.get(day)+"");

            }

        }

    }

    private void addTextView(GridLayout viewg, String text) {
        addTextView(viewg, text, 14, 0);
    }

    private void addTextView(GridLayout viewg, String text, float fsize, int lpadding) {
        TextView tview = new TextView(this);
        tview.setTextSize(fsize);
        tview.setPadding(lpadding+16, 6, 6 , 6);
        tview.setText(text);
        viewg.addView(tview);
    }

}
