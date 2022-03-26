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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.icctpassapp.models.Events;
import com.example.icctpassapp.models.ScanStudents;
import com.example.icctpassapp.models.ScannedEvent;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
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

public class EventActivity extends AppCompatActivity {

    Button btn_scan;
    RecyclerView recyclerView;
    EventAdapter eventAdapter;
    Events events;
    ArrayList<ScannedEvent> scannedEvents;
    DatabaseReference databaseReference;
    String userId;
    ValueEventListener scannedEventListener;
    FloatingActionButton fab_dl;
//    String csv = (Environment.getExternalStorageDirectory().getAbsolutePath() + "/MyAttendace.csv");

    private final String TAG = "EventActivityTAG";
    private WritableWorkbook writableWorkbook;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        scannedEvents = new ArrayList<>();

        events = (Events) getIntent().getSerializableExtra("event");
        TextView tvName = findViewById(R.id.tv_name);
        TextView tvLocation = findViewById(R.id.tv_location);
        TextView tvTime = findViewById(R.id.tv_time);
        fab_dl = (FloatingActionButton) findViewById (R.id.download_btn_event);

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

          //EVENT DOWNLOAD BUTTON
        fab_dl.setOnClickListener(view -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
                }else{
                    createExcelSheet();
                }
            }else{
                createExcelSheet();
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
            cv.put(MediaStore.MediaColumns.DISPLAY_NAME, events.getName());
            cv.put(MediaStore.MediaColumns.MIME_TYPE, "application/vnd.ms-excel");
            cv.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);
            Uri uri = resolver.insert(MediaStore.Files.getContentUri("external"), cv);
            OutputStream outputStream = resolver.openOutputStream(uri);

            writableWorkbook = Workbook.createWorkbook(outputStream, wbSettings);
            WritableSheet sheet = writableWorkbook.createSheet("sheet1", 0);
            sheet.addCell(new Label(0, 0, "Name"));
            sheet.addCell(new Label(1, 0, "Course"));
            sheet.addCell(new Label(2, 0, "Email"));

            for(int i = 0; scannedEvents.size() > i; i++){
                ScannedEvent sf = scannedEvents.get(i);
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
