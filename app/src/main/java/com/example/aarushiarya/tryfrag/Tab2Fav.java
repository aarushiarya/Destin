package com.example.aarushiarya.tryfrag;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Map;

public class Tab2Fav extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private TextView errFav;
    private ArrayList<ListItem> mFavList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup continer,
                             Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.tab2fav,continer,false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerFav);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        errFav = rootView.findViewById(R.id.errFav);

        Map<String,?> mFav =  MyStore.getAllFav(getContext());
        mFavList = new ArrayList<>();

        for(Map.Entry<String,?> entry: mFav.entrySet() ){
            mFavList.add(MyStore.getFav(getContext(),entry.getKey()));
        }

        if(mFavList.size()==0){
            errFav.setVisibility(View.VISIBLE);
        }else{
            adapter = new MyAdapterFav(mFavList,getContext());
            recyclerView.setAdapter(adapter);
        }
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        Map<String,?> mFav =  MyStore.getAllFav(getContext());
        mFavList = new ArrayList<>();

        for(Map.Entry<String,?> entry: mFav.entrySet() ){

            mFavList.add(MyStore.getFav(getContext(),entry.getKey()));
        }
        adapter = new MyAdapterFav(mFavList, getContext());
        recyclerView.setAdapter(adapter);
    }
}
