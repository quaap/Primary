package com.quaap.primary;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MathMenuActivity extends AppCompatActivity implements Button.OnClickListener {

    SharedPreferences mPrefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPrefs = getSharedPreferences("def", MODE_PRIVATE);

        Button math_button = (Button)findViewById(R.id.math_button);
        math_button.setTag(-1);
        math_button.setOnClickListener(this);

        Button clear_button = (Button)findViewById(R.id.clear_button);
        clear_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(MathMenuActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Clear progress")
                        .setMessage("Are you sure you want to clear your progress?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener()  {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                clearProgress();
                            }

                        })
                        .setNegativeButton("No", null)
                        .show();


            }
        });
        checkStorageAccess();
    }

    private void showLevelButtons() {
        int highest = mPrefs.getInt("highestLevelnum", 0);

        LinearLayout button_layout = (LinearLayout)findViewById(R.id.button_layout);

        button_layout.removeAllViews();

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER_VERTICAL;

        for( Level level: Levels.Math1Levels) {

            LinearLayout levelrow = new LinearLayout(this);
            levelrow.setOrientation(LinearLayout.HORIZONTAL);
            button_layout.addView(levelrow);

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

    private void clearProgress() {
        SharedPreferences.Editor ed = mPrefs.edit();
        ed.clear();
        ed.commit();

        show_hide_gip();
        showLevelButtons();
    }

    private void show_hide_gip() {
        LinearLayout gip_layout = (LinearLayout)findViewById(R.id.gip_layout);
        TextView score_overview = (TextView)findViewById(R.id.score_overview);
        if (mPrefs.getInt("levelnum", -1)==-1) {
            gip_layout.setVisibility(View.GONE);
            score_overview.setText(" ");
        } else {
            gip_layout.setVisibility(View.VISIBLE);
            int correct = mPrefs.getInt("totalCorrect", 0);
            int incorrect = mPrefs.getInt("totalIncorrect", 0);
            int highest = mPrefs.getInt("highestLevelnum", 0)+1;
            int tscore = mPrefs.getInt("tscore", 0);
            if (correct+incorrect>0) {
                String score = "Level: " + highest + ". Correct: " + correct + "/" + (correct + incorrect) + ". Points: " + tscore;
                score_overview.setText(score);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        show_hide_gip();
        showLevelButtons();
    }

    @Override
    public void onClick(View view) {
        Intent mathintent = new Intent(MathMenuActivity.this, Math1Activity.class);
        mathintent.putExtra(BaseActivity.LEVELNAME, (int)view.getTag());
        startActivity(mathintent);
    }

    private boolean checkStorageAccess() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_WRITE_EXTERNAL_STORAGE);
            return false;
        }
        return true;
    }

    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 121;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this, "Yay!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Boo!", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
