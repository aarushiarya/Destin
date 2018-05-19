package com.example.aarushiarya.tryfrag;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.aarushiarya.tryfrag.Model.Address_components;
import com.example.aarushiarya.tryfrag.Model.Detail;
import com.example.aarushiarya.tryfrag.Model.Result;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.maps.android.PolyUtil;
import org.json.JSONObject;

import com.example.aarushiarya.tryfrag.Model.Routes;
import com.example.aarushiarya.tryfrag.Model.Steps;
import com.example.aarushiarya.tryfrag.Model.Legs;
import com.example.aarushiarya.tryfrag.Model.Direction;

import static java.lang.Double.parseDouble;

public class Map extends Fragment {

    private static final String TAG = "";
    public GoogleMap mMap;
    public MapView mMapView;
    private Double lat, lng;
    //private Double latOrigin,lngOrigin;
    public Address_components des[];
    String destination = "";
    String desTitle = "Destination";
    //String oriTitle = "Origin";
    String mode = "";
    String origin = "";

    protected GeoDataClient mGeoDataClient;

    private PlaceAutocompleteAdapter mAdapter;

    private AutoCompleteTextView mAutocompleteViewMap;

    private static final LatLngBounds BOUNDS_GREATER_SYDNEY = new LatLngBounds(
            new LatLng(-34.041458, 150.790100), new LatLng(-33.682247, 151.383362));


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.map, container, false);
        detailActivity activity = (detailActivity) getActivity();
        lat = activity.getLat();
        lng = activity.getLng();
        des = activity.getDes();
        desTitle = activity.nameP;
        if (des == null) {

        } else {
            for (int i = 0; i < des.length; i++) {
                destination += " " + des[i].getShort_name();
            }
        }

        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                mMap.getUiSettings().setMapToolbarEnabled(false);
                // Add a marker in Sydney and move the camera
                LatLng sydney = new LatLng(lat, lng);
                mMap.addMarker(new MarkerOptions().position(sydney).title(desTitle));
                //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 10));
            }
        });

        String[] values = {"Driving", "Walking", "Bicycling", "Transit"};
        Spinner spinner = (Spinner) rootView.findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, values);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        mode = "driving";
                        if (mAutocompleteViewMap.getText().toString().equals("")) {
                            //do nothing
                        } else {
                            displayRoutes(view);
                        }
                        break;
                    case 1:
                        mode = "walking";
                        displayRoutes(view);
                        break;
                    case 2:
                        mode = "bicycling";
                        displayRoutes(view);
                        break;
                    case 3:
                        mode = "transit";
                        displayRoutes(view);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mGeoDataClient = Places.getGeoDataClient(getActivity(), null);
        mAutocompleteViewMap = (AutoCompleteTextView) rootView.findViewById(R.id.autocomplete_map);
        mAutocompleteViewMap.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final AutocompletePrediction item = mAdapter.getItem(position);
                final String placeId = item.getPlaceId();
                String urlMap = "https://maps.googleapis.com/maps/api/place/details/json?placeid=" + placeId + "&key=AIzaSyBsqqvSXgfdba5wWPx2YGTJvyWg4UUUsCM";
                final CharSequence primaryText = item.getPrimaryText(null);
                //oriTitle = primaryText.toString();
                displayRoutes(view);

            }
        });
        mAdapter = new PlaceAutocompleteAdapter(getContext(), mGeoDataClient, BOUNDS_GREATER_SYDNEY, null);
        mAutocompleteViewMap.setAdapter(mAdapter);
        mAutocompleteViewMap.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                displayRoutes(view);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return rootView;
    }

    public void displayRoutes(View view) {
        mMap.clear();
        LatLng sydney = new LatLng(lat, lng);
        mMap.addMarker(new MarkerOptions().position(sydney).title(desTitle));
        origin = mAutocompleteViewMap.getText().toString();
        //mMap.addMarker(new MarkerOptions().position(origin).title("Origin"));
        String url = "https://maps.googleapis.com/maps/api/directions/json?origin=" + origin + "&destination=" + destination + "&mode=" + mode + "&key=AIzaSyBsqqvSXgfdba5wWPx2YGTJvyWg4UUUsCM";

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JsonObjectRequest StringRequest = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {

            public static final String TAG = "";

            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d(TAG, "RESPONSE:" + response);
                    Gson gson = new Gson();
                    Direction path = gson.fromJson(response.toString(), Direction.class);
                    if (path.getStatus().matches("ZERO_RESULTS")) {
                        Toast.makeText(getContext(), "No path found", Toast.LENGTH_SHORT);
                    } else {
                        Routes routes = path.getRoutes()[0];
                        Legs legs = routes.getLegs()[0];
                        Steps[] steps = legs.getSteps();
                        String[] polylines = new String[steps.length];
                        for (int i = 0; i < steps.length; i++) {
                            polylines[i] = steps[i].getPolyline().getPoints();
                        }
                        display(polylines);
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
                Log.d(TAG, "ERROR");
                //Toast.makeText(MapsActivity.this, error.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });

        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(StringRequest);
    }

    public void display(String[] directionsList) {
        int count = directionsList.length;
        for (int i = 0; i < count; i++) {
            PolylineOptions options = new PolylineOptions();
            options.color(Color.RED);
            options.width(10);
            options.addAll(PolyUtil.decode(directionsList[i]));

            mMap.addPolyline(options);
        }
    }
}

