package com.example.xenologer;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private ImageButton button;
    private EditText searchBar;
    private CardView cardView;
    private RecyclerView recyclerView1, recyclerView2;
    private Recycler_Apod_Adapter adapterApod;
    private Recycler_Adapter adapterItem;
    private ArrayList<ItemDetails> itemList;
    private ArrayList<ApodDetails> apodList;
    private RequestQueue requestQueue1,requestQueue2;
    private int year,month,day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.dateBtn);
        searchBar = findViewById(R.id.searchBar);
        cardView = findViewById(R.id.apodCard);

        recyclerView1 = findViewById(R.id.apodRecycler);
        recyclerView2 = findViewById(R.id.itemsRecycler);

        cardView.setVisibility(View.GONE);

        itemList = new ArrayList<>();
        apodList = new ArrayList<>();

        recyclerView1.setHasFixedSize(true);
        recyclerView1.setLayoutManager(new LinearLayoutManager(this));

        recyclerView2.setHasFixedSize(true);
        recyclerView2.setLayoutManager(new GridLayoutManager(this, 2));

        requestQueue1 = Volley.newRequestQueue(MainActivity.this);
        requestQueue2 = Volley.newRequestQueue(MainActivity.this);

        searchBar.animate().translationY(-60f).setDuration(1000).start();

        jsonItemReq("Galaxy");

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                recyclerView2.setVisibility(View.VISIBLE);
                cardView.setVisibility(View.GONE);
                cardView.animate().translationY(-50f).setDuration(0).start();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                requestQueue1.cancelAll("REQ");
                jsonItemReq(searchBar.getText().toString());
            }
        });

        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DATE);

        final DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this,MainActivity.this,year,month,day);

        button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });

    }

    public void jsonItemReq(String search){

        if(itemList.size()>0){
            itemList.clear();
        }

        String itemUrl = "https://images-api.nasa.gov/search?q=" + search;

        JsonObjectRequest jsonObjectRequest2 = new JsonObjectRequest(Request.Method.GET, itemUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject jsonObject = response.getJSONObject("collection");
                    JSONArray jsonArray = jsonObject.getJSONArray("items");

                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject datas = jsonArray.getJSONObject(i);

                        JSONArray item = datas.getJSONArray("data");
                        JSONArray link = datas.getJSONArray("links");

                        JSONObject image = link.getJSONObject(0);
                        String imageUrl = image.getString("href");

                        JSONObject jsonObject1 = item.getJSONObject(0);
                        String nasaTitle = jsonObject1.getString("title");
                        String nasaId = jsonObject1.getString("nasa_id");

                        itemList.add(new ItemDetails("TITLE : "+nasaTitle,imageUrl,nasaId));
                    }
                    adapterItem = new Recycler_Adapter(itemList,MainActivity.this,MainActivity.this);
                    recyclerView2.setAdapter(adapterItem);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        jsonObjectRequest2.setTag("REQ");
        requestQueue1.add(jsonObjectRequest2);
    }

    public void jsonReq(final int yyyy, int mm, int dd) {

        if(apodList.size()>0){
            apodList.clear();
        }

        final String month, day;

        if (mm < 10) {
            month = "0" + mm;
        } else {
            month = Integer.toString(mm);
        }

        if(dd < 10){
            day = "0" + dd;
        }else {
            day = Integer.toString(dd);
        }

        String apodUrl = "https://api.nasa.gov/planetary/apod?api_key=DEMO_KEY&date="+ yyyy +"-"+ month +"-"+ day;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, apodUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String date = response.getString("date");
                    String desc = response.getString("explanation");
                    String image = response.getString("url");

                    String type = response.getString("media_type");
                    if (type.equals("image")) {
                        apodList.add(new ApodDetails("APOD : "+date, image, desc));
                        adapterApod = new Recycler_Apod_Adapter(apodList, MainActivity.this);
                    } else {
                        apodList.add(new ApodDetails("APOD : "+date, "image", desc));
                        adapterApod = new Recycler_Apod_Adapter(apodList, MainActivity.this);
                    }
                    recyclerView1.setAdapter(adapterApod);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                apodList.add(new ApodDetails("APOD : "+ yyyy +"-"+ month +"-"+ day, "image","Data Unavailable !"));
                adapterApod = new Recycler_Apod_Adapter(apodList, MainActivity.this);
                recyclerView1.setAdapter(adapterApod);
            }
        });
        requestQueue2.add(jsonObjectRequest);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        searchBar.getText().clear();
        cardView.setVisibility(View.VISIBLE);
        recyclerView2.setVisibility(View.GONE);
        cardView.animate().translationY(50f).setDuration(1000).start();
        jsonReq(year, month+1, dayOfMonth);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}