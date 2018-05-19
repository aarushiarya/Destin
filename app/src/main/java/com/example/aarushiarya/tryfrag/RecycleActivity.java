package com.example.aarushiarya.tryfrag;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.aarushiarya.tryfrag.Model.MyPlaces;
import com.example.aarushiarya.tryfrag.Model.Results;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RecycleActivity extends AppCompatActivity {
   private static String URL_DATA =  "";
   private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private Context mContext;
    Button btnNext,btnPrev;
    private TextView err;
    private String next;
    private List<ListItem> listItems;
    private List<ListItem> listItems2;
    private int i =0;
    private MyPlaces[] places = new MyPlaces[3];

    //private String URL_DATA="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Search results");
        Bundle bundle = getIntent().getExtras();
        if(bundle.getString("activity").matches("main")){
            URL_DATA = bundle.getString("stuff");
        }


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle);
        //Bundle bundle = getIntent().getExtras();

        btnNext = findViewById(R.id.next);
        btnPrev = findViewById(R.id.prev);
        btnPrev.setClickable(false);
        btnPrev.setEnabled(false);
        //Extract the data…
        //URL_DATA = bundle.getString("request");
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        err = findViewById(R.id.err);

        listItems = new ArrayList<>();
        loadRecyclerViewData();
    }

    private void loadRecyclerViewData() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Fetching results");
        progressDialog.show();
        mContext = getApplicationContext();
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        // Initialize a new JsonArrayRequest instance

        JsonObjectRequest StringRequest = new JsonObjectRequest(Request.Method.GET, URL_DATA, new Response.Listener<JSONObject>() {

            public static final String TAG = "";

            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();

                try {
                    //Log.d(TAG,"RESPONSE:"+response);
                    Gson gson = new Gson();
                    places[i] = gson.fromJson(response.toString(),MyPlaces.class);
                    if(places[i].getStatus().matches("ZERO_RESULTS")){
                        //Toast.makeText(RecycleActivity.this,"No results",Toast.LENGTH_SHORT).show();
                        btnNext.setClickable(false);
                        btnNext.setEnabled(false);
                        err.setVisibility(View.VISIBLE);
                    }else {
                        Results[] res = places[i].getResults();
                        next = places[i].getNext_page_token();
                        if(next==null){
                            btnNext.setClickable(false);
                            btnNext.setEnabled(false);
                        }
                        //clickNext(next);
                        //JSONObject jsonObject = new JSONObject(response);
                        //JSONArray array = response;

                        for(int j=0; j<res.length;j++){
                            ListItem item = new ListItem(res[j].getPlace_id(),
                                    res[j].getName(),
                                    res[j].getVicinity(),
                                    res[j].getIcon());
                            listItems.add(item);
                        }

                        adapter = new MyAdapter(listItems,getApplicationContext());
                        recyclerView.setAdapter(adapter);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            public static final String TAG = "";
            //progressDialog.dismiss();

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO: Handle error
                Log.d(TAG,"ERROR");
                Toast.makeText(getApplicationContext(), error.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });

        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(StringRequest);


    }

    public void clickPrev(View view){
        --i;

        listItems2 = new ArrayList<>();
        Results[] res = places[i].getResults();
        next = places[i].getNext_page_token();
        if(next==null){
            btnNext.setClickable(false);
            btnNext.setEnabled(false);
        }
        else {
            btnNext.setEnabled(true);
            btnNext.setClickable(true);
        }
        if(i==0){
            btnPrev.setClickable(false);
            btnPrev.setEnabled(false);
        }else {
            btnPrev.setClickable(true);
            btnPrev.setEnabled(true);
        }

        for(int j=0; j<res.length;j++){
            //JSONObject o = res.g
            ListItem item = new ListItem(res[j].getPlace_id(),
                    res[j].getName(),
                    res[j].getVicinity(),
                    res[j].getIcon());
            listItems2.add(item);
        }

        adapter = new MyAdapter(listItems2,getApplicationContext());
        recyclerView.setAdapter(adapter);
    }

    public void clickNext(View view){
        ++i;
        //Toast.makeText(this,"next:"+next,Toast.LENGTH_SHORT).show();
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Fetching results");
        progressDialog.show();
        mContext = getApplicationContext();
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        String url="https://maps.googleapis.com/maps/api/place/nearbysearch/json?pagetoken="+next+"&key=AIzaSyBsqqvSXgfdba5wWPx2YGTJvyWg4UUUsCM";

        // Initialize a new JsonArrayRequest instance

        JsonObjectRequest StringRequest = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {

            public static final String TAG = "";

            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();

                try {
                    listItems2 = new ArrayList<>();
                    //Log.d(TAG,"RESPONSE:"+response);
                    Gson gson = new Gson();
                    places[i] = gson.fromJson(response.toString(),MyPlaces.class);
                    Results[] res = places[i].getResults();
                    next = places[i].getNext_page_token();
                    if(next==null){
                        btnNext.setClickable(false);
                        btnNext.setEnabled(false);
                    }else {
                        btnNext.setEnabled(true);
                        btnNext.setClickable(true);
                    }
                    if(i==0){
                        btnPrev.setClickable(false);
                        btnPrev.setEnabled(false);
                    }else {
                        btnPrev.setClickable(true);
                        btnPrev.setEnabled(true);
                    }
                    //clickNext(next);
                    //JSONObject jsonObject = new JSONObject(response);
                    //JSONArray array = response;

                    for(int j=0; j<res.length;j++){
                        //JSONObject o = res.g
                        ListItem item = new ListItem(res[j].getPlace_id(),
                                res[j].getName(),
                                res[j].getVicinity(),
                                res[j].getIcon());
                        listItems2.add(item);
                    }

                    adapter = new MyAdapter(listItems2,getApplicationContext());
                    recyclerView.setAdapter(adapter);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            public static final String TAG = "";
            //progressDialog.dismiss();

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO: Handle error
                Log.d(TAG,"ERROR");
                Toast.makeText(getApplicationContext(), error.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });

        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(StringRequest);
    }
}
