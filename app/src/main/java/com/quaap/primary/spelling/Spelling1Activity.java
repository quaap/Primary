package com.quaap.primary.spelling;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.quaap.primary.R;
import com.quaap.primary.base.BaseActivity;

public class Spelling1Activity extends BaseActivity implements TextToVoice.VoiceReadyListener {

    private String[] words;
    private int wordsIndex;

    TextToVoice v;
    public Spelling1Activity() {
        super(R.layout.activity_spelling1);

    }

    private EditText uinput;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        words = getResources().getStringArray(R.array.spelling_words_1);
        wordsIndex = 0;

        super.onCreate(savedInstanceState);

        uinput = (EditText)findViewById(R.id.spelling_edit) ;

        Button b = (Button)findViewById(R.id.btn_repeat);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                v.speak(words[wordsIndex]);
            }
        });

        Button done = (Button)findViewById(R.id.done_spell);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                answerReady(uinput.getText().toString());
            }
        });


        uinput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
                int result = actionId & EditorInfo.IME_MASK_ACTION;
                switch(result) {
                    case EditorInfo.IME_ACTION_DONE:
                    case EditorInfo.IME_ACTION_NEXT:
                        answerReady(uinput.getText().toString());
                        break;
                }
                return false;
            }
        });

    }

    @Override
    protected void onPause() {
        if (v!=null) {
            v.shutDown();
            v = null;
        }
        super.onPause();
    }


    @Override
    protected void onResume() {
        setReadyForProblem(false);
        findViewById(R.id.spelling_problem_area).setVisibility(View.INVISIBLE);
        v = new TextToVoice(this);
        v.setVoiceReadyListener(this);

        super.onResume();

    }

    @Override
    protected void showProbImpl() {
        String word = words[wordsIndex];
        v.speak(word);
        uinput.setText("");
        showSoftKeyboard(uinput);
    }


    protected void answerReady(String answer) {
        String word = words[wordsIndex].trim();
        int points = 0;
        boolean isright = answer.trim().equals(word);
        if (isright) {
            points = word.length() * (levelnum+1);
            wordsIndex++;
        }
        answerDone(isright, points, word, word, answer.trim());

    }

    @Override
    protected void setStatus(String text) {

    }

    final protected Handler handler = new Handler();
    @Override
    public void onVoiceReady(TextToVoice ttv) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                findViewById(R.id.spelling_problem_area).setVisibility(View.VISIBLE);
                setReadyForProblem(true);
            }
        });
        Log.d("sp1", "onVoiceReady called");
    }

    @Override
    public void onSpeakComplete(TextToVoice ttv) {
        startTimer();
    }

    @Override
    public void onError(TextToVoice ttv) {

    }
}
