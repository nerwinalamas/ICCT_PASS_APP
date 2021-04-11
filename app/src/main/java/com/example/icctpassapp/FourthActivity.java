package com.example.icctpassapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

// SIGN UP SCREEN

public class FourthActivity extends AppCompatActivity {

    private TextInputLayout Fullname, Course, Password, Email;
    private Button signup;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fourth);

        Fullname = (TextInputLayout) findViewById(R.id.fullname);
        Course = (TextInputLayout) findViewById(R.id.course);
        Email = (TextInputLayout) findViewById(R.id.email);
        Password = (TextInputLayout) findViewById(R.id.password);
        signup = (Button) findViewById(R.id.btn_signup);


        mAuth = FirebaseAuth.getInstance();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fn = Fullname.getEditText().getText().toString().trim();
                String c = Course.getEditText().getText().toString().trim();
                String e = Email.getEditText().getText().toString().trim();
                String pass = Password.getEditText().getText().toString().trim();

                if (fn.isEmpty()) {
                    Fullname.setError("Full Name is required");
                    Fullname.requestFocus();
                    return;
                }

                if (c.isEmpty()) {
                    Course.setError("Course is required");
                    Course.requestFocus();
                    return;
                }

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

                mAuth.createUserWithEmailAndPassword(e, pass)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()) {
                                    User user = new User(fn, e, pass, c);
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