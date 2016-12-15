package com.quaap.primary;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class Math1Activity extends AppCompatActivity {

    private SharedPreferences mPrefs;

    private int num1;
    private int num2;
    private MathOp op;
    private int answer;

    private int correct=0;
    private int incorrect=0;


    private int levelnum = 0;

    private int highestLevelnum = 0;
    private int totalCorrect=0;
    private int totalIncorrect=0;
    private int tscore = 0;

    public static final String LEVELNAME = "levelnum";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPrefs = getSharedPreferences("def", MODE_PRIVATE);

        levelnum = getIntent().getIntExtra(LEVELNAME, -1);

        if (levelnum==-1) {
            levelnum = mPrefs.getInt("levelnum", levelnum);
            correct = mPrefs.getInt("correct", correct);
            incorrect = mPrefs.getInt("incorrect", incorrect);
        }
        totalCorrect = mPrefs.getInt("totalCorrect", totalCorrect);
        totalIncorrect = mPrefs.getInt("totalIncorrect", totalIncorrect);
        highestLevelnum = mPrefs.getInt("highestLevelnum", highestLevelnum);
        tscore = mPrefs.getInt("tscore", tscore);

//        if (highestLevelnum<levelnum) {
//            highestLevelnum = levelnum;
//        }



        setContentView(R.layout.activity_math1);
        int orientation = getResources().getConfiguration().orientation;
        if (orientation== Configuration.ORIENTATION_LANDSCAPE) {
            LinearLayout answerarea = (LinearLayout)findViewById(R.id.answer_area);
            answerarea.setOrientation(LinearLayout.HORIZONTAL);
        }
        showProb();
        setLevelFields();

    }


    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences.Editor ed = mPrefs.edit();

        ed.putInt("levelnum", levelnum);
        ed.putInt("correct", correct);
        ed.putInt("incorrect", incorrect);
        ed.putInt("totalCorrect", totalCorrect);
        ed.putInt("totalIncorrect", totalIncorrect);
        ed.putInt("highestLevelnum", highestLevelnum);
        ed.putInt("tscore", tscore);

        ed.commit();
    }





    private List<Button> answerbuttons = new ArrayList<>();

    protected void showProb() {

        TextView num1txt = (TextView)findViewById(R.id.num1);
        TextView num2txt = (TextView)findViewById(R.id.num2);
        TextView optxt = (TextView)findViewById(R.id.op);

        makeRandomProblem();

        num1txt.setText(Integer.toString(num1));
        num2txt.setText(Integer.toString(num2));
        optxt.setText(op.toString());
        answer = getAnswer(num1, num2, op);

        LinearLayout answerarea = (LinearLayout)findViewById(R.id.answer_area);
        answerarea.removeAllViews();
        answerbuttons.clear();

        int numans = 4;
        List<Integer> answers = getAnswerChoices(numans);

        float fontsize = num1txt.getTextSize();
        for (int i=0; i<answers.size(); i++) {
            int tmpans = answers.get(i);
            makeAnswerButton(tmpans, answerarea, fontsize);
        }

    }

    private int correctInARow = 0;

    final Handler handler = new Handler();
    private void answerGiven(int ans) {

        for (Button ab: answerbuttons) {
            ab.setEnabled(false);
        }
        boolean isright = ans == answer;

        final TextView status = (TextView)findViewById(R.id.txtstatus);
        if (isright) {
            correct++;
            correctInARow++;
            totalCorrect++;
            tscore += (Math.abs(num1)+Math.abs(num2)) * (op.ordinal()+1) * ((correctInARow+1)/2);

            if (correct>=levels[levelnum].getRounds()) {
                status.setText("Correct!");
                correct = 0;
                incorrect = 0;
                if (levelnum+1>=levels.length) {
                    status.setText("You've completed all the levels!");
                    return;
                } else {
                    if (highestLevelnum<levelnum+1) {
                        highestLevelnum = levelnum+1;
                    }
                    new AlertDialog.Builder(this)
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .setTitle("Level complete!")
                            .setMessage("Go to the next level?")
                            .setPositiveButton("Next level", new DialogInterface.OnClickListener()  {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    levelnum++;
                                    showProb();
                                    setLevelFields();
                                }

                            })
                            .setNegativeButton("Repeat this level", new DialogInterface.OnClickListener()  {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    correct = 0;
                                    incorrect = 0;
                                    showProb();
                                    setLevelFields();
                                }

                            })
                            .show();
                    //status.setText("Correct! On to " + levelnum);
                }
            } else {
                status.setText("Correct!");
            }
            final int corrects = correct;
            final int incorrects = incorrect;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (corrects == correct && incorrects == incorrect) {
                        status.setText(" ");
                    }
                }
            }, status.getText().length() * 300);
            showProb();
        } else {
            incorrect++;
            correctInARow = 0;
            totalIncorrect++;
            status.setText("Try again!");

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    for (Button ab: answerbuttons) {
                        ab.setEnabled(true);
                    }
                    status.setText(" ");
                }
            }, 1500);
        }
        setLevelFields();
    }

    private void setLevelFields() {
        TextView leveltxt = (TextView)findViewById(R.id.level);
        //leveltxt.setText(String.format("%d", level.ordinal()));
        leveltxt.setText(levels[levelnum].toString());
        TextView correcttxt = (TextView)findViewById(R.id.correct);
        correcttxt.setText(String.format(Locale.getDefault(), "%d", correct));

        TextView neededtxt = (TextView)findViewById(R.id.needed);
        neededtxt.setText(String.format(Locale.getDefault(), "%d", levels[levelnum].getRounds()));

        if (correct + incorrect>0) {
            TextView scoretxt = (TextView) findViewById(R.id.score);
            scoretxt.setText(String.format(Locale.getDefault(), "%3.1f%%", 100 * correct / (float) (correct + incorrect)));
        }

        TextView total_ratio = (TextView)findViewById(R.id.total_ratio);
        total_ratio.setText(String.format(Locale.getDefault(), "%d / %d", totalCorrect, totalCorrect + totalIncorrect));

        TextView tscore_txt = (TextView)findViewById(R.id.tscore);
        tscore_txt.setText(String.format(Locale.getDefault(), "%d", tscore));

        TextView bonuses = (TextView) findViewById(R.id.bonuses);
        if (correctInARow>1) {
            String btext = correctInARow + " in a row!";
            bonuses.setText(btext);
        } else {
            bonuses.setText(" ");
        }


    }

    private void makeRandomProblem() {
        int max = levels[levelnum].getMaxNum();
        if (correct>levels[levelnum].getRounds()/2) {
            num1 = getRand(max / 2, max);
        } else {
            num1 = getRand(max);
        }
        num2 = getRand(max);
        if (num2==0 && Math.random()>.3) num2 = getRand(1, max);
        if (num2==1 && Math.random()>.3) num2 = getRand(2, max);

        op = MathOp.random(levels[levelnum].getMinMathOp(), levels[levelnum].getMaxMathOp());

        if (op == MathOp.Minus || op == MathOp.Divide) {
            if (num1<num2) {
                int tmp = num1;
                num1 = num2;
                num2 = tmp;
            }
            if (op == MathOp.Divide) {
                if (num1%num2 != 0) {
                    num1 = num1*num2;
                }
            }
        }
    }

    @NonNull
    private List<Integer> getAnswerChoices(int numans) {
        List<Integer> answers = new ArrayList<>();
        answers.add(answer);
        for (int i=1; i<numans; i++) {
            int tmpans;
            do {
                tmpans = answer + getRand(-Math.min(answer*2/3, 7), 6);
            } while (answers.contains(tmpans));
            answers.add(tmpans);
        }
        Collections.shuffle(answers);
        return answers;
    }

    private void makeAnswerButton(int tmpans, LinearLayout answerarea, float fontsize) {
        Button ansbutt = new Button(this);
        ansbutt.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontsize);
        ansbutt.setText(tmpans+"");
        ansbutt.setTag(tmpans);
        ansbutt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                answerGiven((int)view.getTag());
            }
        });
        ansbutt.setGravity(Gravity.RIGHT);
        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lparams.gravity = Gravity.RIGHT;
        ansbutt.setLayoutParams(lparams);
        answerarea.addView(ansbutt);
        answerbuttons.add(ansbutt);
    }



    private int getAnswer(int n1, int n2, MathOp op) {
        switch (op) {
            case Plus:
                return n1 + n2;
            case Minus:
                return n1 - n2;
            case Times:
                return n1 * n2;
            case Divide:
                return n1 / n2;
            default:
                throw new IllegalArgumentException("Unknown operator: " + op);
        }
    }

    public int getRand(int upper) {
        return getRand(0,upper);
    }

    public int getRand(int lower, int upper) {
        return (int) (Math.random() * (upper + 1 - lower)) + lower;
    }

    public static final Level [] levels = {
            new Level(MathOp.Plus, MathOp.Plus, 5,  10),
            new Level(MathOp.Minus, MathOp.Minus, 5, 10),

            new Level(MathOp.Plus, MathOp.Plus, 10,  20),
            new Level(MathOp.Minus, MathOp.Minus, 10, 20),

            new Level(MathOp.Plus, MathOp.Plus, 15,  20),
            new Level(MathOp.Minus, MathOp.Minus, 15, 20),

            new Level(MathOp.Plus, MathOp.Plus, 25,  10),
            new Level(MathOp.Minus, MathOp.Minus, 25,  10),

            new Level(MathOp.Times, MathOp.Times, 5, 10),
            new Level(MathOp.Divide, MathOp.Divide, 5, 10),

            new Level(MathOp.Times, MathOp.Times, 10, 10),
            new Level(MathOp.Divide, MathOp.Divide, 10, 10),

            new Level(MathOp.Times, MathOp.Times, 12, 10),
            new Level(MathOp.Divide, MathOp.Divide, 12, 10),

            new Level(MathOp.Divide, MathOp.Times, 12, 30),

            new Level(MathOp.Divide, MathOp.Plus, 12, 200),

    };


}

