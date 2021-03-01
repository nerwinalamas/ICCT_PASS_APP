package com.example.icctpassapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

// COE LOGO SPLASH SCREEN

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Thread t = new Thread(() -> {

            try {
                Thread.sleep(2000);

                Intent i = new Intent(getBaseContext(), ThirdActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                finish();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        t.start();

    }
}