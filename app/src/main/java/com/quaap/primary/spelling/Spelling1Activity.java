package com.quaap.primary.spelling;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.quaap.primary.R;
import com.quaap.primary.base.BaseActivity;

public class Spelling1Activity extends BaseActivity {

    public static final String LevelSetName = "Spelling1Levels";

    public Spelling1Activity() {
        super(LevelSetName, R.string.subject_spelling1, R.layout.activity_spelling1, R.id.txtstatus);
    }


    @Override
    protected void showProbImpl() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


}
