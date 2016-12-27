package com.quaap.primary.math;


import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quaap.primary.R;
import com.quaap.primary.base.BaseActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class BasicMathActivity extends BaseActivity implements BaseActivity.AnswerGivenListener<Integer> {

    private int num1;
    private int num2;
    private MathOp op;
    private int answer;

    //public static final String LevelSetName = "Math1Levels";


    public enum Mode {Buttons, Input}

    public Mode mode = Mode.Buttons;
    //Mode.Input doesn't work yet.

    public BasicMathActivity() {
       super(R.layout.activity_math1);
        setFasttimes(900, 1800, 3000);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        int orientation = getResources().getConfiguration().orientation;
        if (orientation== Configuration.ORIENTATION_LANDSCAPE) {
            LinearLayout answerarea = (LinearLayout)findViewById(R.id.answer_area);
            answerarea.setOrientation(LinearLayout.HORIZONTAL);
        }
    }

    @Override
    protected void onShowLevel() {

    }


    @Override
    protected void showProbImpl() {

        TextView num1txt = (TextView)findViewById(R.id.num1);
        TextView num2txt = (TextView)findViewById(R.id.num2);
        TextView optxt = (TextView)findViewById(R.id.op);

        makeRandomProblem();

        num1txt.setText(String.format(Locale.getDefault(),"%d",num1));
        num2txt.setText(String.format(Locale.getDefault(),"%d",num2));
        optxt.setText(op.toString());
        answer = getAnswer(num1, num2, op);

        LinearLayout answerarea = (LinearLayout)findViewById(R.id.answer_area);
        float fontsize = num1txt.getTextSize();

        if (mode==Mode.Buttons) {
            makeAnswerButtons(answerarea, fontsize);
        } else if (mode==Mode.Input) {
            makeAnswerBox(answerarea, fontsize);
        }

    }


    @Override
    protected void setStatus(String text) {
        final TextView status = (TextView)findViewById(R.id.txtstatus);
        status.setText(text);

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



    public boolean answerGiven(Integer ans) {

        boolean isright = ans == answer;

        int points = 1 + (Math.abs(num1)+Math.abs(num2)) * (op.ordinal()+1);
        answerDone(isright, points, num1 + op.toString() + num2, answer+"", ans+"");


        return isright;

    }



    private void makeRandomProblem() {

        int tries = 0;
        BasicMathLevel level = (BasicMathLevel) levels[levelnum];
        boolean negsallowed = level.getNegatives() != Negatives.None;
        do {
            int min = 0;
            int max = level.getMaxNum();
            if (negsallowed) {
                min = -max;
            }
            if (correct > level.getRounds() / 2) { //increase difficulty in second half of level
                if (negsallowed) {
                    num1 = Math.random()>5? getRand(max/2-1, max) : getRand(min, min/2+1);
                } else {
                    num1 = getRand(max/2-1, max);
                }
            } else {
                num1 = getRand(min/2, max/2 + 1);
            }
            num2 = getRand(min, max);
            if ((num2 == 0 || num2 == 1) && Math.random() > .3) num2=getRand(2, max); //reduce number of x+0 and x+1

            if (level.getNegatives() == Negatives.Required && num1>=0 && num2>=0) { //force a negative value
                num1 = -getRand(1, max);
            }

            op = MathOp.random(level.getMinMathOp(), level.getMaxMathOp());

            if ((op == MathOp.Minus && level.getNegatives() == Negatives.None) || op == MathOp.Divide) {
                if (num1 < num2) {
                    int tmp = num1;
                    num1 = num2;
                    num2 = tmp;
                }
                if (op == MathOp.Divide) {
                    if (num2 == 0) {
                        num2=getRand(1, max);
                        if (num1<num2) num1=getRand(num2, max);
                    }
                    if (num1 % num2 != 0) {
                        num1 = num1 * num2;
                    }
                }
            }
            if (op == MathOp.Minus) {
                if (getRand(0,10)>5) num1 = num1 + num2;
            }
            //prevent 2 identical problems in a row
        } while (tries++<50 && seenProblem(num1, num2, op));


    }

    //Mode.Input impl:

    private EditText answerBox;
    private void makeAnswerBox(LinearLayout answerarea, float fontsize) {
        //answerarea.removeAllViews();
        if (answerBox==null) {
            answerBox = new EditText(this);
            answerBox.setTextSize(fontsize);
            answerBox.setEms(1);
            answerBox.setInputType(InputType.TYPE_CLASS_NUMBER);
            answerarea.addView(answerBox);
            answerBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if(actionId== EditorInfo.IME_ACTION_DONE){
                        answerGiven(Integer.parseInt(answerBox.getText().toString()));
                    }
                    return false;
                }
            });
            answerBox.setGravity(Gravity.RIGHT);
//            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
//            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
        }
        showSoftKeyboard(answerBox);

//        answerBox.requestFocus();

    }


    //Mode.Buttons impl:
//    @NonNull
//    private List<Integer> getAnswerChoices2(int numans) {
//        List<Integer> answers = new ArrayList<>();
//        answers.add(answer);
//
//        for (int i=1; i<numans; i++) {
//            int tmpans;
//            do {
//                int range = Math.abs(answer/10) + 6;
//                int min = -Math.min(answer*2/3, range); // <-- prevent negatives
//                if (((BasicMathLevel) levels[levelnum]).getNegatives()!=Negatives.None) {
//                    min = -range;
//                }
//                tmpans = answer + getRand(min, range);
//            } while (answers.contains(tmpans));
//            answers.add(tmpans);
//        }
//        Collections.shuffle(answers);
//        return answers;
//    }

    private List<Integer> getAnswerChoices(int numans) {
        List<Integer> answers = new ArrayList<>();
        answers.add(answer);

        boolean allownegs = ((BasicMathLevel) levels[levelnum]).getNegatives()!=Negatives.None;
        int range = Math.abs(answer/10);
        for (int i=1; i<numans; i++) {
            int tmpans;
            do {

                if (allownegs) {

                    if (getRand(10)>8) {
                        tmpans = Math.abs(num1) + Math.abs(num2);
                    } else if (getRand(10)>5) {
                        tmpans = -answer;
                    } else {
                        tmpans = answer + getRand(-3 - range, 3 + range);
                    }

                } else if (getRand(10)>3) {
                    tmpans = answer + getRand(-3 - range, 3 + range); //normal random
                } else {
                    //tricky answers
                    switch (op) {
                        case Plus:
                            tmpans = Math.max(num1, num2) - Math.min(num1, num2);
                            break;
                        case Times:
                            tmpans = num1 * (num2 + getRand(1,2));
                            break;
                        case Divide:
                            tmpans = num1 - num2;
                            break;
                        case Minus:
                        default:
                            tmpans = num1 + num2;
                            break;
                    }
                }

            } while (answers.contains(tmpans) || (!allownegs && tmpans<0) );
            answers.add(tmpans);
        }
        Collections.shuffle(answers);
        return answers;
    }


    @SuppressLint("RtlHardcoded")
    private void makeAnswerButtons(LinearLayout answerarea, float fontsize) {

        List<Integer> answers = getAnswerChoices(4);

        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lparams.gravity = Gravity.RIGHT;
        lparams.weight = 1;


        makeChoiceButtons(answerarea, answers, this, fontsize, lparams, Gravity.RIGHT);

    }



}

