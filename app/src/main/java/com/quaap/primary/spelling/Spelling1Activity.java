package com.quaap.primary.spelling;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.quaap.primary.R;
import com.quaap.primary.base.BaseActivity;

public class Spelling1Activity extends BaseActivity implements TextToVoice.VoiceFullyInitializedListener {

    public static final String LevelSetName = "Spelling1Levels";

    TextToVoice v;
    public Spelling1Activity() {
        super(R.layout.activity_spelling1);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Button b = (Button)findViewById(R.id.btn_repeat);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                v.speak("Hello, World.");
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
        super.onResume();

        v = new TextToVoice(this);
        v.setFullyInitializedListener(this);
    }

    @Override
    protected void showProbImpl() {

    }


    @Override
    protected void setStatus(String text) {

    }


    @Override
    public void onVoiceFullyInitialized(TextToVoice ttv) {

    }
}
