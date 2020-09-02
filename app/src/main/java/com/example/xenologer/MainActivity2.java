package com.example.xenologer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity {

    private ImageView imageView;
    private TextView textView1, textView2;
    private String itemName,itemId,itemImage;
    private RequestQueue requestQueue;
    private boolean jpgFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        imageView = findViewById(R.id.resultImage1);
        textView1 = findViewById(R.id.resultName2);
        textView2 = findViewById(R.id.resultText2);

        requestQueue = Volley.newRequestQueue(MainActivity2.this);

        Intent intent = getIntent();

        itemName = intent.getStringExtra("name");
        itemId = intent.getStringExtra("url");

        jsonAssetReq(itemId);

        textView1.setText(itemName);
        textView2.setText("NASA ID : "+itemId);

    }


    public void jsonAssetReq(final String nasaId) {

        String assetUrl = "https://images-api.nasa.gov/asset/" + nasaId;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, assetUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject jsonObject = response.getJSONObject("collection");

                    JSONArray jsonArray = jsonObject.getJSONArray("items");

                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);

                    itemImage = jsonObject1.getString("href");

                    setImage(itemImage);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

    public void setImage(String imageUrl){
        String[] format = imageUrl.split(".");

        for(String a : format){
            if (a.equals("JPG")) {
                jpgFormat = true;
                break;
            }
        }

        if(jpgFormat){
            Glide.with(MainActivity2.this)
                    .load("https://images-assets.nasa.gov/image/"+itemId+"/"+itemId+"~orig.JPG")
                    .circleCrop()
                    .placeholder(R.drawable.ic_nasa_logo)
                    .into(imageView);
        }
        else {
            Glide.with(MainActivity2.this)
                    .load("https://images-assets.nasa.gov/image/"+itemId+"/"+itemId+"~orig.jpg")
                    .circleCrop()
                    .placeholder(R.drawable.ic_nasa_logo)
                    .into(imageView);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}