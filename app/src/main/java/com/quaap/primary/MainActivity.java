package com.quaap.primary;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements Button.OnClickListener {

    SharedPreferences mPrefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPrefs = getSharedPreferences("def", MODE_PRIVATE);

        int highest = mPrefs.getInt("highestLevelnum", 0);

        LinearLayout activity_main = (LinearLayout)findViewById(R.id.button_layout);

        Button math_button = (Button)findViewById(R.id.math_button);
        math_button.setTag(-1);
        math_button.setOnClickListener(this);

        Button clear_button = (Button)findViewById(R.id.clear_button);
        clear_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor ed = mPrefs.edit();
                ed.clear();
                ed.commit();

                show_hide_gip();

            }
        });


        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);


        for( Math1Activity.Level level: Math1Activity.levels) {



            LinearLayout levelrow = new LinearLayout(this);
            levelrow.setOrientation(LinearLayout.HORIZONTAL);
            activity_main.addView(levelrow);


            Button levelbutt = new Button(this);
            levelbutt.setLayoutParams(lp);
            levelbutt.setText("Level " + level.getLevelNum());
            levelbutt.setTag(level.getLevelNum() - 1);
            levelbutt.setOnClickListener(this);
            levelrow.addView(levelbutt);

            TextView desc = new TextView(this);
            desc.setText(level.getName());
            desc.setLayoutParams(lp);
            desc.setTextSize(16);
            levelrow.addView(desc);

            boolean beenthere = level.getLevelNum()-1<=highest;
            levelbutt.setEnabled(beenthere);
            desc.setEnabled(beenthere);


        }

    }

    private void show_hide_gip() {
        LinearLayout gip_layout = (LinearLayout)findViewById(R.id.gip_layout);

        if (mPrefs.getInt("levelnum", -1)==-1) {
            gip_layout.setVisibility(View.GONE);
        } else {
            gip_layout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        show_hide_gip();
    }

    @Override
    public void onClick(View view) {
        Intent mathintent = new Intent(MainActivity.this, Math1Activity.class);
        mathintent.putExtra(Math1Activity.LEVELNAME, (int)view.getTag());
        startActivity(mathintent);
    }
}
