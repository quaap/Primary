package com.quaap.primary;

import android.content.SharedPreferences;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.quaap.primary.db.PrimaryDB;
import com.quaap.primary.db.User;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class Math1Activity extends AppCompatActivity {

    private PrimaryDB db;
    private SharedPreferences mPrefs;

    private int num1;
    private int num2;
    private MathOp op;
    private int answer;

    private User user;


    private int correct=0;
    private int incorrect=0;

    private Level level = Level.PlusLevel1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mPrefs = getSharedPreferences("def", MODE_PRIVATE);
        level = Level.valueOf(mPrefs.getString("level", Level.PlusLevel1.name()));
        correct = mPrefs.getInt("correct", 0);
        incorrect = mPrefs.getInt("incorrect", 0);

        String userstr = mPrefs.getString("user", "dad");


        setContentView(R.layout.activity_math1);
        showProb();
        setLevelFields();
        db = new PrimaryDB(this);
        user = db.getUser(userstr);
        if (user == null) {
            user = db.addUser(userstr, userstr);

        }

    }


    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences.Editor ed = mPrefs.edit();

        ed.putString("user", user.getName());
        ed.putString("level", level.name());
        ed.putInt("correct", correct);
        ed.putInt("incorrect", incorrect);

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


    private void answerGiven(int ans) {

        for (Button ab: answerbuttons) {
            ab.setEnabled(false);
        }
        boolean isright = ans == answer;

        db.log(user, "Math", level.name(), num1+""+op+num2, answer+"", ans+"", isright);


        if (isright) {
            correct++;
            if (correct>=level.getRounds()) {
                db.logClass(user, "Math", level.name(), correct + incorrect, 100*correct/(float)(correct + incorrect));

                correct = 0;
                level = level.getNext();
                Toast.makeText(this,"Correct! On to " + level.toString(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this,"Correct!", Toast.LENGTH_SHORT).show();

            }
            showProb();
        } else {
            incorrect++;
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
        setLevelFields();
    }

    private void setLevelFields() {
        TextView leveltxt = (TextView)findViewById(R.id.level);
        //leveltxt.setText(String.format("%d", level.ordinal()));
        leveltxt.setText(level.toString());
        TextView correcttxt = (TextView)findViewById(R.id.correct);
        correcttxt.setText(String.format(Locale.getDefault(), "%d", correct));

        TextView neededtxt = (TextView)findViewById(R.id.needed);
        neededtxt.setText(String.format(Locale.getDefault(), "%d", level.getRounds()));

        if (correct + incorrect>0) {
            TextView scoretxt = (TextView) findViewById(R.id.score);
            scoretxt.setText(String.format(Locale.getDefault(), "%3.1f%%", 100 * correct / (float) (correct + incorrect)));
        }
    }

    private void makeRandomProblem() {
        num1 = getRand(level.getMaxNum(correct));
        num2 = getRand(level.getMaxNum(correct));
        op = MathOp.random(level.getMinMathOp(), level.getMaxMathOp());

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
                tmpans = answer + getRand(-Math.min(answer, 7), 7);
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

    public enum Level {
        PlusLevel1(MathOp.Plus, 10,  10),
        MinusLevel1(MathOp.Minus, MathOp.Minus, 10, 10),

        PlusLevel2(MathOp.Plus, 20,  20),
        MinusLevel2(MathOp.Minus, MathOp.Minus, 20, 20),

        PlusLevel3(MathOp.Plus, 50,  30),
        MinusLevel3(MathOp.Minus, MathOp.Minus, 50, 30),

        PlusLevel4(MathOp.Plus, 100, 30),
        MinusLevel4(MathOp.Minus, MathOp.Minus, 100, 30),

        ;

        private MathOp mMaxMathOp;
        private MathOp mMinMathOp;
        private int mMaxNum;
        private int mRounds;

        Level(MathOp maxMathOp, int maxNum, int rounds) {
            this(maxMathOp, MathOp.Plus, maxNum, rounds);
        }

        Level(MathOp maxMathOp, MathOp minMathOp, int maxNum, int rounds) {
            mMaxMathOp = maxMathOp;
            mMinMathOp = minMathOp;
            mMaxNum = maxNum;
            mRounds = rounds;
        }

        public Level getNext() {
            return this.ordinal() < values().length - 1
                    ? values()[this.ordinal() + 1]
                    : null;
        }

        public MathOp getMaxMathOp() {
            return mMaxMathOp;
        }

        public MathOp getMinMathOp() {
            return mMinMathOp;
        }

        public int getMaxNum() {
            return mMaxNum;
        }

        public int getMaxNum(int prevCorrect) {
            return (int)(mMaxNum * ((double)Math.max(prevCorrect, mRounds/5)/mRounds));
        }

        public int getRounds() {
            return mRounds;
        }
    }

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
            return randomEnum(MathOp.class, MathOp.Plus, upto);
        }

        public static MathOp random(MathOp start, MathOp upto) {
            return randomEnum(MathOp.class, start, upto);
        }

        public static <T extends Enum<?>> T randomEnum(Class<T> clazz, T start, T upto){

            int max = upto.ordinal();
            int min = start.ordinal();

            int x = random.nextInt(max - min +1 ) + min;
            return clazz.getEnumConstants()[x];
        }
        private static final SecureRandom random = new SecureRandom();
    }
}
