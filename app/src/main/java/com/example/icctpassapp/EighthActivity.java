package com.example.icctpassapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EighthActivity extends AppCompatActivity {

    TextView fullname, email,
            fullname_title, fullname2,
            course_title, course2,
            email_title, email2,
            password_title, password2,
            contact_us, about;
    Button btn_logout;

    FirebaseUser user;
    DatabaseReference reference;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eighth);

        BottomNavigationView bottomNavigationView = findViewById(R.id.botnav);
        bottomNavigationView.setSelectedItemId(R.id.btm_profile);

        email = (TextView) findViewById(R.id.email);
        fullname = (TextView) findViewById(R.id.fullname);

        fullname_title = (TextView) findViewById(R.id.fullname_title);
        fullname2 = (TextView) findViewById(R.id.fullname2);
        course_title = (TextView) findViewById(R.id.course_title);
        course2 = (TextView) findViewById(R.id.course2);
        email_title = (TextView) findViewById(R.id.email_title);
        email2 = (TextView) findViewById(R.id.email2);
        password_title = (TextView) findViewById(R.id.password_title);
        password2 = (TextView) findViewById(R.id.password2);

        btn_logout = (Button) findViewById(R.id.logout);

        contact_us = (TextView) findViewById(R.id.contact_us);
        about = (TextView) findViewById(R.id.about);

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

                    fullname.setText(fn);
                    email.setText(e);

                    fullname2.setText(fn);
                    course2.setText(c);
                    email2.setText(e);
                    password2.setText(pass);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EighthActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });

        contact_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ContactUsActivity.class);
                startActivity(i);
            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), AboutActivity.class);
                startActivity(i);
            }
        });

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(EighthActivity.this, ThirdActivity.class);
                startActivity(i);
                finish();
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.btm_create_event:
                        startActivity(new Intent(getApplicationContext(), SixthActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.btm_create_classroom:
                        startActivity(new Intent(getApplicationContext(), SeventhActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.btm_profile:
                        return true;
                    case R.id.btm_create_qrcode:
                        startActivity(new Intent(getApplicationContext(), NinthActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
    }

}