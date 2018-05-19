package com.example.aarushiarya.tryfrag;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResponse;
import com.google.android.gms.location.places.PlacePhotoResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class Photo extends Fragment {
    protected GeoDataClient mGeoDataClient;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<listItemPhoto> listItems;
    private TextView errPhoto;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.photos,container,false);
        mGeoDataClient = Places.getGeoDataClient(getActivity(), null);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerViewPhoto);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        listItems = new ArrayList<>();
        errPhoto = rootView.findViewById(R.id.errPhoto);

        detailActivity activity = (detailActivity) getActivity();
        String placeId= activity.getID();
        loadPhotos(placeId);

        return rootView;
    }
    // Request photos and metadata for the specified place.
    private void loadPhotos(String pID) {
        final String placeId = pID;
        final Task<PlacePhotoMetadataResponse> photoMetadataResponse = mGeoDataClient.getPlacePhotos(placeId);
        photoMetadataResponse.addOnCompleteListener(new OnCompleteListener<PlacePhotoMetadataResponse>() {
            @Override
            public void onComplete(@NonNull Task<PlacePhotoMetadataResponse> task) {
                try {
                    // Get the list of photos.
                    PlacePhotoMetadataResponse photos = task.getResult();
                    // Get the PlacePhotoMetadataBuffer (metadata for all of the photos).
                    PlacePhotoMetadataBuffer photoMetadataBuffer = photos.getPhotoMetadata();
                    // Get the first photo in the list.
                    for (int i = 0; i < photoMetadataBuffer.getCount(); i++) {
                        PlacePhotoMetadata photoMetadata = photoMetadataBuffer.get(i);
                        // Get the attribution text.
                        CharSequence attribution = photoMetadata.getAttributions();
                        // Get a full-size bitmap for the photo.
                        Task<PlacePhotoResponse> photoResponse = mGeoDataClient.getPhoto(photoMetadata);
                        photoResponse.addOnCompleteListener(new OnCompleteListener<PlacePhotoResponse>() {
                            @Override
                            public void onComplete(@NonNull Task<PlacePhotoResponse> task) {
                                PlacePhotoResponse photo = task.getResult();
                                Bitmap bitmap = photo.getBitmap();
                                //imageView.setImageBitmap(bitmap);
                                adapter = new MyAdapterPhoto(getView(),getContext(),listItems);
                                recyclerView.setAdapter(adapter);
                                listItemPhoto item = new listItemPhoto(bitmap);
                                listItems.add(item);
                            }
                        });
                    }
                    photoMetadataBuffer.release();
                } catch (Exception e) {
                    e.printStackTrace();
                    errPhoto.setVisibility(View.VISIBLE);
                    //Toast.makeText(getContext(),"Network error. Please wait.",Toast.LENGTH_SHORT).show();
                }

                //adapter = new MyAdapterPhoto(getView(),getActivity().getApplicationContext(),listItems);
                //recyclerView.setAdapter(adapter);
            }
        });

    }
}