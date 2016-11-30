package com.quaap.primary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button math_button = (Button)findViewById(R.id.math_button);
        math_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mathintent = new Intent(MainActivity.this, Math1Activity.class);
                startActivity(mathintent);

            }
        });
    }
}
