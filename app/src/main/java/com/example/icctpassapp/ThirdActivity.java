package com.example.icctpassapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

// SIGN UP/LOGIN SCREEN

public class ThirdActivity extends AppCompatActivity {

    Button signup, login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        signup = (Button) findViewById(R.id.signup);
        login = (Button) findViewById(R.id.login);

        signup.setOnClickListener(v -> {
            Intent i = new Intent(getBaseContext(),FourthActivity.class);
            startActivity(i);
        });

        login.setOnClickListener(v -> {
            Intent i = new Intent(getBaseContext(),FifthActivity.class);
            startActivity(i);
        });
    }
}