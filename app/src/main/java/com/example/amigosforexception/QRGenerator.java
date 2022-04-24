package com.example.amigosforexception;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.io.File;
import java.io.UnsupportedEncodingException;

public class QRGenerator extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrgenerator);

        String data = getIntent().getStringExtra("postBillId");
        BitMatrix matrix = null;
        String path = "billQR.png";
        try {
            matrix = new MultiFormatWriter().encode(
                    new String(data.getBytes("UTF-8"), "UTF-8"),
                    BarcodeFormat.QR_CODE, 200, 200);
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}