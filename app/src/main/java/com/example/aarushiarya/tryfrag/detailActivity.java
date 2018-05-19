package com.example.aarushiarya.tryfrag;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.aarushiarya.tryfrag.Model.Detail;
import com.example.aarushiarya.tryfrag.Model.Result;
import com.example.aarushiarya.tryfrag.Model.Address_components;
import com.example.aarushiarya.tryfrag.Model.Location;
import com.google.gson.Gson;

import org.json.JSONObject;

import static java.lang.Double.parseDouble;
import static java.lang.Float.parseFloat;

public class detailActivity extends AppCompatActivity {
    private String DETAIL_URL="";
    private String ID = "";
    private Double lat,lng;
    private Address_components des[];
    public String nameP, addP, webP;
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private ListItem listItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        Bundle bundle = getIntent().getExtras();
        String name = bundle.getString("name");

        setTitle(name);

        String id = bundle.getString("placeId");
        ID = id;
        //http://webhw81-env.us-east-2.elasticbeanstalk.com/getDetailPage/

        String url = "https://maps.googleapis.com/maps/api/place/details/json?placeid="+ID+"&key=AIzaSyBsqqvSXgfdba5wWPx2YGTJvyWg4UUUsCM";
        DETAIL_URL = url;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        loadResponse(url);
    }
    public String getDETAIL_URL() {
        return DETAIL_URL;
    }
    public String getID(){
        return ID;
    }
    public Double getLat(){
        return lat;
    }

    public Double getLng(){
        return lng;
    }

    public Address_components[] getDes() {
        return des;
    }

    public String getNameP() {
        return nameP;
    }


    public void loadResponse(String url){

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest StringRequest = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {

            public static final String TAG = "";

            @Override
            public void onResponse(JSONObject response) {


                try {
                    Log.d(TAG,"RESPONSE:"+response);
                    Gson gson = new Gson();

                    Detail detail = gson.fromJson(response.toString(),Detail.class);
                    Result resDetail = detail.getResult();
                    String latitude = resDetail.getGeometry().getLocation().getLat();
                    lat = parseDouble(latitude);

                    String longitude = resDetail.getGeometry().getLocation().getLng();
                    lng = parseDouble(longitude);

                    des = resDetail.getAddress_components();
                    nameP = resDetail.getName();
                    addP = resDetail.getFormatted_address();
                    webP = resDetail.getWebsite();
                    listItem = new ListItem(ID,nameP,addP,resDetail.getIcon());
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
                Toast.makeText(detailActivity.this, error.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });

        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(StringRequest);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);


        MenuItem icon = menu.findItem(R.id.action_fav);


        if(MyStore.checkFav(getApplicationContext(),ID)){
            icon.setIcon(R.drawable.ic_white_filled_heart);
        }else{
            icon.setIcon(R.drawable.ic_white_blank_heart);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.action_fav){
            Toast.makeText(this,"fav",Toast.LENGTH_SHORT).show();
            if(item.getIcon().getConstantState() == getResources().getDrawable(R.drawable.ic_white_filled_heart).getConstantState()){
                Toast.makeText(this," was removed from favorites",Toast.LENGTH_SHORT).show();
                item.setIcon(R.drawable.ic_white_blank_heart);
                MyStore.removeFav(this,ID);
            }
            else {
                Toast.makeText(this," was added to favorites",Toast.LENGTH_SHORT).show();
                item.setIcon(R.drawable.ic_white_filled_heart);
                MyStore.addFav(this,listItem);
            }
        }
        else if(id == R.id.action_share){
            Toast.makeText(this,"share",Toast.LENGTH_SHORT).show();
            //open intent to twitter
            String url = "https://twitter.com/intent/tweet?text=Check out "+nameP+" located at "+addP+ " Website:"+webP;

            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    Info tab1 = new Info();
                    return tab1;
                case 1:
                    Photo tab2 = new Photo();
                    return tab2;
                case 2:
                    Map tab3 = new Map();
                    return tab3;
                case 3:
                    Review tab4 = new Review();
                    return tab4;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 4;
        }

        public CharSequence getPageTitle(int position){
            switch (position){
                case 0: return "INFO";
                case 1: return "PHOTOS";
                case 2: return "MAP";
                case 3: return "REVIEW";
            }
            return null;
        }
    }
}
