package com.example.icctpassapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.icctpassapp.models.Events;
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

public class SixthActivity extends AppCompatActivity {

    FloatingActionButton floatingActionButton2;
    RecyclerView recyclerView2;

    DataBase DB;
    ArrayList<String> id, eventName, eventLocation, time;
    CreateAdapter2 createAdapter2;
    private DatabaseReference dbRef;
    private String userId;
    private ArrayList<Events> eventList;
    private ValueEventListener eventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sixth);

        dbRef = FirebaseDatabase.getInstance().getReference();
        userId = FirebaseAuth.getInstance().getUid();
        eventList = new ArrayList<>();
        getEvents();

        BottomNavigationView bottomNavigationView = findViewById(R.id.botnav);
        bottomNavigationView.setSelectedItemId(R.id.btm_create_event);

        floatingActionButton2 = findViewById(R.id.fab_create_event);

        recyclerView2 = findViewById(R.id.recyclerView2);
        recyclerView2.setHasFixedSize(true);

        DB = new DataBase(SixthActivity.this);

        id = new ArrayList<>();
        eventName = new ArrayList<>();
        eventLocation = new ArrayList<>();
        time = new ArrayList<>();

//        storeDataInArrays();

        createAdapter2 = new CreateAdapter2(eventList);
        recyclerView2.setAdapter(createAdapter2);
        recyclerView2.setLayoutManager(new LinearLayoutManager(SixthActivity.this));

        createAdapter2.setOnItemClickListener2(this::gotoCEActivity);

        //Floating Button
        floatingActionButton2.setOnClickListener(v -> {
            final Dialog dialog = new Dialog(SixthActivity.this);

            // Include activity_dialogbox_ce.xml file
            dialog.setContentView(R.layout.activity_dialogbox_ce);

            // Set dialog title
            dialog.setTitle("Create Event");

            // set values for custom dialog components
            TextInputLayout eventName = dialog.findViewById(R.id.event_name);
            TextInputLayout eventLocation = dialog.findViewById(R.id.event_location);
            TextInputLayout time = dialog.findViewById(R.id.time);

            Button add = dialog.findViewById(R.id.add);
            Button cancel = dialog.findViewById(R.id.cancel);

            //fab2
            add.setOnClickListener(v1 -> {
//                        DataBase DB = new DataBase(SixthActivity.this);
//                        DB.insertEvent(eventName.getEditText().getText().toString(),
//                                eventLocation.getEditText().getText().toString(),
//                                time.getEditText().getText().toString());
//                        Toast.makeText(getApplicationContext(), "Created Successfully", Toast.LENGTH_SHORT).show();
//                        dialog.dismiss();

                Events events = new Events();
                events.setName(eventName.getEditText().getText().toString());
                events.setLocation(eventLocation.getEditText().getText().toString());
                events.setTime(time.getEditText().getText().toString());
                createEvent(events);
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

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.btm_create_event:
                        return true;
                    case R.id.btm_create_classroom:
                        startActivity(new Intent(getApplicationContext(), SeventhActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.btm_profile:
                        startActivity(new Intent(getApplicationContext(),EighthActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.btm_create_qrcode:
                        startActivity(new Intent(getApplicationContext(),NinthActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
    }

    void storeDataInArrays(){
        Cursor cursor = DB.viewEvent();
        if(cursor.getCount() == 0){
            Toast.makeText(getApplicationContext(), "No Data Found", Toast.LENGTH_SHORT).show();
        }else{
            while (cursor.moveToNext()){
                id.add(cursor.getString(0));
                eventName.add(cursor.getString(1));
                eventLocation.add(cursor.getString(2));
                time.add(cursor.getString(3));
            }
        }
    }

    private void gotoCEActivity(int position) {
        Events events = eventList.get(position);
        Intent i = new Intent(this, EventActivity.class);
        i.putExtra("event", events);
        startActivity(i);
    }


    private void getEvents(){
        eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                eventList.clear();
                if(snapshot.hasChildren()){
                    for(DataSnapshot ds:snapshot.getChildren()){
                        Events events = ds.getValue(Events.class);
                        if(events != null) {
                            events.setEventId(ds.getKey());
                            eventList.add(events);
                        }
                    }
                }
                createAdapter2.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        dbRef.child("Events")
                .child(userId)
                .addValueEventListener(eventListener);
    }

    private void createEvent(Events events){

        dbRef.child("Events")
                .child(userId)
                .push()
                .setValue(events, (error, ref) -> Toast.makeText(getApplicationContext(), "Created Successfully", Toast.LENGTH_SHORT).show());
    }

    @Override
    protected void onDestroy() {
        dbRef.child("Events")
                .child(userId)
                .removeEventListener(eventListener);
        super.onDestroy();
    }
}