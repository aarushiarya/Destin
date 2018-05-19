package com.example.aarushiarya.tryfrag;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.aarushiarya.tryfrag.Model.Address_components;
import com.example.aarushiarya.tryfrag.Model.Detail;
import com.example.aarushiarya.tryfrag.Model.ListItemYelp;
import com.example.aarushiarya.tryfrag.Model.Match;
import com.example.aarushiarya.tryfrag.Model.MyAdapterYelp;
import com.example.aarushiarya.tryfrag.Model.MyBusi;
import com.example.aarushiarya.tryfrag.Model.MyYelp;
import com.example.aarushiarya.tryfrag.Model.Result;
import com.example.aarushiarya.tryfrag.Model.Reviews;
import com.example.aarushiarya.tryfrag.Model.YelpReviews;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import static java.lang.Integer.parseInt;

public class Review extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<ListItem2> listItems;
    private List<ListItem2> listItemsDe;
    private List<ListItemYelp> listItemsYelpDe;
    private List<ListItemYelp> listItemsYelp;
    private Context mContext;
    private static String URL_DATA = "";
    public Address_components des[];
    public String destination="" , coun="", city = "", state="";
    public String name="";
    public String yelpId="";
    int s1=0, s2=0;
    private TextView errRev;
            //"https://maps.googleapis.com/maps/api/place/details/json?placeid=ChIJa147K9HX3IAR-lwiGIQv9i4&key=AIzaSyBsqqvSXgfdba5wWPx2YGTJvyWg4UUUsCM";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.review,container,false);
        String[] values = {"Google Reviews", "Yelp Reviews"};
        Spinner spinner = (Spinner) rootView.findViewById(R.id.spinner2);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, values);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);

        String[] values3 = {"Default order", "Highest Ratings", "Lowest Ratings", "Most Recent", "Least Recent"};
        Spinner spinner3 = (Spinner) rootView.findViewById(R.id.spinner3);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, values3);
        adapter3.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner3.setAdapter(adapter3);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.listRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        errRev = rootView.findViewById(R.id.errRev);

        listItems = new ArrayList<>();
        listItemsYelp = new ArrayList<>();
        listItemsDe = new ArrayList<>();
        listItemsYelpDe = new ArrayList<>();
        destination="";
        state="";
        coun="";
        city="";
        detailActivity activity = (detailActivity) getActivity();
        URL_DATA = activity.getDETAIL_URL();
        des = activity.getDes();
        for(int i=0; i<des.length;i++){
            if(des[i].getTypes()[0].matches("street_number")){
                destination += " "+des[i].getShort_name();
            }
            if(des[i].getTypes()[0].matches("route")){
                destination += " "+des[i].getShort_name();
            }
            if(des[i].getTypes()[0].matches("neighborhood")){
                destination += " "+des[i].getShort_name();
            }
            if(des[i].getTypes()[0].matches("administrative_area_level_2")){
                city += des[i].getShort_name();
            }
            if(des[i].getTypes()[0].matches("administrative_area_level_1")){
                state += des[i].getShort_name();
            }
            if(des[i].getTypes()[0].matches("country")){
                coun += des[i].getShort_name();
            }
        }
        name=activity.getNameP();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        s1=0;
                        decide();
                        loadRecyclerViewData();
                        //Toast.makeText(getContext(), "google", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        s1=1;
                        decide();
                        loadYelp();
                        //Toast.makeText(getContext(), "yelp", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onNothingSelected (AdapterView < ? > parent){

            }
        });

        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0: s2=0;
                        decide();
                        //Toast.makeText(getContext(), "default", Toast.LENGTH_SHORT).show();
                        break;
                    case 1: s2=1;
                        decide();
                        //loadHighest();
                        //Toast.makeText(getContext(), "Highest", Toast.LENGTH_SHORT).show();
                        break;
                    case 2: s2=2;
                        decide();
                        //loadLowest();
                        //Toast.makeText(getContext(), "Lowest", Toast.LENGTH_SHORT).show();
                        break;
                    case 3: s2=3;
                        decide();
                        //loadMost();
                        //Toast.makeText(getContext(), "Most", Toast.LENGTH_SHORT).show();
                        break;
                    case 4: s2=4;
                        decide();
                        //loadLeast();
                        //Toast.makeText(getContext(), "Least", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
            @Override
            public void onNothingSelected (AdapterView < ? > parent){

            }
        });
        return rootView;
    }

    public void decide(){

        if(s1==0 && s2==0){
            //Toast.makeText(getContext(), "G default", Toast.LENGTH_SHORT).show();
            loadDefault();
        } else if(s1==0 && s2==1){
            //Toast.makeText(getContext(), "g Highest", Toast.LENGTH_SHORT).show();
            loadHighest();
        }else if(s1==0 && s2==2){
            //Toast.makeText(getContext(), "g Lowest", Toast.LENGTH_SHORT).show();
            loadLowest();
        }else if(s1==0 && s2==3){
            //Toast.makeText(getContext(), "g Most", Toast.LENGTH_SHORT).show();
            loadMost();
        }else if(s1==0 && s2==4){
            //Toast.makeText(getContext(), "g Least", Toast.LENGTH_SHORT).show();
            loadLeast();
        }else if(s1==1 && s2==0){
            //Toast.makeText(getContext(), "y de", Toast.LENGTH_SHORT).show();
            loadYelpDefault();
        }else if(s1==1 && s2==1){
            //Toast.makeText(getContext(), "y Highest", Toast.LENGTH_SHORT).show();
            loadYelpHighest();
        }else if(s1==1 && s2==2){
            //Toast.makeText(getContext(), "y lo", Toast.LENGTH_SHORT).show();
            loadYelpLowest();
        }else if(s1==1 && s2==3){
            //Toast.makeText(getContext(), "y mo", Toast.LENGTH_SHORT).show();
            loadYelpMost();
        }
        else if(s1==1 && s2==4){
            //Toast.makeText(getContext(), "y least", Toast.LENGTH_SHORT).show();
            loadYelpLeast();
        }
    }
    public void loadYelp(){
        String url = "http://webhw81-env.us-east-2.elasticbeanstalk.com/yelpMatch/"+name+"/"+destination+"/"+city+
                "/"+state+"/"+coun;
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Fetching results");
        progressDialog.show();
        mContext = getActivity().getApplicationContext();
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        // Initialize a new JsonArrayRequest instance

        JsonObjectRequest StringRequest = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {

            public static final String TAG = "";

            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();

                try {
                    Gson gson = new Gson();
                    Match m = gson.fromJson(response.toString(),Match.class);
                    MyBusi[] busi = m.getBusinesses();
                    if(busi.length == 0){
                        errRev.setVisibility(View.VISIBLE);
                    }
                    else{
                        yelpId = busi[0].getId();
                        yelpReview(yelpId);
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
                Toast.makeText(getActivity().getApplicationContext(), error.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });

        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(StringRequest);


    }

    private void yelpReview(String id){
        mContext = getActivity().getApplicationContext();
        //listItemsYelp = new ArrayList<ListItem2>();
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        String url = "http://webhw81-env.us-east-2.elasticbeanstalk.com/yelpReview/"+id;
        JsonObjectRequest StringRequest = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {

            public static final String TAG = "";

            @Override
            public void onResponse(JSONObject response) {

                try {
                    Gson gson = new Gson();
                    MyYelp m = gson.fromJson(response.toString(), MyYelp.class);
                    YelpReviews[] mRev = m.getReviews();
                    for (int j = 0; j<3; j++) {
                        ListItemYelp item = new ListItemYelp(mRev[j].getUrl(),
                                mRev[j].getUser().getName(),
                                mRev[j].getText(),
                                mRev[j].getUser().getImage_url(),
                                mRev[j].getRating(),
                                mRev[j].getTime_created());
                        listItemsYelp.add(item);
                    }
                    adapter = new MyAdapterYelp(getView(), getActivity().getApplicationContext(), listItemsYelp);
                    recyclerView.setAdapter(adapter);
                    listItemsYelpDe = listItemsYelp;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            public static final String TAG = "";

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO: Handle error
                Log.d(TAG,"ERROR");
                Toast.makeText(getActivity().getApplicationContext(), error.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });

        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(StringRequest);
    }

    public void loadDefault(){
        adapter = new MyAdapter2(getView(),getActivity().getApplicationContext(),listItemsDe);
        recyclerView.setAdapter(adapter);
    }

    private void loadHighest(){
        //sort ArrayList
        Collections.sort(listItems, new Comparator<ListItem2>() {
            @Override
            public int compare(ListItem2 o1, ListItem2 o2) {
                return Integer.valueOf(parseInt(o2.getRating())).compareTo(parseInt(o1.getRating()));
            }
        });

        adapter = new MyAdapter2(getView(),getActivity().getApplicationContext(),listItems);
        recyclerView.setAdapter(adapter);
    }

    private void loadLowest(){
        //sort ArrayList
        Collections.sort(listItems, new Comparator<ListItem2>() {
            @Override
            public int compare(ListItem2 o1, ListItem2 o2) {
                return Integer.valueOf(parseInt(o1.getRating())).compareTo(parseInt(o2.getRating()));
            }
        });

        adapter = new MyAdapter2(getView(),getActivity().getApplicationContext(),listItems);
        recyclerView.setAdapter(adapter);
    }

    private void loadMost(){
        //sort ArrayList
        Collections.sort(listItems, new Comparator<ListItem2>() {
            @Override
            public int compare(ListItem2 o1, ListItem2 o2) {
                return Integer.valueOf(parseInt(o2.getTime())).compareTo(parseInt(o1.getTime()));
            }
        });

        adapter = new MyAdapter2(getView(),getActivity().getApplicationContext(),listItems);
        recyclerView.setAdapter(adapter);
    }

    private void loadLeast(){
        //sort ArrayList
        Collections.sort(listItems, new Comparator<ListItem2>() {
            @Override
            public int compare(ListItem2 o1, ListItem2 o2) {
                return Integer.valueOf(parseInt(o1.getTime())).compareTo(parseInt(o2.getTime()));
            }
        });

        adapter = new MyAdapter2(getView(),getActivity().getApplicationContext(),listItems);
        recyclerView.setAdapter(adapter);
    }

    private void loadYelpDefault(){
        adapter = new MyAdapterYelp(getView(),getActivity().getApplicationContext(),listItemsYelpDe);
        recyclerView.setAdapter(adapter);
    }

    private void loadYelpHighest(){
        //sort ArrayList
        Collections.sort(listItemsYelp, new Comparator<ListItemYelp>() {
            @Override
            public int compare(ListItemYelp o1, ListItemYelp o2) {
                return Integer.valueOf(parseInt(o2.getRating())).compareTo(parseInt(o1.getRating()));
            }
        });

        adapter = new MyAdapterYelp(getView(),getActivity().getApplicationContext(),listItemsYelp);
        recyclerView.setAdapter(adapter);
    }

    private void loadYelpLowest(){
        //sort ArrayList
        Collections.sort(listItemsYelp, new Comparator<ListItemYelp>() {
            @Override
            public int compare(ListItemYelp o1, ListItemYelp o2) {
                return Integer.valueOf(parseInt(o1.getRating())).compareTo(parseInt(o2.getRating()));
            }
        });

        adapter = new MyAdapterYelp(getView(),getActivity().getApplicationContext(),listItemsYelp);
        recyclerView.setAdapter(adapter);
    }

    private void loadYelpMost(){
        //sort ArrayList

        Collections.sort(listItemsYelp, new Comparator<ListItemYelp>() {
            @Override
            public int compare(ListItemYelp o1, ListItemYelp o2) {
                return o2.getTime().compareTo(o1.getTime());
            }
        });

        adapter = new MyAdapterYelp(getView(),getActivity().getApplicationContext(),listItemsYelp);
        recyclerView.setAdapter(adapter);
    }

    private void loadYelpLeast(){
        //sort ArrayList
        Collections.sort(listItemsYelp, new Comparator<ListItemYelp>() {
            @Override
            public int compare(ListItemYelp o1, ListItemYelp o2) {
                return o1.getTime().compareTo(o2.getTime());
            }
        });

        adapter = new MyAdapterYelp(getView(),getActivity().getApplicationContext(),listItemsYelp);
        recyclerView.setAdapter(adapter);
    }
    private void loadRecyclerViewData() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Fetching results");
        progressDialog.show();

        mContext = getActivity().getApplicationContext();
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        // Initialize a new JsonArrayRequest instance

        JsonObjectRequest StringRequest = new JsonObjectRequest(Request.Method.GET, URL_DATA, new Response.Listener<JSONObject>() {

            public static final String TAG = "";

            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();

                try {
                    Gson gson = new Gson();
                    Detail detail = gson.fromJson(response.toString(),Detail.class);
                    Result res = detail.getResult();
                    Reviews[] rev = res.getReviews();

                    for(int i=0; i<rev.length;i++){
                        ListItem2 item = new ListItem2( rev[i].getAuthor_url(),
                                rev[i].getAuthor_name(),
                                rev[i].getText(),
                                rev[i].getProfile_photo_url(),
                                rev[i].getRating(),
                                rev[i].getTime());
                        listItems.add(item);
                    }

                    adapter = new MyAdapter2(getView(),getActivity().getApplicationContext(),listItems);
                    recyclerView.setAdapter(adapter);
                    listItemsDe = listItems;
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
                Toast.makeText(getActivity().getApplicationContext(), error.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });

        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(StringRequest);


    }
}