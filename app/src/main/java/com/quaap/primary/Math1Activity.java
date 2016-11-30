package com.quaap.primary;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Math1Activity extends AppCompatActivity {


    public enum MathOp {
        Plus("+"),
        Minus("-"),
        Times("\u00D7"),
        Divide("\u00F7");

        private String mDisplay;
        MathOp(String display) {
            mDisplay = display;
        }

        @Override
        public String toString() {
            return mDisplay;
        }

        public static MathOp random(MathOp upto) {
            return randomEnum(MathOp.class, upto);
        }

        public static <T extends Enum<?>> T randomEnum(Class<T> clazz, T upto){

            int x = random.nextInt(upto.ordinal()+1);
            return clazz.getEnumConstants()[x];
        }
        private static final SecureRandom random = new SecureRandom();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_math1);
        showProb();
    }

    private int num1;
    private int num2;
    private MathOp op;
    private int answer;

    private int level = 1;

    private List<Button> answerbuttons = new ArrayList<>();

    protected void showProb() {

        TextView num1txt = (TextView)findViewById(R.id.num1);
        TextView num2txt = (TextView)findViewById(R.id.num2);
        TextView optxt = (TextView)findViewById(R.id.op);

        num1 = getRand(5 * level);
        num2 = getRand(5 * level);
        op = MathOp.random(MathOp.Plus);

        num1txt.setText(num1+"");
        num2txt.setText(num2+"");
        optxt.setText(op+"");
        answer = getAnswer(num1, num2, op);

        LinearLayout answerarea = (LinearLayout)findViewById(R.id.answer_area);
        answerarea.removeAllViews();
        answerbuttons.clear();
        int numans = 4;
        List<Integer> answers = new ArrayList<>();
        answers.add(answer);
        for (int i=1; i<numans; i++) {
            int tmpans;
            do {
                tmpans = answer + getRand(-answer/2, Math.max(answer/2,5));
            } while (answers.contains(tmpans));
            answers.add(tmpans);
        }
        Collections.shuffle(answers);
        for (int i=0; i<numans; i++) {
            int tmpans = answers.get(i);
            Button ansbutt = new Button(this);
            ansbutt.setText(tmpans+"");
            ansbutt.setTag(tmpans);
            ansbutt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    answerGiven((int)view.getTag());
                }
            });
            answerarea.addView(ansbutt);
            answerbuttons.add(ansbutt);
        }


    }

    private void answerGiven(int ans) {

        for (Button ab: answerbuttons) {
            ab.setEnabled(false);
        }



        if (ans == answer) {
            Toast.makeText(this,"Correct!", Toast.LENGTH_SHORT).show();
            showProb();
        } else {
            Toast.makeText(this,"Wrong!", Toast.LENGTH_SHORT).show();
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    for (Button ab: answerbuttons) {
                        ab.setEnabled(true);
                    }
                }
            }, 2000);
        }
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

}
