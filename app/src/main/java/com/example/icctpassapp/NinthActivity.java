package com.example.icctpassapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class NinthActivity extends AppCompatActivity {

    EditText Fullname, Section, Course, Student_no, Year_level, Contact_no, Email, Address;
    Button generate_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ninth);
        BottomNavigationView bottomNavigationView = findViewById(R.id.botnav);


        Fullname = (EditText)findViewById(R.id.fullname);
        Section = (EditText)findViewById(R.id.section);
        Course = (EditText)findViewById(R.id.course);
        Student_no = (EditText)findViewById(R.id.student_no);
        Year_level = (EditText)findViewById(R.id.year_level);
        Contact_no = (EditText)findViewById(R.id.contactno);
        Email = (EditText)findViewById(R.id.email);
        Address = (EditText)findViewById(R.id.address);

        generate_btn = (Button)findViewById(R.id.btn_generate);

        bottomNavigationView.setSelectedItemId(R.id.create_qrcode);

        generate_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullname = Fullname.getText().toString();
                String section = Section.getText().toString();
                String course = Course.getText().toString();
                String student_no = Student_no.getText().toString();
                String year_level = Year_level.getText().toString();
                String contact_no = Contact_no.getText().toString();
                String email = Email.getText().toString();
                String address = Address.getText().toString();


                Intent i = new Intent(getBaseContext(),TwelfthActivity.class);
                startActivity(i);
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(), SixthActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(),SeventhActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.about:
                        startActivity(new Intent(getApplicationContext(),EighthActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.create_qrcode:

                        return true;
                }
                return false;
            }
        });
    }
}