package com.example.icctpassapp;

import androidx.appcompat.app.AppCompatActivity;
        import android.content.Intent;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.Toast;

// LOGIN SCREEN

public class FifthActivity extends AppCompatActivity {
    EditText Email,Password;
    Button submit;
    DataBase DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fifth);

        Email = (EditText) findViewById(R.id.email);
        Password = (EditText) findViewById(R.id.password);
        submit = (Button) findViewById(R.id.submit);
        DB = new DataBase(this);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String e = Email.getText().toString();
                String pass = Password.getText().toString();

                if (e.equals("") || pass.equals("")) {
                    Toast.makeText(FifthActivity.this, "Please Enter all the fields", Toast.LENGTH_SHORT).show();
                } else {
                    Boolean checkemailpass = DB.checkemailpassword(e, pass);
                    if (checkemailpass == true ) {
                        Toast.makeText(FifthActivity.this, "Sign in Successfull", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getApplicationContext(), SeventhActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        Toast.makeText(FifthActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}