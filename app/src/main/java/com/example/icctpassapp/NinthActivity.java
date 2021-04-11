package com.example.icctpassapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.File;
import java.io.FileOutputStream;


public class NinthActivity extends AppCompatActivity {

    TextInputLayout Fullname, Section, Course, Student_no, Year_level, Contact_no, Email, Address;
    ImageView generate_img;

    Button btn_save;
    FileOutputStream outputStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ninth);

        BottomNavigationView bottomNavigationView = findViewById(R.id.botnav);
        bottomNavigationView.setSelectedItemId(R.id.btm_create_qrcode);

        Fullname = (TextInputLayout) findViewById(R.id.fullname);
        Section = (TextInputLayout) findViewById(R.id.section);
        Course = (TextInputLayout) findViewById(R.id.course);
        Student_no = (TextInputLayout) findViewById(R.id.student_no);
        Year_level = (TextInputLayout) findViewById(R.id.year_level);
        Contact_no = (TextInputLayout) findViewById(R.id.contactno);
        Email = (TextInputLayout) findViewById(R.id.email);
        Address = (TextInputLayout) findViewById(R.id.address);

        btn_save = (Button) findViewById( R.id.btn_save);
        generate_img = (ImageView) findViewById(R.id.generate_qrcode);

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BitmapDrawable bitmapDrawable = (BitmapDrawable) generate_img.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();

                File filepath = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                File dir = new File(filepath.getAbsolutePath() + "/MyQR");
                dir.mkdirs();
                File file = new File(dir, System.currentTimeMillis()+ ".jpg");
                try{
                    outputStream = new FileOutputStream(file);
                }catch (Exception e){
                    e.printStackTrace();
                }
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
                Toast.makeText(getApplicationContext(), "Image Saved", Toast.LENGTH_SHORT).show();
                try{
                    outputStream.flush();
                }catch (Exception e){
                    e.printStackTrace();
                }
                try{
                    outputStream.close();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

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
                        return true;
                }
                return false;
            }
        });
    }

    public void GenerateButton(View view){
        QRCodeWriter QR = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = QR.encode(
                    Fullname.getEditText().getText().toString() + "\n" +
                            Section.getEditText().getText().toString() + "\n" +
                            Course.getEditText().getText().toString() + "\n" +
                            Student_no.getEditText().getText().toString() + "\n" +
                            Year_level.getEditText().getText().toString() + "\n" +
                            Contact_no.getEditText().getText().toString() + "\n" +
                            Email.getEditText().getText().toString() + "\n" +
                            Address.getEditText().getText().toString(),
                    BarcodeFormat.QR_CODE, 200, 200);

            Bitmap bitmap = Bitmap.createBitmap(200, 200, Bitmap.Config.RGB_565);
            for (int x = 0; x<200; x++){
                for (int y=0; y<200; y++){
                    bitmap.setPixel(x,y,bitMatrix.get(x,y)? Color.BLACK : Color.WHITE);
                }
            }
            generate_img.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

