package com.example.icctpassapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

public class ClassActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    Button btn_scan;

    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference reference = db.getReference().child("Scan Students");

    ArrayList<String> id_class, Type_class, Content_class;
    ClassAdapter classAdapter;

    TextView cn, sc, s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class);

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

        classAdapter = new ClassAdapter(ClassActivity.this, id_class, Type_class, Content_class);
        recyclerView.setAdapter(classAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(ClassActivity.this));

        cn.setText(getIntent().getStringExtra("className"));
        sc.setText(getIntent().getStringExtra("subjectCode"));
        s.setText(getIntent().getStringExtra("section"));

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

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        reference.push().setValue(result)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(ClassActivity.this, "Scan Sucessfully", Toast.LENGTH_SHORT).show();
                        classAdapter = new ClassAdapter(getApplicationContext(), id_class, Type_class, Content_class);
                        recyclerView.setAdapter(classAdapter);
                        classAdapter.notifyDataSetChanged();
                    }
                });
        super.onActivityResult(requestCode, resultCode, data);
    }
}