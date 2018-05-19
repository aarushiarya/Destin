package com.example.aarushiarya.tryfrag;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
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
import com.google.gson.Gson;

import org.json.JSONObject;

import static java.lang.Float.parseFloat;

public class Info extends Fragment {

    private TextView address, phone, price, google, website;
    private RatingBar rating;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.info,container,false);
        address = (TextView) rootView.findViewById(R.id.mAddress);
        phone = (TextView) rootView.findViewById(R.id.mPhone);
        price = (TextView) rootView.findViewById(R.id.mPrice);
        rating = rootView.findViewById(R.id.mRating);
        google = (TextView) rootView.findViewById(R.id.mGoogle);
        website = (TextView) rootView.findViewById(R.id.mWebsite);

        detailActivity activity = (detailActivity) getActivity();
        String url = activity.getDETAIL_URL();
        loadResponse(url);
        return rootView;
    }
    public void loadResponse(String url){
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Fetching results");
        progressDialog.show();

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        JsonObjectRequest StringRequest = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {

            public static final String TAG = "";

            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();

                try {
                    Log.d(TAG,"RESPONSE:"+response);
                    Gson gson = new Gson();

                    Detail detail = gson.fromJson(response.toString(),Detail.class);
                    Result resDetail = detail.getResult();

                    String add = resDetail.getFormatted_address();
                    String pho = resDetail.getFormatted_phone_number();
                    String rat = resDetail.getRating();
                    int price = resDetail.getPrice();
                    String goo = resDetail.getUrl();
                    String web = resDetail.getWebsite();
                    displayDetail(add,pho,rat,price,goo,web);
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

    public void displayDetail(String add, String pho, String rat, int pri, String goo, String web){
        if(add == null){
            address.setText("No address");
        }else {
            address.setText(add);
        }

        if(pho==null){
            phone.setText("No phone number");
        }else{
            phone.setText(pho);
        }

        String dol="";

        if(pri==0){
            price.setText("No price level");
        }else{
            while(pri>0){
                dol += "$";
                pri--;
            }
            price.setText(dol);
        }

        if(rat == null){
            rat="0";
        }
        rating.setRating(parseFloat(rat));

        if(goo == null){
            google.setText("No google page");
        }else{
            String linkedText = String.format("<a href=\"%s\">"+goo+"</a> ", goo);
            google.setText(Html.fromHtml(linkedText));
            google.setMovementMethod(LinkMovementMethod.getInstance());
        }

        if(web == null){
            website.setText("No website");
        }else {
            String webUrl = String.format("<a href=\"%s\">"+web+"</a> ", web);
            website.setText(Html.fromHtml(webUrl));
            website.setMovementMethod(LinkMovementMethod.getInstance());
        }

    }

}