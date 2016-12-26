package com.quaap.primary.spelling;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.quaap.primary.R;
import com.quaap.primary.base.BaseActivity;

public class Spelling1Activity extends BaseActivity {

    public static final String LevelSetName = "Spelling1Levels";

    TextToVoice v;
    public Spelling1Activity() {
        super(R.layout.activity_spelling1);

    }


    @Override
    protected void showProbImpl() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        v = new TextToVoice(this);

        Button b = (Button)findViewById(R.id.button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                v.speak("Hello, World.");
            }
        });

    }

    @Override
    protected void setStatus(String text) {

    }


}
