package com.example.icctpassapp;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.icctpassapp.models.Appointments;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AppointmentActivity extends AppCompatActivity  {

    FloatingActionButton floatingActionButton3, floatingActionButton4;
    RecyclerView recyclerView3;

    private DatabaseReference dbRef;
    private String userId;
    private ValueEventListener appointmentListener;
    private ArrayList<Appointments> appointmentsList;
    ArrayList<String> id, roomName, subName, time;
    CreateAdapter3 createAdapter3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);

        dbRef = FirebaseDatabase.getInstance().getReference();
        userId = FirebaseAuth.getInstance().getUid();
        appointmentsList = new ArrayList<>();

        BottomNavigationView bottomNavigationView = findViewById(R.id.botnav);
        bottomNavigationView.setSelectedItemId(R.id.btm_appointment);

        floatingActionButton3 = findViewById(R.id.fab_create_room);
        floatingActionButton4 = findViewById(R.id.fab_health_declaration);

        recyclerView3 = (RecyclerView) findViewById(R.id.recyclerView3);
        recyclerView3.setHasFixedSize(true);

        id = new ArrayList<>();
        roomName = new ArrayList<>();
        subName = new ArrayList<>();
        time = new ArrayList<>();
        getAppointments();

        createAdapter3 = new CreateAdapter3(appointmentsList);
        recyclerView3.setAdapter(createAdapter3);
        recyclerView3.setLayoutManager(new LinearLayoutManager(AppointmentActivity.this));

        createAdapter3.setOnItemClickListener3(this::gotoCRActivity);

        //Floating Button
        floatingActionButton3.setOnClickListener(v -> {
            final Dialog dialog = new Dialog(AppointmentActivity.this);

            // Include activity_dialogbox_ce.xml file
            dialog.setContentView(R.layout.activity_dialogbox_dv);

            // Set dialog title
            //dialog.setTitle("Create Event");

            // set values for custom dialog components
            TextInputLayout roomName = dialog.findViewById(R.id.dv_room_name);
            TextInputLayout subName = dialog.findViewById(R.id.dv_sub_name);
            TextInputLayout time = dialog.findViewById(R.id.dv_time);

            Button add = dialog.findViewById(R.id.add);
            Button cancel = dialog.findViewById(R.id.cancel);

            //fab2
            add.setOnClickListener(v1 -> {

                Appointments appointments = new Appointments();
                appointments.setApRoomName(roomName.getEditText().getText().toString());
                appointments.setApSubName(subName.getEditText().getText().toString());
                appointments.setApTime(time.getEditText().getText().toString());
                createAppointment(appointments);
                dialog.dismiss();
            });

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.show();
        });

        floatingActionButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AppointmentActivity.this, HealthDeclarationFormActivity.class);
                startActivity(i);
            }
        });

        //Bottom Navigation Bar
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
                        startActivity(new Intent(getApplicationContext(), EighthActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.btm_create_qrcode:
                        startActivity(new Intent(getApplicationContext(), NinthActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.btm_appointment:
                        return true;
                }
                return false;
            }
        });
    }

    private void gotoCRActivity(int position) {
        Appointments appointments = appointmentsList.get(position);
        Intent i = new Intent(this, AppointmentPage2Activity.class);
        i.putExtra("appointment", appointments);
        startActivity(i);
    }

    private void getAppointments(){
        appointmentListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                appointmentsList.clear();
                if(snapshot.hasChildren()){
                    for(DataSnapshot ds:snapshot.getChildren()){
                        Appointments appointments = ds.getValue(Appointments.class);
                        if(appointments != null) {
                            appointments.setAppointmentId(ds.getKey());
                            appointmentsList.add(appointments);
                        }
                    }
                }
                createAdapter3.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        dbRef.child("Appointments")
                .child(userId)
                .addValueEventListener(appointmentListener);
    }

    private void createAppointment(Appointments appointments){

        dbRef.child("Appointments")
                .child(userId)
                .push()
                .setValue(appointments, (error, ref) -> Toast.makeText(getApplicationContext(), "Created Successfully", Toast.LENGTH_SHORT).show());
    }

    @Override
    protected void onDestroy() {
        dbRef.child("Appointments")
                .child(userId)
                .removeEventListener(appointmentListener);
        super.onDestroy();
    }

}
