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

import com.example.icctpassapp.models.Classroom;
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
import java.util.HashMap;

public class SeventhActivity extends AppCompatActivity {

    FloatingActionButton floatingActionButton1;
    private DatabaseReference databaseReference;
    private CreateAdapter createAdapter;
    private ArrayList<Classroom> classes;
    private RecyclerView recyclerView;
    TextInputLayout className, subjectCode,section;
    private String userId;
    private ValueEventListener classRoomValueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seventh);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        userId = FirebaseAuth.getInstance().getUid();

        BottomNavigationView bottomNavigationView = findViewById(R.id.botnav);
        bottomNavigationView.setSelectedItemId(R.id.btm_create_classroom);

        floatingActionButton1 = findViewById(R.id.fab_create_classroom);

        classes = new ArrayList<>();
        createAdapter = new CreateAdapter(this, classes);

        recyclerView = findViewById(R.id.recyclerView_class);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(createAdapter);

        getClassRoom();

        //Floating Button
        floatingActionButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(SeventhActivity.this);
                dialog.setContentView(R.layout.activity_dialogbox_cc);
                dialog.setTitle("Create Classroom");

                className = (TextInputLayout) dialog.findViewById(R.id.class_name);
                subjectCode = (TextInputLayout) dialog.findViewById(R.id.subject_code);
                section = (TextInputLayout) dialog.findViewById(R.id.section);

                Button add = dialog.findViewById(R.id.add);
                Button cancel = dialog.findViewById(R.id.cancel);

                // fab1
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String cn = className.getEditText().getText().toString().trim();
                        String sc = subjectCode.getEditText().getText().toString().trim();
                        String s = section.getEditText().getText().toString().trim();

                        if (cn.isEmpty() || sc.isEmpty() || s.isEmpty()) {
                            Toast.makeText(SeventhActivity.this, "Please insert a Class Name, Subject and Section", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        HashMap<String, String> classMap = new HashMap<>();
                        classMap.put("className", cn);
                        classMap.put("subjectCode", sc);
                        classMap.put("section", s);

                        Classroom c = new Classroom();
                        c.setName(cn);
                        c.setSubjectCode(sc);
                        c.setSection(s);
                        databaseReference
                                .child("Classrooms")
                                .child(userId)
                                .push()
                                .setValue(c, (error, ref) -> {
                                    Toast.makeText(SeventhActivity.this, "Class added", Toast.LENGTH_SHORT).show();
                                });
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
                    case R.id.btm_create_event:
                        startActivity(new Intent(getApplicationContext(), SixthActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.btm_create_classroom:
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
                        startActivity(new Intent(getApplicationContext(),AppointmentActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
    }

    private void getClassRoom(){
        classRoomValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                classes.clear();
                if(snapshot.hasChildren()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Classroom classroom = dataSnapshot.getValue(Classroom.class);
                        classes.add(classroom);
                    }
                }
                createAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        databaseReference
                .child("Classrooms")
                .child(userId)
                .addValueEventListener(classRoomValueEventListener);
    }

    @Override
    protected void onDestroy() {
        databaseReference
                .child("Classrooms")
                .child(userId)
                .removeEventListener(classRoomValueEventListener);
        super.onDestroy();
    }
}
