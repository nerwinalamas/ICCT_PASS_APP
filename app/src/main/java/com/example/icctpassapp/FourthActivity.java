package com.example.icctpassapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

// SIGN UP SCREEN

public class FourthActivity extends AppCompatActivity {

    private TextInputLayout fullname, course, password, email;
    private Button signup;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fourth);

        fullname = (TextInputLayout) findViewById(R.id.fullname);
        course = (TextInputLayout) findViewById(R.id.course);
        email = (TextInputLayout) findViewById(R.id.email);
        password = (TextInputLayout) findViewById(R.id.password);
        signup = (Button) findViewById(R.id.btn_signup);

        mAuth = FirebaseAuth.getInstance();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fn = fullname.getEditText().getText().toString().trim();
                String c = course.getEditText().getText().toString().trim();
                String e = email.getEditText().getText().toString().trim();
                String pass = password.getEditText().getText().toString().trim();

                if (fn.isEmpty()) {
                    fullname.setError("Full Name is required");
                    fullname.requestFocus();
                    return;
                }

                if (c.isEmpty()) {
                    course.setError("Course is required");
                    course.requestFocus();
                    return;
                }

                if (e.isEmpty()) {
                    email.setError("Email is required");
                    email.requestFocus();
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(e).matches()) {
                    email.setError("Please provide valid email!");
                    email.requestFocus();
                    return;
                }

                if (pass.isEmpty()) {
                    password.setError("Password is required");
                    password.requestFocus();
                    return;
                }

                if (pass.length() < 6) {
                    password.setError("Minimum password length should be 6 characters!");
                    password.requestFocus();
                }

                mAuth.createUserWithEmailAndPassword(e, pass)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()) {
                                    User user = new User(fn, c, e, pass);
                                    FirebaseDatabase.getInstance().getReference("Users")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(FourthActivity.this, "User has been registered successfully!", Toast.LENGTH_LONG).show();
                                                Intent i = new Intent(FourthActivity.this, FifthActivity.class);
                                                startActivity(i);
                                                finish();
                                            } else {
                                                Toast.makeText(FourthActivity.this, "Failed, Please try again", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                                } else {
                                    Toast.makeText(FourthActivity.this, "Failed, Please try again", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });
    }
}