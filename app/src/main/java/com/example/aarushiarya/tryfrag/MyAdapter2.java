package com.example.aarushiarya.tryfrag;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.aarushiarya.tryfrag.Model.Results;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class MyAdapter2 extends RecyclerView.Adapter<MyAdapter2.ViewHolder>

{
    private View view;
    private List<ListItem2> listItems;
    Context context;
    ArrayList<Results> places = new ArrayList<Results>();


    public MyAdapter2(View view, Context context, List < ListItem2 > listItems) {
        this.view = view;
        this.listItems = listItems;
        this.context = context;
        //this.places = places;
    }

    @NonNull
    @Override
    public MyAdapter2.ViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item2, parent, false);
        return new MyAdapter2.ViewHolder(view,context,places);
    }

    @Override
    public void onBindViewHolder (@NonNull ViewHolder holder,int position){
        ListItem2 listItem = listItems.get(position);

        holder.textViewHead.setText(listItem.getHead());
        holder.textViewDesc.setText(listItem.getDesc());
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd hh:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        holder.textViewTime.setText(sdf.format(new Date(Long.parseLong(listItem.getTime())*1000)));
        holder.ratingBar.setRating(Float.parseFloat(listItem.getRating()));

        Picasso.get().load(listItem.getImageUrl()).into(holder.imageView);
    }

    @Override
    public int getItemCount () {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView textViewHead;
        public TextView textViewDesc;
        public TextView textViewTime;
        public ImageView imageView;
        public RatingBar ratingBar;
        Context context;
        ArrayList<Results> places = new ArrayList<Results>();


        public ViewHolder(View itemView, Context context, ArrayList<Results> places) {
            super(itemView);
            this.places = places;
            this.context = context;

            itemView.setOnClickListener(this);

            textViewHead = (TextView) itemView.findViewById(R.id.textViewHead);
            textViewDesc = (TextView) itemView.findViewById(R.id.textViewDesc);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            textViewTime = itemView.findViewById(R.id.textViewTime);
            ratingBar = itemView.findViewById(R.id.ratingBar);
        }

        public void onClick(View v) {
            int position = getAdapterPosition();
            //Results places = this.places.get(position);
            //listItems[position] =
            ListItem2 listItem = listItems.get(position);
            String name = listItem.getAuthor_url();

            Intent br = new Intent(Intent.ACTION_VIEW, Uri.parse(name));
            v.getContext().startActivity(br);
        }

    }
}