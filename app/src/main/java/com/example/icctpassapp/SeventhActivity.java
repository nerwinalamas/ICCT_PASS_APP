package com.example.icctpassapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class SeventhActivity extends AppCompatActivity {

    FloatingActionButton floatingActionButton1, floatingActionButton2;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    CreateAdapter createAdapter;
    ArrayList<CreateItem> createItems = new ArrayList<>();

    CreateAdapter2 createAdapter2;
    ArrayList<CreateItem2> createItems2 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seventh);
        BottomNavigationView bottomNavigationView = findViewById(R.id.botnav);
        bottomNavigationView.setSelectedItemId(R.id.home);

        floatingActionButton1 = findViewById(R.id.create_classroom);
        floatingActionButton2 = findViewById(R.id.create_event);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        createAdapter = new CreateAdapter(this, createItems);
        recyclerView.setAdapter(createAdapter);

        createAdapter2 = new CreateAdapter2(this, createItems2);
        recyclerView.setAdapter(createAdapter2);

        //Floating Button
        floatingActionButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(SeventhActivity.this);
                // Include activity_dialogbox_ce.xml file
                dialog.setContentView(R.layout.activity_dialogbox_cc);
                // Set dialog title
                dialog.setTitle("Create Classroom");

                // set values for custom dialog components

                EditText className = dialog.findViewById(R.id.class_name);
                EditText subject = dialog.findViewById(R.id.subject_code);
                EditText section = dialog.findViewById(R.id.section);

                Button add = dialog.findViewById(R.id.add);
                Button cancel = dialog.findViewById(R.id.cancel);

                // fab1
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String class_name = String.valueOf(className.getText());
                        String subject_code = String.valueOf(subject.getText());
                        String sections = String.valueOf(section.getText());
                        CreateItem createItem = new CreateItem(class_name, subject_code, sections);
                        createItems.add(createItem);
                        createAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();

            }

        });



        floatingActionButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(SeventhActivity.this);

                // Include activity_dialogbox_ce.xml file
                dialog.setContentView(R.layout.activity_dialogbox_ce);

                // Set dialog title
                dialog.setTitle("Create Event");

                // set values for custom dialog components
                EditText eventName = dialog.findViewById(R.id.event_name);
                EditText eventLocation = dialog.findViewById(R.id.event_location);
                EditText time = dialog.findViewById(R.id.time);

                Button add = dialog.findViewById(R.id.add);
                Button cancel = dialog.findViewById(R.id.cancel);

                //fab2
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String event_name = String.valueOf(eventName.getText());
                        String event_location = String.valueOf(eventLocation.getText());
                        String oras = String.valueOf(time.getText());
                        CreateItem2 createItem2 = new CreateItem2(event_name, event_location, oras);
                        createItems2.add(createItem2);
                        createAdapter2.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });
                
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        //Bottom Navigation Bar
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(), SixthActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), SeventhActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.about:
                        startActivity(new Intent(getApplicationContext(), EighthActivity.class));
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
