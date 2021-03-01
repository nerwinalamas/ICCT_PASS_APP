package com.example.icctpassapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

// SIGN UP SCREEN

public class FourthActivity extends AppCompatActivity {

    EditText LastName, FirstName, MiddleName, Password, Repassword, ContactNumber, Email;
    Button signup;
    DataBase DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fourth);

        LastName = (EditText)findViewById(R.id.lastname);
        FirstName = (EditText)findViewById(R.id.firstname);
        MiddleName = (EditText)findViewById(R.id.middlename);
        ContactNumber = (EditText)findViewById(R.id.contactno);
        Email = (EditText)findViewById(R.id.email);
        Password = (EditText)findViewById(R.id.password);
        Repassword = (EditText)findViewById(R.id.retypepassword);
        signup = (Button)findViewById(R.id.signup);
        DB = new DataBase(this);


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ln = LastName.getText().toString();
                String fn = FirstName.getText().toString();
                String mn = MiddleName.getText().toString();
                String cn = ContactNumber.getText().toString();
                String e = Email.getText().toString();
                String pass = Password.getText().toString();
                String repass = Repassword.getText().toString();


                if(ln.equals(" ")||fn.equals(" ")||mn.equals(" ")||cn.equals(" ")||e.equals(" ")||pass.equals(" ")||repass.equals(" "))
                    Toast.makeText(FourthActivity.this, "Please enter all the fields", Toast.LENGTH_LONG).show();
                else{
                    if(pass.equals(repass)){
                        Boolean checkuser = DB.checkemail(e);
                        if(checkuser==false){
                            Boolean insertData = DB.insertData(ln, fn, mn, cn, e, pass, repass);
                            if(insertData==true){
                                Toast.makeText(FourthActivity.this, "Registered successfully", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getApplicationContext(),FifthActivity.class);
                                startActivity(intent);
                            }else{
                                Toast.makeText(FourthActivity.this, "Registration failed", Toast.LENGTH_LONG).show();
                            }
                        }
                        else{
                            Toast.makeText(FourthActivity.this, "User already exists! please sign in", Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(FourthActivity.this, "Passwords not matching", Toast.LENGTH_LONG).show();
                    }
                } }
        });
    }
}