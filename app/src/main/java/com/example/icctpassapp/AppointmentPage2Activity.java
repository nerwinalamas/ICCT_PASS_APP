package com.example.icctpassapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.icctpassapp.models.Appointments;
import com.example.icctpassapp.models.HealthForm;
import com.example.icctpassapp.models.ScannedAppointment;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

public class AppointmentPage2Activity extends AppCompatActivity {

    Button btn_scan;
    RecyclerView recyclerView;
    AppointmentAdapter appointmentAdapter;
    Appointments appointments;
    ArrayList<ScannedAppointment> scannedAppointments;
    ValueEventListener scannedAppointmentListener;
    DatabaseReference databaseReference;
    String userId;

    String TAG = "AppointmentPage2ActivityTAG";

    FloatingActionButton fab_dl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_page2);

        appointments = (Appointments) getIntent().getSerializableExtra("appointment");

        scannedAppointments = new ArrayList<>();

        TextView tvRoomName = findViewById(R.id.tv_rname_page2);
        TextView tvSubName = findViewById(R.id.tv_sname_page2);
        TextView tvTime = findViewById(R.id.tv_time_page2);

        fab_dl = (FloatingActionButton) findViewById (R.id.download_btn_appointment);

        tvRoomName.setText(appointments.getApRoomName());
        tvSubName.setText(appointments.getApSubName());
        tvTime.setText(appointments.getApTime());

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_room);
        recyclerView.setHasFixedSize(true);

        btn_scan = (Button) findViewById(R.id.btn_scan_appointment);

        appointmentAdapter = new AppointmentAdapter(scannedAppointments);
        recyclerView.setAdapter(appointmentAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(AppointmentPage2Activity.this));

//        getScannedAppointments();

        btn_scan.setOnClickListener(view -> {
            IntentIntegrator intentIntegrator = new IntentIntegrator(AppointmentPage2Activity.this);
            intentIntegrator.setOrientationLocked(true);
            intentIntegrator.setBeepEnabled(true);
            intentIntegrator.setCaptureActivity(ScanQR3.class);
            intentIntegrator.initiateScan();
        });

        //APPOINTMENT DOWNLOAD BUTTON
        fab_dl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Download Button Appointment");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @NonNull Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                makeText(getApplicationContext(), "No result found", LENGTH_SHORT).show();
            } else {
                String scannedAppointments = result.getContents();
                Log.d(TAG, "User id: " + scannedAppointments);
                getVisitorsInfo(scannedAppointments);
//                getDatabasePath("scan_appointments");
//                getScannedAppointments();
//                saveScannedAppointments();

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void saveScannedAppointments(HealthForm healthForm){
        ScannedAppointment scannedAppointment = new ScannedAppointment();
        scannedAppointment.setAppointments(appointments);
        scannedAppointment.setHealthForm(healthForm);
        databaseReference
                .child("scan_appointments")
                .child(userId)
                .child(appointments.getAppointmentId())
                .child(healthForm.getHfUserId())
                .setValue(scannedAppointment, (error, ref) -> {
                    Log.d(TAG, "saveScannedAppointments: ");
                });
    }

//    may error
//    private void getScannedAppointments(){
//        scannedAppointmentListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                scannedAppointments.clear();
//                if(snapshot.hasChildren()){
//                    for(DataSnapshot ds:snapshot.getChildren()){
//                        ScannedAppointment sa = ds.getValue(ScannedAppointment.class);
//                        scannedAppointments.add(sa);
//                    }
//                }
//                appointmentAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        };
//        databaseReference
//                .child("scan_appointments")
//                .child(userId)
//                .child(appointments.getAppointmentId())
//                .addValueEventListener(scannedAppointmentListener);
//    }

//    private void getUserInfo(String userId){
//        databaseReference
//                .child("Users")
//                .child(userId)
//                .addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot snapshot) {
//                        User userProfile = snapshot.getValue(User.class);
//                        if(userProfile != null) {
//                            userProfile.setUserId(userId);
//                            saveScannedAppointments(userProfile);
//                            Log.d(TAG, "User Full name: " + userProfile.getFullName());
//                        } else
//                            Log.d(TAG, "onDataChange: user is null");
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError error) {
//                        Log.d(TAG, "onCancelled: " + error.getMessage());
//                    }
//                });
//    }

    private void getVisitorsInfo(String userId){
        databaseReference
                .child("Health Declarations")
                .child(userId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        HealthForm userProfile = snapshot.getValue(HealthForm.class);
                        if(userProfile != null) {
                            userProfile.setHfName(userId);
                            saveScannedAppointments(userProfile);
                            Log.d(TAG, "User Full name: " + userProfile.getHfUserId());
                        } else
                            Log.d(TAG, "onDataChange: user is null");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d(TAG, "onCancelled: " + error.getMessage());
                    }
                });
    }

//    @Override
//    protected void onDestroy() {
//        databaseReference
//                .child("scan_appointments")
//                .child(userId)
//                .child(appointments.getAppointmentId())
//                .removeEventListener(scannedAppointmentListener);
//        super.onDestroy();
//    }
}
