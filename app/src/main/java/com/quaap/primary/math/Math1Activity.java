package com.quaap.primary.math;


import android.support.annotation.NonNull;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quaap.primary.Levels;
import com.quaap.primary.R;
import com.quaap.primary.base.BaseActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Math1Activity extends BaseActivity {

    private int num1;
    private int num2;
    private MathOp op;
    private int answer;

    public static final String LevelSetName = "Math1Levels";



    public Math1Activity() {
        levels = Levels.getLevels(LevelSetName);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        subject = getString(R.string.subject_math1);
        int layoutId=R.layout.activity_math1;

        OnCreateCommon(layoutId);

    }



    private List<Button> answerbuttons = new ArrayList<>();

    @Override
    protected void showProbImpl() {

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

    private void answerGiven(int ans) {

        for (Button ab: answerbuttons) {
            ab.setEnabled(false);
        }
        boolean isright = ans == answer;

        answerDone(isright, (Math.abs(num1)+Math.abs(num2)) * (op.ordinal()+1),
                num1 + op.toString() + num2, answer+"", ans+"");

        final TextView status = (TextView)findViewById(R.id.txtstatus);
        if (!isright) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    for (Button ab: answerbuttons) {
                        ab.setEnabled(true);
                    }
                }
            }, 1500);
        }

    }

    private void makeRandomProblem() {
        int max = ((Math1Level)levels[levelnum]).getMaxNum();
        if (correct>levels[levelnum].getRounds()/2) {
            num1 = getRand(max / 2, max);
        } else {
            num1 = getRand(max);
        }
        num2 = getRand(max);
        if (num2==0 && Math.random()>.3) num2 = getRand(1, max);
        if (num2==1 && Math.random()>.3) num2 = getRand(2, max);

        op = MathOp.random(((Math1Level)levels[levelnum]).getMinMathOp(), ((Math1Level)levels[levelnum]).getMaxMathOp());

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


}

