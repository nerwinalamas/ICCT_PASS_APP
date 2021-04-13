package com.example.icctpassapp;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.icctpassapp.models.Events;
import com.example.icctpassapp.models.ScannedEvent;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

public class EventActivity extends AppCompatActivity {

    Button btn_scan;
    RecyclerView recyclerView;
    EventAdapter eventAdapter;
    Events events;
    ArrayList<ScannedEvent> scannedEvents;
    DatabaseReference databaseReference;
    String userId;
    ValueEventListener scannedEventListener;

    private final String TAG = "EventActivityTAG";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        scannedEvents = new ArrayList<>();

        events = (Events) getIntent().getSerializableExtra("event");
        TextView tvName = findViewById(R.id.tv_name);
        TextView tvLocation = findViewById(R.id.tv_location);
        TextView tvTime = findViewById(R.id.tv_time);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        userId = FirebaseAuth.getInstance().getUid();

        tvName.setText(events.getName());
        tvLocation.setText(events.getLocation());
        tvTime.setText(events.getTime());

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_event);
        recyclerView.setLayoutManager(new LinearLayoutManager(EventActivity.this));

        btn_scan = (Button) findViewById(R.id.btn_scan_event);

        eventAdapter = new EventAdapter(scannedEvents);
        recyclerView.setAdapter(eventAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(EventActivity.this));

        getScannedEvents();

        btn_scan.setOnClickListener(view -> {
            IntentIntegrator intentIntegrator = new IntentIntegrator(EventActivity.this);
            intentIntegrator.setOrientationLocked(true);
            intentIntegrator.setBeepEnabled(true);
            intentIntegrator.setCaptureActivity(ScanQR2.class);
            intentIntegrator.initiateScan();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(getApplicationContext(), "No result found", Toast.LENGTH_SHORT).show();
            } else {
                String scannedUserId = result.getContents();
                Log.d(TAG, "User id: " + scannedUserId);
                getUserInfo(scannedUserId);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void saveScannedEvents(User user){
        ScannedEvent scannedEvent = new ScannedEvent();
        scannedEvent.setUser(user);
        scannedEvent.setEvents(events);
        databaseReference
                .child("scan_events")
                .child(userId)
                .child(events.getEventId())
                .child(user.getUserId())
                .setValue(scannedEvent, (error, ref) -> {
                    Log.d(TAG, "saveScannedEvents: ");
                });
    }

    private void getScannedEvents(){
        scannedEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                scannedEvents.clear();
                if(snapshot.hasChildren()){
                    for(DataSnapshot ds:snapshot.getChildren()){
                        ScannedEvent se = ds.getValue(ScannedEvent.class);
                        scannedEvents.add(se);
                    }
                }
                eventAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        databaseReference
                .child("scan_events")
                .child(userId)
                .child(events.getEventId())
                .addValueEventListener(scannedEventListener);
    }

    private void getUserInfo(String userId){
        databaseReference
                .child("Users")
                .child(userId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User userProfile = snapshot.getValue(User.class);
                        if(userProfile != null) {
                            userProfile.setUserId(userId);
                            saveScannedEvents(userProfile);
                            Log.d(TAG, "User Full name: " + userProfile.getFullName());
                        } else
                            Log.d(TAG, "onDataChange: user is null");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d(TAG, "onCancelled: " + error.getMessage());
                    }
                });
    }

    @Override
    protected void onDestroy() {
        databaseReference
                .child("scan_events")
                .child(userId)
                .child(events.getEventId())
                .removeEventListener(scannedEventListener);
        super.onDestroy();
    }

    //    void viewScanAttendeesData(){
//        Cursor cursor = DB.viewAttendeesData();
//        if(cursor.getCount() == 0){
//            Toast.makeText(getApplicationContext(), "Please Try Again", Toast.LENGTH_SHORT).show();
//        }else{
//            while (cursor.moveToNext()) {
//                id_event.add(cursor.getString(0));
//                Type_event.add(cursor.getString(1));
//                Content_event.add(cursor.getString(2));
//            }
//        }
//    }
}
