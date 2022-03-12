package com.example.icctpassapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

// LOGIN SCREEN

public class FifthActivity extends AppCompatActivity {

    TextInputLayout Email, Password;
    TextView forgot_pass;
    Button submit;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fifth);

        Email = (TextInputLayout) findViewById(R.id.email);
        Password = (TextInputLayout) findViewById(R.id.password);
        submit = (Button) findViewById(R.id.submit);
        forgot_pass = (TextView) findViewById(R.id.forgot_pass);

        mAuth = FirebaseAuth.getInstance();

        forgot_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(FifthActivity.this, ForgotPasswordActivity.class);
                startActivity(i);
                finish();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String e = Email.getEditText().getText().toString().trim();
                String pass = Password.getEditText().getText().toString().trim();

                if (e.isEmpty()) {
                    Email.setError("Email is required");
                    Email.requestFocus();
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(e).matches()) {
                    Email.setError("Please provide valid email!");
                    Email.requestFocus();
                    return;
                }

                if (pass.isEmpty()) {
                    Password.setError("Password is required");
                    Password.requestFocus();
                    return;
                }

                if (pass.length() < 6) {
                    Password.setError("Minimum password length should be 6 characters!");
                    Password.requestFocus();
                }

                mAuth.signInWithEmailAndPassword(e, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                            if (user.isEmailVerified()){
                                Toast.makeText(FifthActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(FifthActivity.this, SeventhActivity.class);
                                startActivity(i);
                                finish();
                            } else {
                                user.sendEmailVerification();
                                Toast.makeText(FifthActivity.this , "Check your email to verify your Account", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(FifthActivity.this, "Failed, Please try again", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }
}