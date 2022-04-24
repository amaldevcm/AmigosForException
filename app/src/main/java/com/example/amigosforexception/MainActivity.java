package com.example.amigosforexception;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Button proceed;
    EditText cust_name, cust_mobile;
    String customerName, customerMobile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        proceed = (Button) findViewById(R.id.btn_proceed);
        cust_name = findViewById(R.id.cust_name);
        cust_mobile = findViewById(R.id.cust_mobileNo);

        customerMobile = "";
        customerName = "";
        Global.getInstance().dataList = new ArrayList<>();
        Global.getInstance().dataList.clear();

        Intent intent = new Intent(MainActivity.this, ScannerActivity.class);
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customerName = cust_name.getText().toString();
                customerMobile = cust_mobile.getText().toString();
                if(formValidator(customerName, customerMobile)) {
                    Global.getInstance().setCustomerName(customerName);
                    Global.getInstance().setCustomerMobile(customerMobile);
//                    intent.putExtra("Global", global);
                    startActivity(intent);
                }
            }
        });
    }

    public boolean formValidator(String name, String mobileNo){
        TextView validName, validNumber;
        boolean valid = false;
        validName = findViewById(R.id.valid_name);
        validNumber = findViewById(R.id.valid_number);


        if(mobileNo.length() < 10 || mobileNo.length() > 10){
            cust_mobile.setBackgroundTintList(getResources().getColorStateList(R.color.red));
            validNumber.setVisibility(View.VISIBLE);
            valid = false;
        }
        else {
            cust_mobile.setBackgroundTintList(null);
            validNumber.setVisibility(View.INVISIBLE);
            valid = true;
        }

        if(name.length() == 0){
            cust_name.setBackgroundTintList(getResources().getColorStateList(R.color.red));
            validName.setVisibility(View.VISIBLE);
            return false;
        }
        else {
            cust_name.setBackgroundTintList(null);
            validName.setVisibility(View.INVISIBLE);
            if(valid)
                return true;
            else
                return false;
        }
    }
}