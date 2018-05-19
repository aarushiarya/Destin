package com.example.aarushiarya.tryfrag;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Map;

public class MyStore {
    final static String file = "favorite";
    public static void addFav(Context context, ListItem listItem){
        SharedPreferences sharedPreferences = context.getSharedPreferences(file,0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(listItem);
        editor.putString(listItem.getPlace_id(),json);
        editor.apply();
    }

    public static Map<String,?> getAllFav(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(file,0);
        return sharedPreferences.getAll();
    }

    public static ListItem getFav(Context context,String id){
        SharedPreferences sharedPreferences = context.getSharedPreferences(file,0);
        Gson gson = new Gson();
        return gson.fromJson(sharedPreferences.getString(id,""),ListItem.class);

    }

    public static void removeFav(Context context, String id){
        SharedPreferences sharedPreferences = context.getSharedPreferences(file,0);
        sharedPreferences.edit().remove(id).apply();
    }

    public static boolean checkFav(Context context, String id){
        SharedPreferences sharedPreferences = context.getSharedPreferences(file,0);
        Map<String,?> mFav =  sharedPreferences.getAll();
         for(Map.Entry<String,?> entry: mFav.entrySet() ){
             if(entry.getKey().equalsIgnoreCase(id)){
                 return true;
             }
        }
        return false;
    }


}
