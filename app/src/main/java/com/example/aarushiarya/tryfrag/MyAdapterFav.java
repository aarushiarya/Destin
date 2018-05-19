package com.example.aarushiarya.tryfrag;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aarushiarya.tryfrag.Model.Results;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MyAdapterFav extends RecyclerView.Adapter<MyAdapterFav.ViewHolder> {

        private List<ListItem> listItems;
        Context context;
        ArrayList<Results> places = new ArrayList<Results>();

    public MyAdapterFav(List<ListItem> listItems, Context context) {
            this.listItems = listItems;
            this.context = context;
        }

        @NonNull
        @Override
        public MyAdapterFav.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item,parent,false);
            return new MyAdapterFav.ViewHolder(v,context,places);
        }

        @Override
        public void onBindViewHolder(@NonNull final MyAdapterFav.ViewHolder holder, final int position) {
            final ListItem listItem = listItems.get(position);
            final String name = listItem.getHead();

            holder.textViewHead.setText(listItem.getHead());
            holder.textViewDesc.setText(listItem.getDesc());
            Picasso.get().load(listItem.getImageUrl()).into(holder.imageView);

            if(MyStore.checkFav(context,listItem.getPlace_id())){
                holder.btnFav.setImageResource(R.drawable.ic_red_filled_heart);
            }

            holder.btnFav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(holder.btnFav.getDrawable().getConstantState() == v.getContext().getResources().getDrawable(R.drawable.ic_red_filled_heart).getConstantState()){
                        Toast.makeText(v.getContext(),name+" was removed from favorites",Toast.LENGTH_SHORT).show();
                        holder.btnFav.setImageResource(R.drawable.ic_blank_heart);
                        //remove fav
                        MyStore.removeFav(v.getContext(),listItem.getPlace_id());
                        holder.delete(holder.getAdapterPosition());
                    }else{
                        Toast.makeText(v.getContext(),name+" was added to favorites",Toast.LENGTH_SHORT).show();
                        holder.btnFav.setImageResource(R.drawable.ic_red_filled_heart);
                        //add fav
                        MyStore.addFav(v.getContext(),listItem);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return listItems.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
            public TextView textViewHead;
            public TextView textViewDesc;
            public ImageView imageView;
            public ImageView btnFav;
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
                btnFav = (ImageView) itemView.findViewById(R.id.favHeart);
            }

            public void delete(int position){
                listItems.remove(position);
                notifyItemRemoved(position);
            }

            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();
                //Results places = this.places.get(position);
                //listItems[position] =
                ListItem listItem = listItems.get(position);
                String name = listItems.get(position).getHead();
                String id = listItems.get(position).getPlace_id();

                Intent intent = new Intent(this.context,detailActivity.class);
                //intent.putExtra("position",position);
                //Create the bundle
                Bundle bundle = new Bundle();

                //Add your data to bundle
                bundle.putString("name", name);
                bundle.putString("placeId",id);

                //Add the bundle to the intent
                intent.putExtras(bundle);

                //Fire that second activity
                this.context.startActivity(intent);
                //this.context.startActivity(intent);
            }
        }

    }
