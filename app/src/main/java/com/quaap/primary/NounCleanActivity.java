package com.quaap.primary;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.quaap.primary.base.component.ActivityWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class NounCleanActivity extends AppCompatActivity {

    String [] plurals;
    File outdir;
    FileWriter badpluralsfile;
    FileWriter goodpluralsfile;

    int onval = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noun_clean);

        Button good = (Button)findViewById(R.id.noun_clean_good);
        Button bad = (Button)findViewById(R.id.noun_clean_bad);






        good.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    goodpluralsfile.write("<item>" + plurals[onval] + "</item><item>" + plurals[onval+1] + "</item>");
                    goodpluralsfile.write("\n");
                    goodpluralsfile.flush();
                    onval += 2;
                } catch (IOException e) {
                    e.printStackTrace();
                }

                loadNextPlural();
            }
        });

        bad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    badpluralsfile.write(plurals[onval]);
                    badpluralsfile.write("\n");
                    badpluralsfile.flush();
                    onval += 2;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                loadNextPlural();
            }
        });


        plurals = getResources().getStringArray(R.array.plurals);

    }



    private void loadNextPlural() {
        TextView noun = (TextView)findViewById(R.id.noun_clean_noun);
        TextView plural = (TextView)findViewById(R.id.noun_clean_plural);


        noun.setText(plurals[onval]);
        plural.setText(plurals[onval+1]);
    }

    /**
     * Dispatch onPause() to fragments.
     */
    @Override
    protected void onPause() {

        getSharedPreferences(this.getClass().getName(), MODE_PRIVATE).edit().putInt("onval",onval).apply();
        super.onPause();
        try {
            badpluralsfile.close();
            goodpluralsfile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        onval = getSharedPreferences(this.getClass().getName(), MODE_PRIVATE).getInt("onval",onval);

        outdir = ActivityWriter.getAppDocumentsDir(NounCleanActivity.this);

        try {
            badpluralsfile = new FileWriter(new File(outdir,"badplurals.txt"), true);
            goodpluralsfile = new FileWriter(new File(outdir,"goodplurals.txt"), true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        loadNextPlural();
    }
}
