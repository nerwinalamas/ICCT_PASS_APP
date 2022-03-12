 package com.example.icctpassapp;

 import android.graphics.Bitmap;
 import android.graphics.Color;
 import android.os.Bundle;
 import android.widget.ImageView;

 import androidx.appcompat.app.AppCompatActivity;

 import com.google.firebase.auth.FirebaseAuth;
 import com.google.firebase.database.DataSnapshot;
 import com.google.firebase.database.DatabaseError;
 import com.google.firebase.database.DatabaseReference;
 import com.google.firebase.database.FirebaseDatabase;
 import com.google.firebase.database.ValueEventListener;
 import com.google.zxing.BarcodeFormat;
 import com.google.zxing.common.BitMatrix;
 import com.google.zxing.qrcode.QRCodeWriter;

 public class HealthDeclarationQRActivity extends AppCompatActivity {

    ImageView hd_generated_qr;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_declaration_qr);

        hd_generated_qr = findViewById(R.id.hd_img_qr);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Health Declarations");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                QRCodeWriter QR = new QRCodeWriter();
                try {
                    String hd = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    BitMatrix bitMatrix = QR.encode(hd,
                            BarcodeFormat.QR_CODE, 200, 200);

                    Bitmap bitmap = Bitmap.createBitmap(200, 200, Bitmap.Config.RGB_565);
                    for (int x = 0; x<200; x++){
                        for (int y=0; y<200; y++){
                            bitmap.setPixel(x,y,bitMatrix.get(x,y)? Color.BLACK : Color.WHITE);
                        }
                    }
                    hd_generated_qr.setImageBitmap(bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
