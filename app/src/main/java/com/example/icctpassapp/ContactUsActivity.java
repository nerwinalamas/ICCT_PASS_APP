package com.example.icctpassapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.getbase.floatingactionbutton.FloatingActionButton;

public class ContactUsActivity extends AppCompatActivity {

    FloatingActionButton fab_back;
    EditText contact_subject, contact_message, contact_email;
    Button btn_send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        fab_back = (FloatingActionButton) findViewById(R.id.back);

        contact_subject = (EditText) findViewById(R.id.contact_subject);
        contact_message = (EditText) findViewById(R.id.contact_message);
        contact_email = (EditText) findViewById(R.id.contact_email);

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
                String UsersEmail = contact_email.getText().toString();
                String Email = "icctpassapp@gmail.com";

                if (Subject.isEmpty() && Message.isEmpty() && UsersEmail.isEmpty()) {
                    Toast.makeText(ContactUsActivity.this, "Please, Fill out the Empty Fields", Toast.LENGTH_LONG).show();
                } else {
                    Intent sendEmail = new Intent(android.content.Intent.ACTION_SEND);

                    sendEmail.setType("plain/text");
                    sendEmail.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{Email});
                    sendEmail.putExtra(android.content.Intent.EXTRA_SUBJECT, Subject);
                    sendEmail.putExtra(android.content.Intent.EXTRA_TEXT,
                            "Email: " + UsersEmail + '\n' + "Message: "+'\n' + Message);
                    startActivity(Intent.createChooser(sendEmail, "Send mail..."));
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}