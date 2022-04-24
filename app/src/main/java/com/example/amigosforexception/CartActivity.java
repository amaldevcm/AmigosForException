package com.example.amigosforexception;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {
    RecyclerView cartView;
    Button proceed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_item_action_bar);

        View v = getSupportActionBar().getCustomView();
        TextView next = v.findViewById(R.id.next_item);
        ImageView backArrow = v.findViewById(R.id.back_to_scanner);
        proceed = findViewById(R.id.proceed_to_pay);
//
//        Recycler view
        cartView = findViewById(R.id.cartRecyclerView);
        LinearLayoutManager l1 = new LinearLayoutManager(this.getApplicationContext(), RecyclerView.VERTICAL, false);


        List<CartItems> data = Global.getInstance().getDataList();
        CartListAdapter adapter = new CartListAdapter(data, this);
        adapter.notifyDataSetChanged();
        cartView.setAdapter(adapter);
        cartView.setLayoutManager(l1);

//        Back button script
        next.setVisibility(View.INVISIBLE);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CartActivity.this, ScannerActivity.class);
                startActivity(intent);
            }
        });

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postData(data);
            }
        });

    }

    public void postData(List<CartItems> data){
//        Toast.makeText(this, "Posting data", Toast.LENGTH_SHORT).show();
        JSONObject customer = new JSONObject();
        try {
            customer.put("name", Global.getInstance().customerName);
            customer.put("mobile", Global.getInstance().customerMobile);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONArray products = new JSONArray();
        JSONObject item = new JSONObject();
        try {
            for (int i = 0; i < data.size(); i++) {
                item.put("skuCode", data.get(i).getSkuCode());
                item.put("itemName", data.get(i).getItemName());
                item.put("itemMrp", data.get(i).getItemMrp());
                item.put("itemSalePrice", data.get(i).getPrice());
                item.put("quantity", data.get(i).getQuantity());
                item.put("itemEanCode", data.get(i).getItemCode());

                products.put(item);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

        JSONObject apiPostData = new JSONObject();
        try {
            apiPostData.put("customer", customer);
            apiPostData.put("products", products);
        }catch (JSONException e){
            e.printStackTrace();
        }

        RequestQueue queue = Volley.newRequestQueue(this);
        String postUrl = "https://f9146531-91a6-40f0-b139-37f8a09449c7.mock.pstmn.io/items/postBill";
//        Log.i("Post_data", apiPostData.toString());
        JsonObjectRequest postRequest = new JsonObjectRequest(postUrl, apiPostData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
//                Toast.makeText(CartActivity.this, "Data posted", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(CartActivity.this, QRGenerator.class);
                try {
                    intent.putExtra("postBillId", response.get("postedBillId").toString());
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CartActivity.this, "Error posting data to server", Toast.LENGTH_SHORT).show();
                Log.i("post_error", error.toString());
            }
        });
        queue.add(postRequest);
    }
}