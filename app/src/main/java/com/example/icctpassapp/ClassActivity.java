package com.example.icctpassapp;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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

import com.example.icctpassapp.models.Classroom;
import com.example.icctpassapp.models.HealthForm;
import com.example.icctpassapp.models.ScanStudents;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Locale;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

//import java.awt.Button;

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

    FloatingActionButton fab_dl;

    private WritableWorkbook writableWorkbook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class);

        db = FirebaseDatabase.getInstance();
        userId = FirebaseAuth.getInstance().getUid();

        cn = (TextView) findViewById(R.id.tv_class);
        sc = (TextView) findViewById(R.id.tv_subject);
        s = (TextView) findViewById(R.id.tv_section);

        fab_dl = (FloatingActionButton) findViewById (R.id.download_btn_class);

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

        //CLASS DOWNLOAD BUTTON
        fab_dl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
                    }else{
                        createExcelSheet();
                    }
                }else{
                    createExcelSheet();
                }
            }
        });

    }

    private void createExcelSheet(){
        Log.d(TAG, "Creating Excel File");
//        val  = File("${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)}/$csvFile")
        WorkbookSettings wbSettings = new WorkbookSettings();
        wbSettings.setLocale(new Locale("en", "EN"));
        try {
            ContentResolver resolver = getContentResolver();
            ContentValues cv = new ContentValues();
            cv.put(MediaStore.MediaColumns.DISPLAY_NAME, className);
            cv.put(MediaStore.MediaColumns.MIME_TYPE, "application/vnd.ms-excel");
            cv.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);
            Uri uri = resolver.insert(MediaStore.Files.getContentUri("external"), cv);
            OutputStream outputStream = resolver.openOutputStream(uri);

            writableWorkbook = Workbook.createWorkbook(outputStream, wbSettings);
            WritableSheet sheet = writableWorkbook.createSheet("sheet1", 0);
            sheet.addCell(new Label(0, 0, "Name"));
            sheet.addCell(new Label(1, 0, "Course"));
            sheet.addCell(new Label(2, 0, "Email"));

            for(int i = 0; scanStudentsList.size() > i; i++){
                ScanStudents sf = scanStudentsList.get(i);
                User user = sf.getUser();
                int position = i+1;
                sheet.addCell(new Label(0, position, user.getFullName()));
                sheet.addCell(new Label(1, position, user.getCourse()));
                sheet.addCell(new Label(2, position, user.getEmail()));
            }

            writableWorkbook.write();
            writableWorkbook.close();
            Toast.makeText(this, "Successfully downloaded!", Toast.LENGTH_LONG).show();;
        } catch (Exception e) {
            Log.d(TAG, "createExcelSheet: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 100 && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            createExcelSheet();
        }else
            Toast.makeText(this, "Please grant permission to proceed.", Toast.LENGTH_LONG).show();
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