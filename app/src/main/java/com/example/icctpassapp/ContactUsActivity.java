package com.example.icctpassapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;

public class ContactUsActivity extends AppCompatActivity {

    FloatingActionButton fab_back;
    EditText contact_subject, contact_message;
    Button btn_send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        fab_back = (FloatingActionButton) findViewById(R.id.back);

        contact_subject = (EditText) findViewById(R.id.contact_subject);
        contact_message = (EditText) findViewById(R.id.contact_message);

        btn_send = (Button) findViewById(R.id.btn_send);

        fab_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), EighthActivity.class);
                startActivity(i);
            }
        });

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Subject = contact_subject.getText().toString();
                String Message = contact_message.getText().toString();
                String Email = "icctpassapp@gmail.com";

                if (Subject.isEmpty()) {
                    Toast.makeText(ContactUsActivity.this, "Input subject", Toast.LENGTH_SHORT).show();
                } else if (Message.isEmpty()) {
                    Toast.makeText(ContactUsActivity.this, "Input some Message", Toast.LENGTH_SHORT).show();
                } else {
                    String mail = "mailto:" + Email +
                            "?&subject=" + Uri.encode(Subject) +
                            "&body=" + Uri.encode(Message);
                    Intent i = new Intent(Intent.ACTION_SENDTO);
                    i.setData(Uri.parse(mail));
                    try {
                        startActivity(Intent.createChooser(i, "Send Email.."));
                    } catch (Exception e) {
                        Toast.makeText(ContactUsActivity.this, "Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } Toast.makeText(ContactUsActivity.this, "Sending Email....", Toast.LENGTH_SHORT).show();
            }
        });

    }
}