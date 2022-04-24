package com.example.amigosforexception;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

public class ItemDetailsActivity extends AppCompatActivity {
    Intent i;
    TextView itemName, itemCode, price;
    EditText quantity;
    int salePrice;
    String Code;
    boolean dataError = false;
    JSONObject apiResponse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);
        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_item_action_bar);

        View view = getSupportActionBar().getCustomView();
        ImageView backArrow = view.findViewById(R.id.back_to_scanner);
        TextView next = view.findViewById(R.id.next_item);

        itemName = findViewById(R.id.Item_Name);
        itemCode = findViewById(R.id.Item_Code);
        quantity = findViewById(R.id.Quantity);
        price = findViewById(R.id.Price);


        Button toCart = findViewById(R.id.to_cart);
        i = getIntent();
        Code = i.getStringExtra("itemCode");
        Toast.makeText(this, Code, Toast.LENGTH_SHORT).show();

//        Api get request
        String url = "https://f9146531-91a6-40f0-b139-37f8a09449c7.mock.pstmn.io/items/getItem/" + Code;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                displayDetails(response);
                apiResponse = response;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dataError = true;
                Log.i("Error", error.toString());
                Toast.makeText(ItemDetailsActivity.this, "Cannot load item with code"+itemCode, Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);


//       Button click functions
        toCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!dataError)
                    saveItemData();
                Log.i("debug_log","Going to open the cart");
                startActivity(new Intent(ItemDetailsActivity.this, CartActivity.class));
            }
        });

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(ItemDetailsActivity.this, ScannerActivity.class);
                startActivity(intent2);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!dataError)
                    saveItemData();
                Intent intent3 = new Intent(ItemDetailsActivity.this, ScannerActivity.class);
                startActivity(intent3);
            }
        });

        quantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int endPrice;
                if(quantity.getText().toString() != null && quantity.getText().toString().length() != 0)
                    endPrice= Integer.parseInt(quantity.getText().toString()) * salePrice ;
                else
                    endPrice = 0;
                price.setText(String.valueOf(endPrice));
            }

            @Override
            public void afterTextChanged(Editable editable) {  }
        });
    }

    public void displayDetails(JSONObject res){
        try {
            itemName.setText(res.get("itemName").toString());
            itemCode.setText(res.get("itemEanCode").toString());
            salePrice = Integer.parseInt(res.get("itemSalePrice").toString());
            price.setText(String.valueOf(salePrice));

            } catch (JSONException e) { e.printStackTrace(); }
    }

    public void saveItemData(){
        int itemPrice = Integer.parseInt(price.getText().toString());
        int qty;
        if(quantity.getText().toString() != null && quantity.getText().toString().length() != 0)
            qty = Integer.parseInt(quantity.getText().toString());
        else
            qty = 0;

        String skuCode = null;
        int itemMrp= 0;
        try {
            skuCode = apiResponse.get("skuCode").toString();
            itemMrp = Integer.parseInt(apiResponse.get("itemMrp").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        CartItems cartItem = new CartItems(itemCode.getText().toString(),itemName.getText().toString(), skuCode,qty, itemPrice, itemMrp);
        List<CartItems> d = Global.getInstance().getDataList();
        boolean itemAdded = false;
        for(int i = 0; i< d.size(); i++) {
            if (d.get(i).getItemCode() != itemCode.getText().toString()) {
                d.get(i).quantity += qty;
                d.get(i).price = salePrice * d.get(i).getQuantity();
                Toast.makeText(this, "Item present in list", Toast.LENGTH_SHORT).show();
                itemAdded = true;
                break;
            }
        }
        if(itemAdded)
            Global.getInstance().setDataList(d);
        else
            Global.getInstance().dataList.add(cartItem);
    }

}