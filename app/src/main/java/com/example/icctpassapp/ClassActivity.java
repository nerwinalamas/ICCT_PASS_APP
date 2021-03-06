package com.example.icctpassapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.icctpassapp.models.Classroom;
import com.example.icctpassapp.models.ScanStudents;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

public class ClassActivity extends AppCompatActivity {

    private static final String TAG = "ClassActivityTag";
    RecyclerView recyclerView;
    Button btn_scan;

    private FirebaseDatabase db;

    ArrayList<String> id_class, Type_class, Content_class;
    ClassAdapter classAdapter;

    TextView cn, sc, s;
    String className;
    String subjectCode;
    String section;
    String userId;

    private ArrayList<ScanStudents> scanStudentsList;
    private ValueEventListener classValueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class);

        db = FirebaseDatabase.getInstance();
        userId = FirebaseAuth.getInstance().getUid();

        cn = (TextView) findViewById(R.id.tv_class);
        sc = (TextView) findViewById(R.id.tv_subject);
        s = (TextView) findViewById(R.id.tv_section);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_class);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(ClassActivity.this));

        btn_scan = (Button) findViewById(R.id.btn_scan_class);

        id_class = new ArrayList<>();
        Type_class = new ArrayList<>();
        Content_class = new ArrayList<>();

        scanStudentsList = new ArrayList<>();
        classAdapter = new ClassAdapter(scanStudentsList);
        recyclerView.setAdapter(classAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(ClassActivity.this));

        className = getIntent().getStringExtra("className");
        subjectCode = getIntent().getStringExtra("subjectCode");
        section = getIntent().getStringExtra("section");
        cn.setText(className);
        sc.setText(subjectCode);
        s.setText(section);

        btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(ClassActivity.this);
                intentIntegrator.setOrientationLocked(true);
                intentIntegrator.setBeepEnabled(true);
                intentIntegrator.setCaptureActivity(ScanQR.class);
                intentIntegrator.initiateScan();
            }
        });
        getAllScannedStudents();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "Result Content: " + result.getContents());
        String scannedUserId = result.getContents();
        getUserInfo(scannedUserId);
    }

    private void getUserInfo(String userId){
        db.getReference()
                .child("Users")
                .child(userId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Log.d(TAG, "onDataChange: " + snapshot);
                        User userProfile = snapshot.getValue(User.class);
                        if(userProfile != null) {
                            userProfile.setUserId(userId);
                            writeScannedStudent(userProfile);
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

    private void getAllScannedStudents(){
        classValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                scanStudentsList.clear();
                if(snapshot.hasChildren()){
                    for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                        ScanStudents scanStudent = dataSnapshot.getValue(ScanStudents.class);
                        scanStudentsList.add(scanStudent);
                    }
                }
                classAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        db.getReference()
                .child("scan_classroom")
                .child(userId)
                .child(subjectCode)
                .addValueEventListener(classValueEventListener);
    }

    private void writeScannedStudent(User user){
        ScanStudents scanStudents = new ScanStudents();
        scanStudents.setUser(user);

        Classroom classrooms = new Classroom();
        classrooms.setName(className);
        classrooms.setSubjectCode(subjectCode);
        classrooms.setSection(section);

        scanStudents.setClassrooms(classrooms);
        db.getReference()
                .child("scan_classroom")
                .child(userId)
                .child(subjectCode)
                .child(user.getUserId())
                .setValue(scanStudents)
                .addOnCompleteListener(task -> {
//                    scanStudentsList.add(scanStudents);
//                    classAdapter.notifyDataSetChanged();
                });
    }

    @Override
    protected void onDestroy() {
        db.getReference()
                .child("scan_classroom")
                .child(userId)
                .child(subjectCode)
                .removeEventListener(classValueEventListener);
        super.onDestroy();
    }
}