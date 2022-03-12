package com.example.icctpassapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.icctpassapp.models.HealthForm;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HealthDeclarationFormActivity extends AppCompatActivity {

    TextInputLayout name, address, contactNo, purposeOfVisit, temperature;
    Button generateQr;
    RadioButton q1_yes, q1_no, q2_yes, q2_no, q3_yes, q3_no, q4_yes, q4_no, q5_yes, q5_no;
    RadioGroup rg1, rg2, rg3, rg4, rg5;
    CheckBox chk_confirmation;
    HealthForm healthForm;

    private DatabaseReference databaseReference;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_declaration_form);

        name = (TextInputLayout) findViewById(R.id.hd_name);
        address = (TextInputLayout) findViewById(R.id.hd_address);
        contactNo = (TextInputLayout) findViewById(R.id.hd_contact_no);
        purposeOfVisit = (TextInputLayout) findViewById(R.id.hd_purpose_of_visit);
        temperature = (TextInputLayout) findViewById(R.id.hd_temperature);

        generateQr = (Button) findViewById(R.id.hd_btn_generate_qr);

        q1_yes = findViewById(R.id.hd_q1_yes);
        q1_no = findViewById(R.id.hd_q1_no);
        q2_yes = findViewById(R.id.hd_q2_yes);
        q2_no = findViewById(R.id.hd_q2_no);
        q3_yes = findViewById(R.id.hd_q3_yes);
        q3_no = findViewById(R.id.hd_q3_no);
        q4_yes = findViewById(R.id.hd_q4_yes);
        q4_no = findViewById(R.id.hd_q4_no);
        q5_yes = findViewById(R.id.hd_q5_yes);
        q5_no = findViewById(R.id.hd_q5_no);

        rg1 = findViewById(R.id.hd_rg1_yesOrNo);
        rg2 = findViewById(R.id.hd_rg2_yesOrNo);
        rg3 = findViewById(R.id.hd_rg3_yesOrNo);
        rg4 = findViewById(R.id.hd_rg4_yesOrNo);
        rg5 = findViewById(R.id.hd_rg5_yesOrNo);

        chk_confirmation = findViewById(R.id.hd_confirmation);

        healthForm = new HealthForm();

        databaseReference = FirebaseDatabase.getInstance().getReference();
        userId = FirebaseAuth.getInstance().getUid();

        generateQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Name = name.getEditText().getText().toString().trim();
                String Address = address.getEditText().getText().toString().trim();
                String ContactNo = contactNo.getEditText().getText().toString().trim();
                String PurposeOfVisit = purposeOfVisit.getEditText().getText().toString().trim();
                String Temperature = temperature.getEditText().getText().toString().trim();

                String question1_yes = q1_yes.getText().toString();
                String question1_no = q1_no.getText().toString();
                String question2_yes = q2_yes.getText().toString();
                String question2_no = q2_no.getText().toString();
                String question3_yes = q3_yes.getText().toString();
                String question3_no = q3_no.getText().toString();
                String question4_yes = q4_yes.getText().toString();
                String question4_no = q4_no.getText().toString();
                String question5_yes = q5_yes.getText().toString();
                String question5_no = q5_no.getText().toString();

                //String radGroup1 = rg1.toString();

                if (Name.isEmpty()) {
                    name.setError("Full Name is required");
                    name.requestFocus();
                    return;
                }

                if (Address.isEmpty()) {
                    address.setError("Address is required");
                    address.requestFocus();
                    return;
                }

                if (ContactNo.isEmpty()) {
                    contactNo.setError("Contact Number is required");
                    contactNo.requestFocus();
                    return;
                }

                if (PurposeOfVisit.isEmpty()) {
                    purposeOfVisit.setError("Purpose of Visit is required");
                    purposeOfVisit.requestFocus();
                    return;
                }

                if (Temperature.isEmpty()) {
                    temperature.setError("Temperature is required");
                    temperature.requestFocus();
                    return;
                }

                if (Temperature.isEmpty()) {
                    temperature.setError("Temperature is required");
                    temperature.requestFocus();
                    return;
                }

                healthForm.setHfName(name.getEditText().getText().toString().trim());
                healthForm.setHfAddress(address.getEditText().getText().toString().trim());
                healthForm.setHfContact(contactNo.getEditText().getText().toString().trim());
                healthForm.setHfPurposeOfVisit(purposeOfVisit.getEditText().getText().toString().trim());
                healthForm.setHfTemperature(temperature.getEditText().getText().toString().trim());

                if (q1_no.isChecked() && q2_no.isChecked() && q3_no.isChecked() && q4_no.isChecked() && q5_no.isChecked() && chk_confirmation.isChecked()){
                    healthForm.setHfQuestion1(question1_no);
                    healthForm.setHfQuestion2(question2_no);
                    healthForm.setHfQuestion3(question3_no);
                    healthForm.setHfQuestion4(question4_no);
                    healthForm.setHfQuestion5(question5_no);
                    databaseReference.child("Health Declarations").child(userId).setValue(healthForm);

                    Intent i = new Intent(getApplicationContext(), HealthDeclarationQRActivity.class);
                    startActivity(i);
                } else {
                    Toast.makeText(HealthDeclarationFormActivity.this, "Unfortunately, you cannot enter school premises, " +
                            "Please Consult to your Doctor", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
