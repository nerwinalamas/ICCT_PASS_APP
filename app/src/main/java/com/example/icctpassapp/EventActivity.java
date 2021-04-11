package com.example.icctpassapp;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

public class EventActivity extends AppCompatActivity {

    Button btn_scan;
    RecyclerView recyclerView;

    DataBase DB;
    ArrayList<String> id_event, Type_event, Content_event; ;
    EventAdapter eventAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        final Activity activity = this;

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_event);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(EventActivity.this));

        btn_scan = (Button) findViewById(R.id.btn_scan_event);

        DB = new DataBase(EventActivity.this);

        id_event = new ArrayList<>();
        Type_event = new ArrayList<>();
        Content_event = new ArrayList<>();

        viewScanAttendeesData();

        eventAdapter = new EventAdapter(EventActivity.this, id_event, Type_event, Content_event);
        recyclerView.setAdapter(eventAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(EventActivity.this));

        btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(EventActivity.this);
                intentIntegrator.setOrientationLocked(true);
                intentIntegrator.setBeepEnabled(true);
                intentIntegrator.setCaptureActivity(ScanQR2.class);
                intentIntegrator.initiateScan();
            }
        });
    }

    void viewScanAttendeesData(){
        Cursor cursor = DB.viewAttendeesData();
        if(cursor.getCount() == 0){
            Toast.makeText(getApplicationContext(), "Please Try Again", Toast.LENGTH_SHORT).show();
        }else{
            while (cursor.moveToNext()) {
                id_event.add(cursor.getString(0));
                Type_event.add(cursor.getString(1));
                Content_event.add(cursor.getString(2));
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(getApplicationContext(), "No result found", Toast.LENGTH_SHORT).show();
            } else {
                boolean insertScanAttendeesData = DB.insertScanAttendees(result.getFormatName(), result.getContents());
                if (insertScanAttendeesData) {
                    Cursor viewScanAttendeesData = DB.viewAttendeesData();
                    eventAdapter = new EventAdapter(getApplicationContext(), id_event, Type_event, Content_event);
                    recyclerView.setAdapter(eventAdapter);
                    eventAdapter.notifyDataSetChanged();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
