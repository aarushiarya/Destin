package com.example.aarushiarya.tryfrag;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class MyAdapterPhoto extends RecyclerView.Adapter<MyAdapterPhoto.ViewHolder> {

    private List<listItemPhoto> listItems;
    Context context;
    View view;
    ArrayList<Bitmap> bitmaps;

    public MyAdapterPhoto(View view, Context context , List<listItemPhoto> listItems) {
        this.view = view;
        this.listItems = listItems;
        this.context = context;
    }

    @NonNull
    @Override
    public MyAdapterPhoto.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_photo,parent,false);
        return new ViewHolder(v,context,bitmaps);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapterPhoto.ViewHolder holder, int position) {
        listItemPhoto listItem = listItems.get(position);
        holder.imageView.setImageBitmap(listItem.getBitmap());
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        Context context;
        ArrayList<Bitmap> bitmaps;
        ImageView imageView;

        public ViewHolder(View itemView,Context context, ArrayList<Bitmap> bitmaps) {
            super(itemView);
            this.context = context;
            this.bitmaps = bitmaps;

            imageView = (ImageView) itemView.findViewById(R.id.imageViewPhoto);
        }
    }
}