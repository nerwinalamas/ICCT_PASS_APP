package com.example.icctpassapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UpdateProfileActivity extends AppCompatActivity {

    private TextInputLayout TI_fullName, TI_course, TI_email, TI_password;
    private Button btn_update;
    String Fullname, Course, Email, Password, ConfirmPassword;

    FirebaseUser user;
    DatabaseReference reference;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        TI_fullName = (TextInputLayout) findViewById(R.id.update_profile_fullname);
        TI_course = (TextInputLayout) findViewById(R.id.update_profile_course);
        TI_email = (TextInputLayout) findViewById(R.id.update_profile_email);
        TI_password = (TextInputLayout) findViewById(R.id.update_profile_password);

        btn_update = (Button) findViewById(R.id.btn_update);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if (userProfile!=null){
                    String fn = userProfile.getFullName();
                    String e = userProfile.getEmail();
                    String c = userProfile.getCourse();
                    String pass = userProfile.getPassword();

                    TI_fullName.getEditText().setText(fn);
                    TI_course.getEditText().setText(c);
                    TI_email.getEditText().setText(e);
                    TI_password.getEditText().setText(pass);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateProfileActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateData(View view) {
        if (isChanged()) {
            Toast.makeText(UpdateProfileActivity.this,"Data has been updated", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isChanged() {
        if ((Fullname == null || !Fullname.equals(TI_fullName.getEditText().getText().toString()))
                || (Course == null || !Course.equals(TI_course.getEditText().getText().toString()))
                || (Password == null || !Password.equals(TI_password.getEditText().getText().toString()))) {
            reference.child(userID).child("fullName").setValue(TI_fullName.getEditText().getText().toString());
            reference.child(userID).child("course").setValue(TI_course.getEditText().getText().toString());
            reference.child(userID).child("password").setValue(TI_password.getEditText().getText().toString());
            return true;
        }
        else {
            return false;
        }
    }
}